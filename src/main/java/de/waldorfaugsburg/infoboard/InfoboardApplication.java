package de.waldorfaugsburg.infoboard;

import de.waldorfaugsburg.infoboard.streamdeck.StreamDeck;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class InfoboardApplication {

    public void startup() {
        final StreamDeck streamDeck = new StreamDeck("AL35J2C04181");
        streamDeck.clear();
        streamDeck.setBrightness(100);

        try {
            streamDeck.setImage(2, ImageIO.read(new File("C:\\Users\\Linus Groschke\\Pictures\\logo.png")));

            final BufferedImage image = new BufferedImage(72, 72, 13);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.RED);
            graphics.fillRect(0, 0, 72, 72);


            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Arial", Font.BOLD, 14));
            graphics.drawString("Das ist", 10, 30);
            graphics.drawString("ein Test!", 5, 45);
            graphics.dispose();

            streamDeck.setImage(3, image);
            streamDeck.setImage(14, ImageIO.read(new File("C:\\Users\\Linus Groschke\\Pictures\\eu.jpg")));
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    public void shutdown() {

    }
}