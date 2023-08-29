package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import de.waldorfaugsburg.infoboard.config.action.AbstractButtonAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InfoboardFrame extends JFrame {

    private static final int BORDER = 10;
    private static final int GRID_ROWS = 3;
    private static final int GRID_COLUMNS = 5;
    private static final int BUTTON_COUNT = GRID_ROWS * GRID_COLUMNS;

    private static final int NON_PRODUCTION_WIDTH = 1280;
    private static final int NON_PRODUCTION_HEIGHT = 720;

    private final InfoboardApplication application;
    private final Map<Integer, JButton> buttonMap = new HashMap<>();

    public InfoboardFrame(final InfoboardApplication application, final boolean production) throws HeadlessException {
        this.application = application;

        setTitle("Infoboard – Freie Waldorfschule Augsburg");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        if (production) {
            setExtendedState(MAXIMIZED_BOTH);
            setUndecorated(true);
        } else {
            setSize(NON_PRODUCTION_WIDTH, NON_PRODUCTION_HEIGHT);
            updateMenuBar();
        }

        final JPanel buttonGrid = new JPanel();
        buttonGrid.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER), BorderFactory.createLoweredBevelBorder()));

        final GridLayout gridLayout = new GridLayout(GRID_ROWS, GRID_COLUMNS);
        buttonGrid.setLayout(gridLayout);

        // Fill button grid
        for (int i = 0; i < BUTTON_COUNT; i++) {
            final JButton button = new JButton();
            button.setFocusable(false);
            button.setFocusPainted(false);

            buttonGrid.add(button);
            buttonMap.put(i, button);
        }

        add(buttonGrid, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (application.hasConfigurationChanged()) {
                final int option = JOptionPane.showConfirmDialog(
                        this,
                        "Es wurden Änderungen vorgenommen. Möchten Sie diese speichern?",
                        "Speichervorgang",
                        JOptionPane.YES_NO_CANCEL_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    application.saveConfiguration();
                } else if (option == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            System.exit(0);
        }
    }

    public void updateMenuBar() {
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
        createMenuItem.addActionListener(e -> new SettingsFrame(this, application));

        menuSelector.add(createMenuItem);
        menuSelector.addSeparator();

        for (final InfoboardMenu menu : application.getConfiguration().getMenus()) {
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(menu.getName(), application.getMenuRegistry().getCurrentMenuId() == menu.getId());
            item.addActionListener(e -> application.getMenuRegistry().changeMenu(menu.getId()));
            menuSelector.add(item);
        }

        menuBar.add(fileMenu);
        menuBar.add(menuSelector);

        // Add menubar to frame
        setJMenuBar(menuBar);
    }

    public void clear() {
        updateMenuBar();

        // Clear the buttons
        for (final JButton button : buttonMap.values()) {
            button.setText("");
            button.setIcon(null);
            button.setFont(new Font(button.getFont().getName(), Font.BOLD, 14));

            // Remove all action listeners
            for (final ActionListener listener : button.getActionListeners()) {
                button.removeActionListener(listener);
            }
        }
    }

    public void addButton(final InfoboardButton button) {
        final JButton frameButton = buttonMap.get(button.getIndex());
        if (frameButton == null) throw new IllegalStateException("button out of index");

        frameButton.setText(button.getName());
        frameButton.addActionListener(e -> {
            final AbstractButtonAction action = button.getAction();
            if (action == null) return;

            action.run(application);
        });

        final JPopupMenu popupMenu = new JPopupMenu();

        final JMenuItem editName = new JMenuItem("Name ändern");
        editName.addActionListener(e -> {
            final String name = JOptionPane.showInputDialog(
                    this,
                    "Neuen Namen eingeben",
                    "Namen ändern",
                    JOptionPane.PLAIN_MESSAGE);

            button.setName(name);
            addButton(button);
        });

        popupMenu.add(editName);

        final JMenuItem editIcon = new JMenuItem("Icon ändern");
        editIcon.addActionListener(e -> new ButtonIconFrame(this, application, button));
        popupMenu.add(editIcon);

        final JMenuItem editAction = new JMenuItem("Aktion ändern");
        popupMenu.add(editAction);

        frameButton.setComponentPopupMenu(popupMenu);
    }

    public void handleButtonAction(final int key, final boolean pressed) {
        final JButton frameButton = buttonMap.get(key);
        if (frameButton == null) throw new IllegalStateException("button out of index");

        final ButtonModel model = frameButton.getModel();
        model.setPressed(pressed);
        model.setArmed(pressed);
    }
}
