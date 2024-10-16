package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class MonitorWakeAction extends AbstractButtonAction {

    protected OpenUrlAction() {
        super(ButtonActionType.MONITOR_SLEEP);
    }

    @Override
    public void run(final InfoboardApplication application) {
        
    }

    @Override
    public void createSettingsForm(final InfoboardApplication application, final ButtonActionsFrame frame, final JPanel contentPane) {
        
    }

    @Override
    public String getDescription(final InfoboardApplication application) {
        return getType().getName();
    }
}
