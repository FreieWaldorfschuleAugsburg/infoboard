package de.waldorfaugsburg.infoboard.config.icon;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.waldorfaugsburg.infoboard.streamdeck.StreamDeck;
import de.waldorfaugsburg.infoboard.window.ButtonIconFrame;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ImageIcon extends AbstractStreamDeckIcon {

    private static final LoadingCache<String, BufferedImage> IMAGE_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public BufferedImage load(final String path) throws Exception {
                    return Scalr.resize(ImageIO.read(new File(path)), StreamDeck.IMAGE_SIZE);
                }
            });

    private static final BufferedImage BLANK = new BufferedImage(StreamDeck.IMAGE_SIZE, StreamDeck.IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);

    private String path;

    public ImageIcon() {
        super(StreamDeckIconType.IMAGE);
    }

    @Override
    public BufferedImage createImage() throws IOException {
        if (path == null || path.isBlank()) {
            return BLANK;
        }

        try {
            return IMAGE_CACHE.get(path);
        } catch (final ExecutionException e) {
            log.error("Error occurred while loading image {}", path, e);
            return BLANK;
        }
    }

    @Override
    public void createSettingsForm(final ButtonIconFrame frame, final JPanel contentPane) {
        final JLabel pathLabel = new JLabel("Pfad");
        pathLabel.setBounds(0, 6, 46, 14);
        contentPane.add(pathLabel);

        final JTextField pathField = new JTextField(10);
        pathField.setBounds(56, 0, 280, 26);
        pathField.setText(path);
        pathField.addActionListener(e -> {
            final File file = new File(pathField.getText());
            if (!file.exists() || isNoImage(file)) {
                displayImageError(frame);
                pathField.setText(path);
                return;
            }

            path = file.getAbsolutePath();
        });
        contentPane.add(pathField);

        final JButton searchButton = new JButton("Durchsuchen...");
        searchButton.setBounds(346, 0, 109, 26);
        searchButton.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            final int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();
                if (isNoImage(selectedFile)) {
                    displayImageError(frame);
                    return;
                }

                path = selectedFile.getAbsolutePath();
                pathField.setText(path);
                frame.updateImage();
            }
        });
        contentPane.add(searchButton);
    }

    private void displayImageError(final ButtonIconFrame frame) {
        JOptionPane.showMessageDialog(
                frame,
                "Die angegebene Datei existiert nicht, oder ist keine Bilddatei.",
                "Ungültige Datei",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean isNoImage(final File file) {
        try {
            return !Files.probeContentType(file.toPath()).contains("image");
        } catch (IOException e) {
            log.error("Error while probing content type for file {}", file.getAbsolutePath(), e);
        }
        return true;
    }
}
