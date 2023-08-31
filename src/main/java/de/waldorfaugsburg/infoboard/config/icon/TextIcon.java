package de.waldorfaugsburg.infoboard.config.icon;

import de.waldorfaugsburg.infoboard.window.ButtonIconFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;

public class TextIcon extends AbstractStreamDeckIcon {

    private final static int ICON_SIZE = 72;

    private String fontFamily = "Arial";
    private int fontSize = 12;
    private String text = "Hallo Welt";

    public TextIcon(final String text) {
        this();
        this.text = text;
    }

    public TextIcon() {
        super(StreamDeckIconType.TEXT);
    }

    @Override
    public BufferedImage createImage() {
        final BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font(fontFamily, Font.PLAIN, fontSize));

        final FontMetrics fm = graphics.getFontMetrics();

        // Draw lines centered
        final String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];

            final int x = (image.getWidth() - fm.stringWidth(line)) / 2;
            final int y = (fm.getAscent() + (image.getHeight() - ((fm.getAscent() + fm.getDescent()) * (lines.length) - 1)) / 2);
            graphics.drawString(line, x, y + (i * fm.getHeight()));
        }

        graphics.dispose();
        return image;
    }

    @Override
    public void createSettingsForm(final ButtonIconFrame frame, final JPanel contentPane) {
        final JLabel fontFamilyLabel = new JLabel("Schriftart");
        fontFamilyLabel.setBounds(0, 4, 46, 14);
        contentPane.add(fontFamilyLabel);

        final JComboBox<String> fontFamilySelector = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontFamilySelector.setBounds(66, 0, 406, 22);
        fontFamilySelector.setSelectedItem(fontFamily);
        fontFamilySelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                fontFamily = (String) fontFamilySelector.getSelectedItem();
                frame.updateImage();
            }
        });
        contentPane.add(fontFamilySelector);

        final JLabel fontSizeLabel = new JLabel("Schriftgröße");
        fontSizeLabel.setBounds(0, 32, 59, 14);
        contentPane.add(fontSizeLabel);

        final JSpinner fontSizeSelector = new JSpinner();
        fontSizeSelector.setBounds(66, 29, 46, 20);
        fontSizeSelector.setValue(fontSize);
        fontSizeSelector.addChangeListener(e -> {
            fontSize = (int) fontSizeSelector.getValue();
            frame.updateImage();
        });
        contentPane.add(fontSizeSelector);

        final JLabel textLabel = new JLabel("Text");
        textLabel.setBounds(0, 57, 59, 14);
        contentPane.add(textLabel);

        final JScrollPane textScrollPane = new JScrollPane();
        textScrollPane.setBounds(66, 60, 406, 189);
        contentPane.add(textScrollPane);

        final JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                text = textArea.getText();
                frame.updateImage();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                text = textArea.getText();
                frame.updateImage();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                text = textArea.getText();
                frame.updateImage();
            }
        });

        textScrollPane.setViewportView(textArea);
    }
}
