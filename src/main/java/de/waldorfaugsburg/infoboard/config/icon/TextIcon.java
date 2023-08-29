package de.waldorfaugsburg.infoboard.config.icon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TextIcon extends AbstractStreamDeckIcon {

    private final static int ICON_SIZE = 72;
    private final static Font FONT = new Font("Arial", Font.BOLD, 8);

    private String text = "Hallo Welt";

    public TextIcon() {
        super(StreamDeckIconType.TEXT);
    }

    @Override
    public BufferedImage createImage() {
        final BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setColor(Color.WHITE);
        graphics.setFont(FONT);

        // Calculate center
        final FontMetrics fm = graphics.getFontMetrics();
        final int x = (image.getWidth() - fm.stringWidth(text)) / 2;
        final int y = (fm.getAscent() + (image.getHeight() - (fm.getAscent() + fm.getDescent())) / 2);
        graphics.drawString(text, x, y);

        graphics.dispose();
        return image;
    }

    @Override
    public void createSettingsForm(final JPanel panel) {
        final JTextField textField = new JTextField(text);

        textField.addActionListener(e -> {
            text = textField.getText();
            System.out.println(text);
        });

        panel.add(textField);
    }
}
