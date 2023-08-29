package de.waldorfaugsburg.infoboard.config.icon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageIcon extends AbstractStreamDeckIcon {

    private String path;

    public ImageIcon() {
        super(StreamDeckIconType.IMAGE);
    }

    @Override
    public BufferedImage createImage() throws IOException {
        return ImageIO.read(new File(path));
    }

    @Override
    public void createSettingsForm(final JPanel panel) {
        System.out.println("called");
        final JTextField pathField = new JTextField(path);

        pathField.addActionListener(e -> {
            path = pathField.getText();
            System.out.println(path);
        });

        panel.add(pathField);
    }
}
