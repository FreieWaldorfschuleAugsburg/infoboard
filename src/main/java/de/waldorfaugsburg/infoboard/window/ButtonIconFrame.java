package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.config.InfoboardButton;
import de.waldorfaugsburg.infoboard.config.icon.StreamDeckIconType;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ButtonIconFrame extends JDialog {

    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    private final InfoboardApplication application;
    private final InfoboardButton button;

    private final JPanel settingsPane;
    private final JLabel iconLabel;

    public ButtonIconFrame(final JFrame parent, final InfoboardApplication application, final InfoboardButton button) throws HeadlessException {
        super(parent);
        this.application = application;
        this.button = button;

        setTitle(button.getName() + " – Icon ändern");
        setSize(600, 360);
        setResizable(false);
        setLocationRelativeTo(parent);

        final JPanel contentPane = new JPanel();
        contentPane.setBorder(EMPTY_BORDER);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        iconLabel = new JLabel();
        iconLabel.setBounds(502, 11, 72, 72);
        updateImage();
        contentPane.add(iconLabel);

        final JComboBox<StreamDeckIconType> typeSelector = new JComboBox<>(StreamDeckIconType.values());
        typeSelector.setBounds(10, 11, 200, 22);
        typeSelector.setSelectedItem(button.getStreamDeckIcon().getType());
        typeSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                button.setStreamDeckIcon(((StreamDeckIconType) typeSelector.getSelectedItem()).getNewInstance());
                updateSettingsForm();
                updateImage();
            }
        });
        typeSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof StreamDeckIconType iconType) {
                    value = iconType.getName();
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
        button.getStreamDeckIcon().createSettingsForm(this, settingsPane);
        settingsPane.repaint();
        settingsPane.revalidate();
    }

    public void updateImage() {
        try {
            final BufferedImage image = button.getStreamDeckIcon().createImage();
            iconLabel.setIcon(new ImageIcon(image));
        } catch (final IOException e) {
            iconLabel.setText(e.getMessage());
        }
        application.getMenuRenderer().updateMenu();
    }

    public InfoboardApplication getApplication() {
        return application;
    }
}
