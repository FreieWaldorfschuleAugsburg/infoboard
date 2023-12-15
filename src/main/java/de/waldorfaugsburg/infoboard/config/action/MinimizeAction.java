package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class MinimizeAction extends AbstractButtonAction {

    protected MinimizeAction() {
        super(ButtonActionType.MINIMIZE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        application.getFrame().setState(Frame.ICONIFIED);
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName();
    }
}
