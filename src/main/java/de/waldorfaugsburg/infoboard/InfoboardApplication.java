package de.waldorfaugsburg.infoboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.waldorfaugsburg.infoboard.config.InfoboardConfiguration;
import de.waldorfaugsburg.infoboard.config.JsonAdapter;
import de.waldorfaugsburg.infoboard.config.action.AbstractButtonAction;
import de.waldorfaugsburg.infoboard.config.icon.AbstractStreamDeckIcon;
import de.waldorfaugsburg.infoboard.menu.MenuRenderer;
import de.waldorfaugsburg.infoboard.streamdeck.StreamDeck;
import de.waldorfaugsburg.infoboard.window.InfoboardFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class InfoboardApplication {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(AbstractButtonAction.class, new JsonAdapter<AbstractButtonAction>()).registerTypeAdapter(AbstractStreamDeckIcon.class, new JsonAdapter<AbstractStreamDeckIcon>()).setPrettyPrinting().create();

    private InfoboardConfiguration configuration;
    private InfoboardFrame frame;
    private StreamDeck streamDeck;

    private MenuRenderer menuRenderer;

    public void startup() {
        try {
            configuration = readConfiguration();
        } catch (final IOException e) {
            log.error("Error loading configuration", e);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            log.error("Error while setting look and feel", e);
            System.exit(1);
        }

        menuRenderer = new MenuRenderer(this);

        // Create infoboard window
        frame = new InfoboardFrame(this, configuration.isProduction());

        // Only initialize stream deck if in production
        if (configuration.isProduction()) {
            initializeStreamDeck();
        }

        // Open main menu
        menuRenderer.changeMenu(configuration.getMainMenu());
    }

    private void initializeStreamDeck() {
        streamDeck = new StreamDeck(configuration.getStreamDeckSerial());
        streamDeck.addListener((key, pressed) -> frame.handleButtonAction(key, pressed));
    }

    public void shutdown() {
        if (streamDeck != null) {
            streamDeck.close();
        }
    }

    public InfoboardConfiguration getConfiguration() {
        return configuration;
    }

    public void saveConfiguration() {
        saveConfiguration(new File("config.json"));
    }

    private InfoboardConfiguration readConfiguration() throws IOException {
        final InfoboardConfiguration config;
        try (final FileReader reader = new FileReader("config.json", StandardCharsets.UTF_8)) {
            config = gson.fromJson(reader, InfoboardConfiguration.class);
        }
        return config;
    }

    public void saveConfiguration(final File path) {
        try (final FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(configuration, InfoboardConfiguration.class, writer);
        } catch (final IOException e) {
            log.error("Error while saving configuration");
        }
    }

    public boolean hasConfigurationChanged() {
        try {
            final String configurationContent = Files.readString(Path.of("config.json"));
            final String newConfigurationContent = gson.toJson(configuration);

            return !configurationContent.equals(newConfigurationContent);
        } catch (final IOException ignored) {
        }

        return false;
    }

    public Gson getGson() {
        return gson;
    }

    public InfoboardFrame getFrame() {
        return frame;
    }

    public StreamDeck getStreamDeck() {
        return streamDeck;
    }

    public MenuRenderer getMenuRenderer() {
        return menuRenderer;
    }
}