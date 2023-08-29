package de.waldorfaugsburg.infoboard.config.icon;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class AbstractStreamDeckIcon {

    private final StreamDeckIconType type;

    protected AbstractStreamDeckIcon(final StreamDeckIconType type) {
        this.type = type;
    }

    public abstract BufferedImage createImage() throws IOException;

    public abstract void createSettingsForm(final JPanel panel);

    public StreamDeckIconType getType() {
        return type;
    }
}
