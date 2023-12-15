package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class MaximizeAction extends AbstractButtonAction {

    protected MaximizeAction() {
        super(ButtonActionType.MAXIMIZE);
    }

    @Override
    public void run(final InfoboardApplication application) {
        application.getFrame().setState(Frame.NORMAL);
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName();
    }
}
