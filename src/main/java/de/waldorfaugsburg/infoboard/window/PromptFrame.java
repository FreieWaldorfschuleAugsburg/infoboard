package de.waldorfaugsburg.infoboard.window;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PromptFrame extends JDialog {

    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    public PromptFrame(final JFrame parent,
                       final String headline,
                       final String message,
                       final Color textColor,
                       final Color backgroundColor,
                       final int seconds) throws HeadlessException {
        super(parent);

        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        setUndecorated(true);

        final JPanel contentPane = new JPanel();
        contentPane.setBorder(EMPTY_BORDER);

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(backgroundColor);

        final JLabel label = new JLabel("<html><center><h1>" + headline + "</h1><p>" + message + "</p></center></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 25));
        label.setForeground(textColor);

        contentPane.add(label, BorderLayout.CENTER);

        setVisible(true);

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(this::dispose, seconds, TimeUnit.SECONDS);
    }

}
