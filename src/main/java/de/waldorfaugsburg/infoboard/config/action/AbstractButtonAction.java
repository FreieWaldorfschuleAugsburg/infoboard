package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionFrame;

import javax.swing.*;

public abstract class AbstractButtonAction {

    private final ButtonActionType type;

    protected AbstractButtonAction(final ButtonActionType type) {
        this.type = type;
    }

    public abstract void run(final InfoboardApplication application);

    public abstract void createSettingsForm(final InfoboardApplication application, final ButtonActionFrame frame, final JPanel contentPane);

    public ButtonActionType getType() {
        return type;
    }
}
