package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class MenuManagementFrame extends JDialog {

    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    private final InfoboardApplication application;

    private final JList<InfoboardMenu> menuList;
    private final JLabel titleLabel;
    private final JPanel settingsPane;

    public MenuManagementFrame(final JFrame parent, final InfoboardApplication application) throws HeadlessException {
        super(parent);
        this.application = application;

        setTitle("Menüs verwalten – Infoboard");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        final JPanel contentPane = new JPanel();
        contentPane.setBorder(EMPTY_BORDER);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        final JSeparator verticalSeparator = new JSeparator();
        verticalSeparator.setOrientation(SwingConstants.VERTICAL);
        verticalSeparator.setBounds(220, 11, 2, 339);
        contentPane.add(verticalSeparator);

        final JSeparator horizontalSeparator = new JSeparator();
        horizontalSeparator.setBounds(222, 35, 352, 2);
        contentPane.add(horizontalSeparator);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 200, 272);
        contentPane.add(scrollPane);

        menuList = new JList<>(application.getConfiguration().getMenus().toArray(new InfoboardMenu[0]));
        menuList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof InfoboardMenu menu) {
                    value = menu.getName();
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        menuList.addListSelectionListener(e -> updateSettingsForm());
        scrollPane.setViewportView(menuList);

        final JTextField menuName = new JTextField();
        menuName.setBounds(10, 294, 200, 20);
        menuName.setColumns(10);
        contentPane.add(menuName);

        final JButton addButton = new JButton("Hinzufügen");
        addButton.setBounds(10, 327, 89, 23);
        addButton.addActionListener(e -> {
            final String menuNameText = menuName.getText();
            if (!menuNameText.isBlank()) {
                final InfoboardMenu menu = new InfoboardMenu(UUID.randomUUID(), menuNameText, new HashMap<>());
                application.getConfiguration().addMenu(menu);
                updateList();
                application.getMenuRenderer().updateMenu();
            }
        });
        contentPane.add(addButton);

        final JButton removeButton = new JButton("Entfernen");
        removeButton.setBounds(121, 327, 89, 23);
        removeButton.addActionListener(e -> {
            final InfoboardMenu selectedMenu = menuList.getSelectedValue();
            if (selectedMenu.getId().equals(application.getConfiguration().getMainMenuId())) {
                JOptionPane.showMessageDialog(parent, "Das Hauptmenü kann nicht gelöscht werden.", "Löschvorgang", JOptionPane.ERROR_MESSAGE);
                return;
            }

            application.getConfiguration().removeMenu(selectedMenu.getId());
            updateList();
            application.getMenuRenderer().updateMenu();
        });
        contentPane.add(removeButton);

        titleLabel = new JLabel("Hauptmenü");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBounds(232, 11, 342, 20);
        contentPane.add(titleLabel);

        settingsPane = new JPanel();
        settingsPane.setBounds(228, 42, 346, 47);
        settingsPane.setLayout(null);
        contentPane.add(settingsPane);

        updateSettingsForm();

        setVisible(true);
    }

    public void updateList() {
        final InfoboardMenu selectedMenu = menuList.getSelectedValue();
        menuList.setListData(application.getConfiguration().getMenus().toArray(new InfoboardMenu[0]));
        if (hasElement(selectedMenu, menuList)) {
            menuList.setSelectedValue(selectedMenu, true);
            updateSettingsForm();
        }

        menuList.updateUI();
    }

    private void updateSettingsForm() {
        titleLabel.setText("Menü auswählen...");
        settingsPane.removeAll();

        final InfoboardMenu selectedMenu = menuList.getSelectedValue();
        if (selectedMenu != null) {
            titleLabel.setText(selectedMenu.getName());

            final JLabel nameLabel = new JLabel("Menüname");
            nameLabel.setBounds(4, 3, 60, 14);
            settingsPane.add(nameLabel);

            final JTextField nameField = new JTextField();
            nameField.setBounds(63, 0, 283, 20);
            nameField.setColumns(10);
            nameField.setText(selectedMenu.getName());
            settingsPane.add(nameField);

            final JCheckBox mainMenuCheckbox = new JCheckBox("Hauptmenü");
            mainMenuCheckbox.setBounds(0, 24, 97, 23);
            mainMenuCheckbox.setSelected(selectedMenu.getId().equals(application.getConfiguration().getMainMenuId()));
            mainMenuCheckbox.addActionListener(e -> {
                if (mainMenuCheckbox.isSelected()) {
                    application.getConfiguration().setMainMenu(selectedMenu.getId());
                } else {
                    mainMenuCheckbox.setSelected(true);
                }
            });
            settingsPane.add(mainMenuCheckbox);
        }

        settingsPane.repaint();
        settingsPane.revalidate();
    }

    private boolean hasElement(final Object searched, final JList<?> list) {
        for (int a = 0; a < list.getModel().getSize(); a++) {
            final Object element = list.getModel().getElementAt(a);
            if (element.equals(searched)) {
                return true;
            }
        }
        return false;
    }
}
