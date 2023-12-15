package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BrightnessAction extends AbstractButtonAction {

    private int percentage = 100;

    protected BrightnessAction() {
        super(ButtonActionType.BRIGHTNESS);
    }

    @Override
    public void run(final InfoboardApplication application) {
        if (application.isProduction()) {
            application.getStreamDeck().setBrightness(percentage);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel keyLabel = new JLabel("Helligkeit");
        keyLabel.setBounds(0, 4, 46, 14);
        contentPane.add(keyLabel);

        final JSpinner percentageSelector = new JSpinner();
        percentageSelector.setBounds(56, 0, 286, 22);
        percentageSelector.setValue(percentage);
        percentageSelector.addChangeListener(e -> {
            percentage = (int) percentageSelector.getValue();
            frame.updateList();

            run(application);
        });
        contentPane.add(percentageSelector);
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName() + ": " + percentage + "%";
    }
}
