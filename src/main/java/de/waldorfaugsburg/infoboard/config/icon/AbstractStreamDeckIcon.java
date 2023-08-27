package de.waldorfaugsburg.infoboard.config.icon;

import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class AbstractStreamDeckIcon {

    public abstract BufferedImage createImage() throws IOException;
}
