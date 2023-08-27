package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfoboardFrame extends JFrame {

    private final InfoboardApplication application;
    private final Map<Integer, JButton> buttonMap = new HashMap<>();

    public InfoboardFrame(final InfoboardApplication application, final boolean production) throws HeadlessException {
        this.application = application;

        setTitle("Infoboard – Freie Waldorfschule Augsburg");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        if (production) {
            setExtendedState(MAXIMIZED_BOTH);
            setUndecorated(true);
        } else {
            setSize(1280, 720);
            updateMenuBar();
        }

        final JPanel buttonGrid = new JPanel();
        final GridLayout gridLayout = new GridLayout(3, 5);
        buttonGrid.setLayout(gridLayout);

        for (int i = 0; i < gridLayout.getRows() * gridLayout.getColumns(); i++) {
            final JButton button = new JButton();
            button.setFocusable(false);
            button.setFocusPainted(false);

            final JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(new JMenuItem("Beispiel"));
            popupMenu.addSeparator();
            button.setComponentPopupMenu(popupMenu);

            buttonGrid.add(button);
            buttonMap.put(i, button);
        }

        add(buttonGrid, BorderLayout.CENTER);
        setVisible(true);
    }

    private void updateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        // File menu
        final JMenu fileMenu = new JMenu("Datei");

        final JMenuItem saveItem = new JMenuItem("Speichern");
        saveItem.addActionListener(e -> application.saveConfiguration());

        final JMenuItem saveAsItem = new JMenuItem("Speichern unter ...");
        saveAsItem.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            final int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();
                application.saveConfiguration(selectedFile);
            }
        });

        final JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Menu selector
        final JMenu menuSelector = new JMenu("Menü");

        final JMenuItem createMenuItem = new JMenuItem("Menüs verwalten...");
        createMenuItem.addActionListener(e -> new MenuFrame(this, application));

        menuSelector.add(createMenuItem);
        menuSelector.addSeparator();

        for (final InfoboardMenu menu : application.getConfiguration().getMenus()) {
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(menu.getName(), application.getMenuRegistry().getCurrentMenuId() == menu.getId());
            item.addActionListener(e -> application.getMenuRegistry().changeMenu(menu.getId()));
            menuSelector.add(item);
        }

        menuBar.add(fileMenu);
        menuBar.add(menuSelector);

        setJMenuBar(menuBar);
    }

    public void clearAndUpdate() {
        updateMenuBar();
        buttonMap.values().forEach(button -> button.setText(""));
    }

    public JButton getButton(final int i) {
        return buttonMap.get(i);
    }
}
