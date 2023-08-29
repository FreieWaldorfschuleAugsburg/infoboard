package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;

import javax.swing.*;
import java.awt.*;

public class SettingsFrame extends JDialog {

    private final InfoboardApplication application;

    public SettingsFrame(final JFrame parent, final InfoboardApplication application) throws HeadlessException {
        super(parent);
        this.application = application;

        setTitle("Einstellungen – Infoboard");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLoweredBevelBorder()
        ));

        final JList<String> list = new JList<>(new String[]{"test"});
        list.setFixedCellWidth(200);
        panel.add(new JScrollPane(list), BorderLayout.WEST);

        add(panel);
        setVisible(true);
    }
}
