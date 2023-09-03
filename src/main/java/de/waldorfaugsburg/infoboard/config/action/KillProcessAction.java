package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class KillProcessAction extends AbstractButtonAction {

    private String processName;

    protected KillProcessAction() {
        super(ButtonActionType.KILL_PROCESS);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM " + processName);
        } catch (final IOException e) {
            log.error("Error while killing process {}", processName, e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel pathLabel = new JLabel("Prozess");
        pathLabel.setBounds(0, 4, 46, 14);
        contentPane.add(pathLabel);

        final JTextField processField = new JTextField(10);
        processField.setBounds(40, 1, 195, 20);
        processField.setText(processName);
        processField.addActionListener(e -> {
            processName = processField.getText();
            frame.updateList();
        });
        contentPane.add(processField);
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName() + ": " + processName;
    }
}
