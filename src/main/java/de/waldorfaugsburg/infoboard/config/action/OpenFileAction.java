package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardMenu;
import de.waldorfaugsburg.infoboard.window.ButtonActionFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class OpenFileAction extends AbstractButtonAction {

    private String path;

    protected OpenFileAction() {
        super(ButtonActionType.OPEN_FILE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (final IOException e) {
            log.error("Error while opening file {}", path, e);
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionFrame frame, final JPanel contentPane) {
        final JLabel pathLabel = new JLabel("Pfad");
        pathLabel.setBounds(0, 6, 46, 14);
        contentPane.add(pathLabel);

        final JTextField pathField = new JTextField(10);
        pathField.setBounds(56, 0, 280, 26);
        pathField.setText(path);
        pathField.addActionListener(e -> {
            final File file = new File(pathField.getText());

            path = file.getAbsolutePath();
        });
        contentPane.add(pathField);

        final JButton searchButton = new JButton("Durchsuchen...");
        searchButton.setBounds(346, 0, 109, 26);
        searchButton.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            final int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();

                path = selectedFile.getAbsolutePath();
                pathField.setText(path);
            }
        });
        contentPane.add(searchButton);
    }
}
