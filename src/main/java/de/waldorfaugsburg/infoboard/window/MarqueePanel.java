package de.waldorfaugsburg.infoboard.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Adapted version of the original code posted on StackOverflow on Sep 1, 2010
 * https://stackoverflow.com/questions/3617326/marquee-effect-in-java-swing
 */
public class MarqueePanel extends JPanel implements ActionListener {

    private static final int RATE = 5;
    private final Timer timer = new Timer(1000 / RATE, this);
    private final JLabel label = new JLabel();
    private final String s;
    private final int n;
    private int index;

    public MarqueePanel(final String s, final int n) {
        if (s == null || n < 1) {
            throw new IllegalArgumentException("Null string or n < 1");
        }

        final StringBuilder sb = new StringBuilder(n);
        sb.append(" ".repeat(n));
        this.s = sb + s + sb;
        this.n = n;
        label.setFont(new Font("Serif", Font.ITALIC, 36));
        label.setText(sb.toString());
        this.add(label);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        index++;
        if (index > s.length() - n) {
            index = 0;
        }
        label.setText(s.substring(index, index + n));
    }
}