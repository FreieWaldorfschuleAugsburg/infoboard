package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.action.AbstractButtonAction;
import de.waldorfaugsburg.infoboard.config.action.ButtonActionType;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ButtonActionsFrame extends JDialog {

    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    private final InfoboardApplication application;
    private final InfoboardButton button;

    private final JList<AbstractButtonAction> actionList;
    private final JLabel actionTitle;
    private final JPanel actionPane;

    public ButtonActionsFrame(final JFrame parent, final InfoboardApplication application, final InfoboardButton button) throws HeadlessException {
        super(parent);
        this.application = application;
        this.button = button;

        setTitle(button.getName() + " – Aktionen ändern");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        final JPanel contentPane = new JPanel();
        contentPane.setBorder(EMPTY_BORDER);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        final JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setBounds(220, 11, 2, 339);
        contentPane.add(separator);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 200, 272);
        contentPane.add(scrollPane);

        actionList = new JList<>(button.getActions().toArray(new AbstractButtonAction[0]));
        actionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof AbstractButtonAction action) {
                    value = action.getDescription(application);
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        actionList.addListSelectionListener(e -> updateSettingsForm());
        scrollPane.setViewportView(actionList);

        final JComboBox<ButtonActionType> actionTypeList = new JComboBox<>(ButtonActionType.values());
        actionTypeList.setBounds(10, 294, 200, 22);
        actionTypeList.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof ButtonActionType actionType) {
                    value = actionType.getName();
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        contentPane.add(actionTypeList);

        final JButton addButton = new JButton("Hinzufügen");
        addButton.setBounds(10, 327, 89, 23);
        addButton.addActionListener(e -> {
            button.getActions().add(((ButtonActionType) actionTypeList.getSelectedItem()).getNewInstance());
            updateList();
        });
        contentPane.add(addButton);

        final JButton removeButton = new JButton("Entfernen");
        removeButton.setBounds(121, 327, 89, 23);
        removeButton.addActionListener(e -> {
            final AbstractButtonAction selectedAction = actionList.getSelectedValue();
            if (selectedAction != null) {
                button.getActions().remove(selectedAction);
                updateList();
            }
        });
        contentPane.add(removeButton);

        actionTitle = new JLabel("Aktion auswählen...");
        actionTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
        actionTitle.setHorizontalAlignment(SwingConstants.LEFT);
        actionTitle.setBounds(232, 11, 342, 14);
        contentPane.add(actionTitle);

        actionPane = new JPanel();
        actionPane.setBounds(232, 38, 342, 50);
        actionPane.setLayout(null);
        contentPane.add(actionPane);

        setVisible(true);
    }

    public void updateList() {
        final AbstractButtonAction selectedAction = actionList.getSelectedValue();
        actionList.setListData(button.getActions().toArray(new AbstractButtonAction[0]));
        if (hasElement(selectedAction, actionList)) {
            actionList.setSelectedValue(selectedAction, true);
            updateSettingsForm();
        }

        actionList.updateUI();
    }

    private void updateSettingsForm() {
        actionTitle.setText("Aktion auswählen...");
        actionPane.removeAll();

        final AbstractButtonAction buttonAction = actionList.getSelectedValue();
        if (buttonAction != null) {
            actionTitle.setText(buttonAction.getDescription(application));
            buttonAction.createSettingsForm(application, this, actionPane);
        }

        actionPane.repaint();
        actionPane.revalidate();
    }

    private boolean hasElement(final Object searched, final JList<AbstractButtonAction> list) {
        for (int a = 0; a < list.getModel().getSize(); a++) {
            final Object element = list.getModel().getElementAt(a);
            if (element.equals(searched)) {
                return true;
            }
        }
        return false;
    }
}
