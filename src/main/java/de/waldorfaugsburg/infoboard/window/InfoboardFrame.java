package de.waldorfaugsburg.infoboard.window;

import javax.swing.*;
import java.awt.*;

public class InfoboardFrame extends JFrame {

    public InfoboardFrame() throws HeadlessException {
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setLayout(new BorderLayout());

        final JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bar.add(new JLabel("test"));

        final JPanel buttonGrid = new JPanel();
        final GridLayout gridLayout = new GridLayout(3, 5);
        buttonGrid.setLayout(gridLayout);

        final MarqueePanel ticker = new MarqueePanel("+++ Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. ", 210);
        ticker.start();

        for (int i = 0; i < gridLayout.getRows() * gridLayout.getColumns(); i++) {
            final JButton button = new JButton(Integer.toString(i));
            button.setEnabled(false);
            buttonGrid.add(button);
        }

        add(bar, BorderLayout.PAGE_START);
        add(buttonGrid, BorderLayout.CENTER);
        add(ticker, BorderLayout.PAGE_END);
        setVisible(true);
    }
}
