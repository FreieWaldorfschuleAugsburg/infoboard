package de.waldorfaugsburg.infoboard.streamdeck;

import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import lombok.extern.slf4j.Slf4j;
import org.hid4java.*;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Slf4j
public class StreamDeck {

    private static final HidServicesSpecification SPECIFICATION;
    private static final HidServices SERVICES;

    private static final int VENDOR_ID = 0xfd9;
    private static final int PRODUCT_ID = 0x6d;
    private static final byte IMAGE_REPORT_ID = 0x02;
    private static final byte PRIMARY_REPORT_ID = 0x03;
    private static final byte FIRMWARE_REPORT_ID = 0x05;

    private static final byte STRING_LENGTH = 32;
    private static final byte IMAGE_SIZE = 72;
    private static final byte READ_SIZE = 19;
    private static final byte READ_START_INDEX = 4;

    private static final int MAX_PACKET_SIZE = 1024;
    private static final int PACKET_HEADER_LENGTH = 8;
    private static final int MAX_PAYLOAD_SIZE = MAX_PACKET_SIZE - PACKET_HEADER_LENGTH;

    private static final Function<Integer, byte[]> BRIGHTNESS = percentage -> new byte[]{0x08, percentage.byteValue(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final byte[] CLEAR = new byte[]{0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final byte[] RESET_TO_LOGO = new byte[]{0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x6d, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    private static final byte[] OTHER = new byte[]{0x0d, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            (byte) 0x8c, 0x4b, 0x61, (byte) 0xd6, 0x3f, 0x02, 0x00, 0x00,
            0x08, 0x4d, 0x61, (byte) 0xd6, 0x3f, 0x02, 0x00, 0x00,
            (byte) 0xe0, 0x70, 0x53, 0x74, (byte) 0xf7, 0x7f, 0x00, 0x00};

    private final HidDevice device;
    private final String firmwareVersion;
    private final Set<StreamDeckListener> listeners = new HashSet<>();

    static {
        SPECIFICATION = new HidServicesSpecification();
        SPECIFICATION.setScanMode(ScanMode.NO_SCAN);
        SERVICES = new HidServices(SPECIFICATION);
    }

    public StreamDeck(final String serialNumber) {
        device = SERVICES.getHidDevice(VENDOR_ID, PRODUCT_ID, serialNumber);
        if (device == null) throw new IllegalArgumentException("device not found");

        device.open();

        final String rawFirmware = readFeatureReportAsString(FIRMWARE_REPORT_ID, STRING_LENGTH);
        firmwareVersion = rawFirmware.substring(5, rawFirmware.indexOf(0, 5));

        final Thread readThread = new Thread(this::readDevice);
        readThread.start();
    }

    private void readDevice() {
        Byte[] lastData = new Byte[READ_SIZE];
        Arrays.fill(lastData, (byte) 0x00);

        while (device.isOpen()) {
            final Byte[] data = device.read(READ_SIZE);

            for (int i = READ_START_INDEX; i < data.length; i++) {
                final int key = i - READ_START_INDEX;

                if (data[i] == 0x01 && lastData[i] == 0x00) {
                    listeners.forEach(listener -> listener.action(key, true));
                } else if (data[i] == 0x00 && lastData[i] == 0x01) {
                    listeners.forEach(listener -> listener.action(key, false));
                }
            }

            lastData = data;
        }
    }

    public void renderButton(final InfoboardButton button) {
        BufferedImage image;
        try {
            image = button.getStreamDeckIcon().createImage();
        } catch (final IOException e) {
            log.error("Error while creating icon for key {}", button.getIndex(), e);
            return;
        }

        renderImage(button.getIndex(), image);
    }

    public void renderImage(final int key, BufferedImage image) {
        if (key < 0 || key > 14) throw new IllegalArgumentException("out of bounds");

        if (image.getHeight() != IMAGE_SIZE || image.getWidth() != IMAGE_SIZE) {
            image = Scalr.resize(image, Scalr.Mode.AUTOMATIC, IMAGE_SIZE, IMAGE_SIZE);
        }

        // Rotate image
        image = rotateImage(image);

        final ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", imageStream);
        } catch (final IOException e) {
            log.error("Error while writing to stream", e);
        }

        byte[] imageBytes = imageStream.toByteArray();
        int remainingBytes = imageBytes.length;

        // Slice image data into packets
        for (int page = 0; remainingBytes > 0; page++) {
            final ByteArrayOutputStream packetStream = new ByteArrayOutputStream(MAX_PACKET_SIZE);
            final DataOutputStream packetDataStream = new DataOutputStream(packetStream);
            final int byteCount = Math.min(remainingBytes, MAX_PAYLOAD_SIZE);

            try {
                packetDataStream.write(0x07);
                packetDataStream.write(key);
                packetDataStream.write(remainingBytes <= MAX_PAYLOAD_SIZE ? 1 : 0);
                packetStream.write(byteCount & 0xFF);
                packetStream.write(byteCount >> 8);
                packetStream.write(page & 0xFF);
                packetStream.write(page >> 8);

                final int byteOffset = imageBytes.length - remainingBytes;
                remainingBytes -= byteCount;

                final byte[] part = Arrays.copyOfRange(imageBytes, byteOffset, byteOffset + byteCount);
                packetDataStream.write(part);
            } catch (final IOException e) {
                log.error("Error while writing to stream", e);
            }

            final byte[] packet = packetStream.toByteArray();
            device.write(packet, packet.length, IMAGE_REPORT_ID);
        }

        //device.sendFeatureReport(OTHER, PRIMARY_REPORT_ID);
    }

    public void setBrightness(final int percentage) {
        device.sendFeatureReport(BRIGHTNESS.apply(percentage), PRIMARY_REPORT_ID);
    }

    public void clear() {
        device.sendFeatureReport(CLEAR, PRIMARY_REPORT_ID);
    }

    public void resetToLogo() {
        device.sendFeatureReport(RESET_TO_LOGO, PRIMARY_REPORT_ID);
    }

    public void close() {
        resetToLogo();
        device.close();
    }

    public void addListener(final StreamDeckListener listener) {
        listeners.add(listener);
    }

    public HidDevice getDevice() {
        return device;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    private String readFeatureReportAsString(final byte reportId, final int length) {
        final byte[] buffer = new byte[length];
        device.getFeatureReport(buffer, reportId);
        return new String(buffer, StandardCharsets.US_ASCII);
    }

    private BufferedImage rotateImage(final BufferedImage original) {
        final BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        final Graphics2D graphics = image.createGraphics();
        graphics.transform(AffineTransform.getRotateInstance(Math.PI, original.getWidth() / 2D, original.getHeight() / 2D));
        graphics.drawImage(original, 0, 0, null);
        graphics.dispose();
        return image;
    }
}
