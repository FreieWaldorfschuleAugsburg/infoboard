package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class KeypressAction extends AbstractButtonAction {

    private int code = KeyEvent.VK_A;

    protected KeypressAction() {
        super(ButtonActionType.KEYPRESS);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            final Robot robot = new Robot();
            robot.keyPress(code);
            robot.keyRelease(code);
        } catch (final AWTException e) {
            log.error("Error while simulating keypress with key {}", code, e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel keyLabel = new JLabel("Taste");
        keyLabel.setBounds(0, 4, 46, 14);
        contentPane.add(keyLabel);

        final JComboBox<Integer> keySelector = new JComboBox<>(getKeyCodes());
        keySelector.setBounds(56, 0, 286, 22);
        keySelector.setSelectedItem(code);
        keySelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Integer keyCode) {
                    value = KeyEvent.getKeyText(keyCode);
                }

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        keySelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                code = (int) keySelector.getSelectedItem();
                frame.updateList();
            }
        });
        contentPane.add(keySelector);
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName() + ": " + KeyEvent.getKeyText(code);
    }

    private Integer[] getKeyCodes() {
        final Set<Integer> keyCodes = new HashSet<>();
        for (final Field f : KeyEvent.class.getDeclaredFields()) {
            try {
                if (Modifier.isStatic(f.getModifiers()) && f.getType() == int.class && f.getName().startsWith("VK")) {
                    f.setAccessible(true);
                    keyCodes.add((int) f.get(null));
                }
            } catch (final IllegalArgumentException | IllegalAccessException e) {
                log.error("Error while scraping key events", e);
            }
        }

        return keyCodes.toArray(new Integer[0]);
    }
}
