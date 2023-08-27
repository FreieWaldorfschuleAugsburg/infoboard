package de.waldorfaugsburg.infoboard.config.icon;

import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;

@NoArgsConstructor
public class TextIcon extends AbstractStreamDeckIcon {

    private String text;

    @Override
    public BufferedImage createImage() {
        final BufferedImage image = new BufferedImage(72, 72, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 72, 72);
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Abadi", Font.BOLD, 14));
        graphics.drawString(text, 10, 35);
        graphics.dispose();
        return image;
    }
}
