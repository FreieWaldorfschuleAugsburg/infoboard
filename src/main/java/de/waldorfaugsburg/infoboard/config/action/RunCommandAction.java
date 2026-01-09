package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;

@Slf4j
public class RunCommandAction extends AbstractButtonAction {

    private String[] command;

    protected RunCommandAction() {
        super(ButtonActionType.RUN_COMMAND);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            builder.start();
        } catch (final IOException e) {
            log.error("Error while running command {}", command, e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel pathLabel = new JLabel("Befehl");
        pathLabel.setBounds(0, 4, 46, 14);
        contentPane.add(pathLabel);

        /*final JTextField pathField = new JTextField(10);
        pathField.setBounds(34, 1, 195, 20);
        pathField.setText(command);
        pathField.addActionListener(e -> {
            command = pathField.getText();
            frame.updateList();
        });
        contentPane.add(pathField);*/
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName() + ": " + String.join(" ", command);
    }
}
