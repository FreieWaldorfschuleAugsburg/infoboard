package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.icon.StreamDeckIconType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ButtonIconFrame extends JDialog {

    private final InfoboardApplication application;
    private final InfoboardButton button;

    private JPanel customSettingsPanel;
    private JLabel previewImage;

    public ButtonIconFrame(final JFrame parent, final InfoboardApplication application, final InfoboardButton button) throws HeadlessException {
        super(parent);
        this.application = application;
        this.button = button;

        setTitle(button.getName() + " – Icon ändern");
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLoweredBevelBorder()));

        final JSplitPane splitPane = new JSplitPane();

        final JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JComboBox<StreamDeckIconType> typeSelector = new JComboBox<>(StreamDeckIconType.values());
        typeSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                button.setStreamDeckIcon(((StreamDeckIconType) typeSelector.getSelectedItem()).getNewInstance());
                SwingUtilities.invokeLater(this::updateSettingsForm);
            }
        });

        customSettingsPanel = new JPanel();
        final FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        layout.setHgap(20);
        customSettingsPanel.setLayout(layout);

        settingsPanel.add(typeSelector, BorderLayout.NORTH);
        settingsPanel.add(customSettingsPanel, BorderLayout.CENTER);

        System.out.println("caller");
        updateSettingsForm();

        final JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());

        previewImage = new JLabel("", SwingConstants.CENTER);
        updateImage();
        imagePanel.add(previewImage, BorderLayout.CENTER);

        splitPane.setDividerLocation(400);
        splitPane.setLeftComponent(settingsPanel);
        splitPane.setRightComponent(imagePanel);

        panel.add(splitPane);
        add(panel);
        setVisible(true);
    }

    private void updateSettingsForm() {
        customSettingsPanel.removeAll();
        System.out.println("kicked");

        System.out.println("run it");
        button.getStreamDeckIcon().createSettingsForm(customSettingsPanel);
    }

    private void updateImage() {
        try {
            final BufferedImage image = button.getStreamDeckIcon().createImage();
            previewImage.setIcon(new ImageIcon(image));
        } catch (final IOException e) {
            previewImage.setText(e.getMessage());
        }
    }
}
