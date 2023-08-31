package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import de.waldorfaugsburg.infoboard.config.action.ButtonActionType;
import de.waldorfaugsburg.infoboard.config.action.MenuChangeAction;
import de.waldorfaugsburg.infoboard.config.icon.StreamDeckIconType;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ButtonActionFrame extends JDialog {

    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    private final InfoboardApplication application;
    private final InfoboardButton button;

    private final JPanel settingsPane;

    public ButtonActionFrame(final JFrame parent, final InfoboardApplication application, final InfoboardButton button) throws HeadlessException {
        super(parent);
        this.application = application;
        this.button = button;

        setTitle(button.getName() + " – Aktion ändern");
        setSize(600, 360);
        setResizable(false);
        setLocationRelativeTo(parent);

        final JPanel contentPane = new JPanel();
        contentPane.setBorder(EMPTY_BORDER);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        if (button.getAction() == null) {
            button.setAction(ButtonActionType.values()[0].getNewInstance());
        }

        final JComboBox<ButtonActionType> typeSelector = new JComboBox<>(ButtonActionType.values());
        typeSelector.setBounds(10, 11, 200, 22);
        typeSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                button.setAction(((ButtonActionType) typeSelector.getSelectedItem()).getNewInstance());
                updateSettingsForm();
            }
        });
        typeSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof ButtonActionType actionType) {
                    value = actionType.getName();
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        contentPane.add(typeSelector);

        final JSeparator separator = new JSeparator();
        separator.setBounds(10, 44, 482, 2);
        contentPane.add(separator);

        settingsPane = new JPanel();
        settingsPane.setBounds(20, 61, 472, 249);
        settingsPane.setLayout(null);
        updateSettingsForm();
        contentPane.add(settingsPane);

        setVisible(true);
    }

    private void updateSettingsForm() {
        settingsPane.removeAll();
        button.getAction().createSettingsForm(application, this, settingsPane);
        settingsPane.repaint();
        settingsPane.revalidate();
    }
}
