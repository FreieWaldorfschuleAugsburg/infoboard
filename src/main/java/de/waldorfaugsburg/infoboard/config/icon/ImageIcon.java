package de.waldorfaugsburg.infoboard.config.icon;

import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@NoArgsConstructor
public class ImageIcon extends AbstractStreamDeckIcon {

    private String path;

    @Override
    public BufferedImage createImage() throws IOException {
        return ImageIO.read(new File(path));
    }
}
