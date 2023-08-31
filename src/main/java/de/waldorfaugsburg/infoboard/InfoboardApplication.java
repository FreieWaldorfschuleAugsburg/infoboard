package de.waldorfaugsburg.infoboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.waldorfaugsburg.infoboard.config.InfoboardConfiguration;
import de.waldorfaugsburg.infoboard.config.JsonAdapter;
import de.waldorfaugsburg.infoboard.config.action.AbstractButtonAction;
import de.waldorfaugsburg.infoboard.config.icon.AbstractStreamDeckIcon;
import de.waldorfaugsburg.infoboard.menu.MenuRegistry;
import de.waldorfaugsburg.infoboard.streamdeck.StreamDeck;
import de.waldorfaugsburg.infoboard.window.InfoboardFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class InfoboardApplication {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(AbstractButtonAction.class, new JsonAdapter<AbstractButtonAction>()).registerTypeAdapter(AbstractStreamDeckIcon.class, new JsonAdapter<AbstractStreamDeckIcon>()).setPrettyPrinting().create();

    private InfoboardConfiguration configuration;
    private InfoboardFrame frame;
    private StreamDeck streamDeck;

    private MenuRegistry menuRegistry;

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

        menuRegistry = new MenuRegistry(this);

        // Create infoboard window
        frame = new InfoboardFrame(this, configuration.isProduction());

        // Only initialize stream deck if in production
        if (configuration.isProduction()) {
            // TODO put this back
        }

        initializeStreamDeck();

        // Open main menu
        menuRegistry.changeMenu(configuration.getMainMenu());
    }

    private void initializeStreamDeck() {
        streamDeck = new StreamDeck(configuration.getStreamDeckSerial());
        streamDeck.addListener((key, pressed) -> frame.handleButtonAction(key, pressed));
    }

    public void shutdown() {
        streamDeck.close();
    }

    public InfoboardConfiguration getConfiguration() {
        return configuration;
    }

    public void saveConfiguration() {
        saveConfiguration(new File("config.json"));
    }

    private InfoboardConfiguration readConfiguration() throws IOException {
        final InfoboardConfiguration config;
        try (final FileReader reader = new FileReader("config.json")) {
            config = gson.fromJson(reader, InfoboardConfiguration.class);
        }
        return config;
    }

    public void saveConfiguration(final File path) {
        try (final FileWriter writer = new FileWriter(path)) {
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

    public MenuRegistry getMenuRegistry() {
        return menuRegistry;
    }
}