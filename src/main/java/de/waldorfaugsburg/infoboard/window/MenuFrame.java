package de.waldorfaugsburg.infoboard.window;

import de.waldorfaugsburg.infoboard.InfoboardApplication;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JDialog {

    private final InfoboardApplication application;

    public MenuFrame(final JFrame parent, final InfoboardApplication application) throws HeadlessException {
        super(parent);
        this.application = application;

        setSize(600, 400);
        setResizable(false);
        setVisible(true);
    }
}
