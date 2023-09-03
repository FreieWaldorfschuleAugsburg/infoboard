package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.UUID;

public class MenuChangeAction extends AbstractButtonAction {

    private UUID targetMenu;

    protected MenuChangeAction() {
        super(ButtonActionType.MENU_CHANGE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        application.getMenuRenderer().changeMenu(targetMenu);
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel fontFamilyLabel = new JLabel("Zielmenü");
        fontFamilyLabel.setBounds(0, 4, 46, 14);
        contentPane.add(fontFamilyLabel);

        final JComboBox<InfoboardMenu> menuSelector = new JComboBox<>(application.getConfiguration().getMenus().toArray(new InfoboardMenu[0]));
        menuSelector.setBounds(56, 0, 286, 22);
        menuSelector.setSelectedItem(application.getConfiguration().getMenu(targetMenu));
        menuSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof InfoboardMenu menu) {
                    value = menu.getName();
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        menuSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                targetMenu = ((InfoboardMenu) menuSelector.getSelectedItem()).getId();
                frame.updateList();
            }
        });
        contentPane.add(menuSelector);
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName() + ": " + application.getConfiguration().getMenu(targetMenu).getName();
    }
}
