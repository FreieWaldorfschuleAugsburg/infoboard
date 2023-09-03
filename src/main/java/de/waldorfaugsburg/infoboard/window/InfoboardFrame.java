package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import de.waldorfaugsburg.infoboard.config.icon.TextIcon;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InfoboardFrame extends JFrame {

    private static final int BORDER = 10;
    private static final int GRID_ROWS = 3;
    private static final int GRID_COLUMNS = 5;
    public static final int BUTTON_COUNT = GRID_ROWS * GRID_COLUMNS;

    private static final int NON_PRODUCTION_WIDTH = 1280;
    private static final int NON_PRODUCTION_HEIGHT = 720;

    private final InfoboardApplication application;
    private final Map<Integer, JButton> buttonMap = new HashMap<>();

    private InfoboardButton clipboardButton;

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
                final int option = JOptionPane.showConfirmDialog(this, "Es wurden Änderungen vorgenommen. Möchten Sie diese speichern?", "Speichervorgang", JOptionPane.YES_NO_CANCEL_OPTION);

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
        createMenuItem.addActionListener(e -> new MenuManagementFrame(this, application));

        menuSelector.add(createMenuItem);
        menuSelector.addSeparator();

        for (final InfoboardMenu menu : application.getConfiguration().getMenus()) {
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(menu.getName(), application.getMenuRenderer().getCurrentMenuId() == menu.getId());
            item.addActionListener(e -> application.getMenuRenderer().changeMenu(menu.getId()));
            menuSelector.add(item);
        }

        menuBar.add(fileMenu);
        menuBar.add(menuSelector);

        // Add menubar to frame
        setJMenuBar(menuBar);
    }

    public void clear() {
        if (!application.getConfiguration().isProduction()) {
            updateMenuBar();
        }

        // Clear the buttons
        for (final Map.Entry<Integer, JButton> entry : buttonMap.entrySet()) {
            final int index = entry.getKey();
            final JButton button = entry.getValue();

            final ButtonModel model = button.getModel();
            model.setPressed(false);
            model.setArmed(false);

            button.setText("");
            button.setIcon(null);
            button.setFont(new Font(button.getFont().getName(), Font.BOLD, 14));
            button.setTransferHandler(new ButtonTransferHandler(index));

            button.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(final MouseEvent e) {
                    final JButton sourceButton = (JButton) e.getSource();
                    final TransferHandler transferHandler = sourceButton.getTransferHandler();
                    transferHandler.exportAsDrag(sourceButton, e, TransferHandler.COPY);

                    for (final ActionListener listener : button.getActionListeners()) {
                        button.removeActionListener(listener);
                    }
                }
            });

            // Add popup menu
            final JPopupMenu popupMenu = new JPopupMenu();

            if (clipboardButton != null) {
                final JMenuItem pasteButton = new JMenuItem("Button einfügen");
                pasteButton.addActionListener(e -> {
                    final InfoboardButton clonedButton = application.getGson().fromJson(application.getGson().toJson(clipboardButton), InfoboardButton.class);
                    application.getMenuRenderer().getCurrentMenu().getButtons().put(index, clonedButton);

                    clipboardButton = null;
                    application.getMenuRenderer().updateMenu();
                });
                popupMenu.add(pasteButton);
            }

            final JMenuItem createButton = new JMenuItem("Button erstellen");
            createButton.addActionListener(e -> {
                final String name = JOptionPane.showInputDialog(this, "Namen eingeben", "Button ertellen", JOptionPane.PLAIN_MESSAGE);

                final InfoboardButton boardButton = new InfoboardButton(name, null, new TextIcon(name));
                application.getMenuRenderer().getCurrentMenu().getButtons().put(index, boardButton);
                application.getMenuRenderer().updateMenu();
            });
            popupMenu.add(createButton);

            button.setComponentPopupMenu(popupMenu);

            // Remove all action listeners
            for (final ActionListener listener : button.getActionListeners()) {
                button.removeActionListener(listener);
            }
        }
    }

    public void renderButton(final int index, final InfoboardButton button) {
        final JButton frameButton = buttonMap.get(index);
        if (frameButton == null) throw new IllegalStateException("button out of index");

        frameButton.setText(button.getName());
        frameButton.addActionListener(e -> button.getActions().forEach(action -> action.run(application)));

        // Override popup menu
        final JPopupMenu popupMenu = new JPopupMenu();

        final JMenuItem editName = new JMenuItem("Name ändern");
        editName.addActionListener(e -> {
            final String name = JOptionPane.showInputDialog(this, "Neuen Namen eingeben", "Namen ändern", JOptionPane.PLAIN_MESSAGE);

            button.setName(name);
            renderButton(index, button);
        });

        popupMenu.add(editName);

        final JMenuItem editIcon = new JMenuItem("Icon ändern");
        editIcon.addActionListener(e -> new ButtonIconFrame(this, application, button));
        popupMenu.add(editIcon);

        final JMenuItem editAction = new JMenuItem("Aktionen ändern");
        editAction.addActionListener(e -> new ButtonActionsFrame(this, application, button));
        popupMenu.add(editAction);

        popupMenu.addSeparator();

        final JMenuItem copyButton = new JMenuItem("Button kopieren");
        copyButton.addActionListener(e -> {
            clipboardButton = button;
            application.getMenuRenderer().updateMenu();
        });
        popupMenu.add(copyButton);

        final JMenuItem deleteButton = new JMenuItem("Button löschen");
        deleteButton.addActionListener(e -> {
            application.getMenuRenderer().getCurrentMenu().getButtons().values().remove(button);
            application.getMenuRenderer().updateMenu();
        });
        popupMenu.add(deleteButton);

        frameButton.setComponentPopupMenu(popupMenu);
    }

    public void handleButtonAction(final int key, final boolean pressed) {
        final JButton frameButton = buttonMap.get(key);
        if (frameButton == null) throw new IllegalStateException("button out of index");

        final ButtonModel model = frameButton.getModel();
        model.setPressed(pressed);
        model.setArmed(pressed);
    }

    private class ButtonTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

        private final int index;

        public ButtonTransferHandler(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public int getSourceActions(final JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new StringSelection(Integer.toString(getIndex()));
        }

        @Override
        public boolean canImport(final TransferSupport support) {
            return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
        }

        @Override
        public boolean importData(final TransferSupport support) {
            boolean accept = false;
            if (canImport(support)) {
                try {
                    final Transferable t = support.getTransferable();
                    final Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                    if (value instanceof String) {
                        final Component component = support.getComponent();
                        if (component instanceof JButton) {
                            final int sourceIndex = Integer.parseInt(value.toString());
                            final int targetIndex = ((ButtonTransferHandler) ((JButton) component).getTransferHandler()).getIndex();

                            accept = true;

                            final InfoboardButton targetButton = application.getMenuRenderer().getCurrentMenu().getButtons().get(targetIndex);
                            if (targetButton != null) {
                                final int override = JOptionPane.showConfirmDialog(application.getFrame(), "Auf dem Zielfeld befindet sich bereits ein Button. Möchtest du diesen überschreiben?", "Button überschreiben", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (override == JOptionPane.NO_OPTION) {
                                    accept = false;
                                }
                            }

                            if (accept) {
                                final InfoboardButton sourceButton = application.getMenuRenderer().getCurrentMenu().getButtons().remove(sourceIndex);
                                application.getMenuRenderer().getCurrentMenu().getButtons().put(targetIndex, sourceButton);
                                application.getMenuRenderer().updateMenu();
                            }
                        }
                    }
                } catch (final Exception e) {
                    log.error("Error while handling transfer", e);
                }
            }
            return accept;
        }
    }
}
