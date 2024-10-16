package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Slf4j
public class MonitorWakeAction extends AbstractButtonAction {

    private static final int WAKE_ON_LAN_PORT = 9;

    protected MonitorWakeAction() {
        super(ButtonActionType.MONITOR_WAKE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            final byte[] hardwareAddressBytes = getHardwareAddressBytes(application.getConfiguration().getMonitorHardwareAddress());
            final byte[] bytes = new byte[6 + 16 * hardwareAddressBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += hardwareAddressBytes.length) {
                System.arraycopy(hardwareAddressBytes, 0, bytes, i, hardwareAddressBytes.length);
            }

            final InetAddress address = InetAddress.getByName(application.getConfiguration().getMonitorAddress());
            final DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, WAKE_ON_LAN_PORT);
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.send(packet);
            }
        } catch (final Exception e) {
            log.error("Error while waking monitor", e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {

    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName();
    }

    private byte[] getHardwareAddressBytes(final String macAddress) throws IllegalArgumentException {
        final byte[] bytes = new byte[6];
        final String[] hex = macAddress.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
