package de.waldorfaugsburg.infoboard.config.action;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import de.waldorfaugsburg.infoboard.window.ButtonActionsFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class MonitorSleepAction extends AbstractButtonAction {

    protected MonitorSleepAction() {
        super(ButtonActionType.MONITOR_SLEEP);
    }

    @Override
    public void run(final InfoboardApplication application) {
        try {
            final URL url = new URL(application.getConfiguration().getMonitorSleepWebhook());
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                log.error("HTTP error while putting monitor to sleep - status {}", connection.getResponseCode());
            }
        } catch (final IOException e) {
            log.error("Error while putting monitor to sleep", e);
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
