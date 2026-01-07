package de.waldorfaugsburg.infoboard.config.icon;

import de.waldorfaugsburg.infoboard.window.ButtonIconFrame;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class AbstractStreamDeckIcon {

    @Getter
    private final StreamDeckIconType type;

    protected AbstractStreamDeckIcon(final StreamDeckIconType type) {
        this.type = type;
    }

    public abstract void render(final Graphics2D g2d);

    public BufferedImage createImage() throws IOException {
        final BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = image.createGraphics();
        render(graphics);
        graphics.dispose();
        return image;
    }

    public abstract void createSettingsForm(final ButtonIconFrame frame, final JPanel contentPane);

    public int getWidth() {
        return 72;
    }

    public int getHeight() {
        return 72;
    }
}
