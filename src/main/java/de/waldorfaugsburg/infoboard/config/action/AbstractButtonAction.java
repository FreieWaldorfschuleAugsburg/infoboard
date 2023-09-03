package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;

import javax.swing.*;

public abstract class AbstractButtonAction {

    private final ButtonActionType type;

    protected AbstractButtonAction(final ButtonActionType type) {
        this.type = type;
    }

    public abstract void run(final InfoboardApplication application);

    public abstract void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane);

    public abstract String getDescription(final InfoboardApplication application);

    public ButtonActionType getType() {
        return type;
    }
}
