package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class OpenFileAction extends AbstractButtonAction {

    private String path;
    private String arguments;

    protected OpenFileAction() {
        super(ButtonActionType.OPEN_FILE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        final File file = new File(path);
        if (!file.exists()) {
            application.displayPrompt("Zieldatei nicht gefunden!", "Möglicherweise wurde die gesuchte Datei umbennant oder verschoben. Wenden Sie sich bitte an den Betreuer des Infoboards!", Color.WHITE, Color.RED, 5);
            log.error("Error opening non-existent file '{}'", path);
            return;
        }

        try {
            Runtime.getRuntime().exec("cmd /c start \"\" \"" + path + "\" " + arguments);
        } catch (final IOException e) {
            log.error("Error while opening file '{}'", path, e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        final JLabel argumentsLabel = new JLabel("Argumente");
        argumentsLabel.setBounds(0, 25, 53, 14);
        contentPane.add(argumentsLabel);

        final JTextField argumentsField = new JTextField(10);
        argumentsField.setBounds(63, 25, 166, 20);
        argumentsField.setText(arguments);
        argumentsField.addActionListener(e -> arguments = argumentsField.getText());
        contentPane.add(argumentsField);

        final JLabel pathLabel = new JLabel("Pfad");
        pathLabel.setBounds(0, 4, 46, 14);
        contentPane.add(pathLabel);

        final JTextField pathField = new JTextField(10);
        pathField.setBounds(34, 1, 195, 20);
        pathField.setText(path);
        pathField.addActionListener(e -> {
            final File file = new File(pathField.getText());

            path = file.getAbsolutePath();
            arguments = argumentsField.getText();

            frame.updateList();
        });
        contentPane.add(pathField);

        final JButton searchButton = new JButton("Durchsuchen...");
        searchButton.setBounds(235, 0, 107, 23);
        searchButton.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser(application.getLastFileChooserPath());
            final int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();

                path = selectedFile.getAbsolutePath();
                arguments = argumentsField.getText();

                application.setLastFileChooserPath(selectedFile.getParentFile().getAbsolutePath());

                pathField.setText(path);
                frame.updateList();
            }
        });
        contentPane.add(searchButton);
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName() + ": " + path;
    }
}
