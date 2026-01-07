package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

@Slf4j
public class CloseDialogsAction extends AbstractButtonAction {

    protected CloseDialogsAction() {
        super(ButtonActionType.CLOSE_DIALOGS);
    }

    @Override
    public void run(final InfoboardApplication application) {
        for (final Window ownedWindow : application.getFrame().getOwnedWindows()) {
            ownedWindow.dispose();
        }
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {

    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName();
    }
}
