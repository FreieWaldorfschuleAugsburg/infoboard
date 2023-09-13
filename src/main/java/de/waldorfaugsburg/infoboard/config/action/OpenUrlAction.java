package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class OpenUrlAction extends AbstractButtonAction {

    private String path;

    protected OpenUrlAction() {
        super(ButtonActionType.OPEN_URL);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            Runtime.getRuntime().exec("cmd /c start \"\" \"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\" --kiosk --fullscreen \"" + path + "\" ");
        } catch (final IOException e) {
            log.error("Error while opening file {}", path, e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel pathLabel = new JLabel("Pfad");
        pathLabel.setBounds(0, 4, 46, 14);
        contentPane.add(pathLabel);

        final JTextField pathField = new JTextField(10);
        pathField.setBounds(34, 1, 195, 20);
        pathField.setText(path);
        pathField.addActionListener(e -> {
            path = pathField.getText();
            frame.updateList();
        });
        contentPane.add(pathField);
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName() + ": " + path;
    }
}
