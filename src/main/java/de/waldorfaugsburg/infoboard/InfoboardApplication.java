package de.waldorfaugsburg.infoboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.waldorfaugsburg.infoboard.config.InfoboardConfiguration;
import de.waldorfaugsburg.infoboard.config.JsonAdapter;
import de.waldorfaugsburg.infoboard.config.action.AbstractButtonAction;
import de.waldorfaugsburg.infoboard.config.icon.AbstractStreamDeckIcon;
import de.waldorfaugsburg.infoboard.http.HTTPServer;
import de.waldorfaugsburg.infoboard.menu.MenuRenderer;
import de.waldorfaugsburg.infoboard.streamdeck.StreamDeck;
import de.waldorfaugsburg.infoboard.window.InfoboardFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class InfoboardApplication {

    private static final String CONFIGURATION_PATH = "config.json";

    private final Gson gson = new GsonBuilder().registerTypeAdapter(AbstractButtonAction.class, new JsonAdapter<AbstractButtonAction>()).registerTypeAdapter(AbstractStreamDeckIcon.class, new JsonAdapter<AbstractStreamDeckIcon>()).setPrettyPrinting().create();

    private boolean production;
    private InfoboardConfiguration configuration;
    private InfoboardFrame frame;
    private StreamDeck streamDeck;
    private MenuRenderer menuRenderer;

    private String lastFileChooserPath;

    public void startup(final String[] args) {
        production = args.length == 1 && args[0].equals("prod");

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
        new HTTPServer(this);

        // Create infoboard window
        frame = new InfoboardFrame(this, production);

        // Only initialize stream deck if in production
        if (production) {
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

    public void reload() {
        try {
            configuration = readConfiguration();
        } catch (final IOException e) {
            log.error("Error loading configuration", e);
        }

        menuRenderer.changeMenu(configuration.getMainMenu());
    }

    public void reloadHttpTarget() {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(configuration.getHttpTarget())).build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (final IOException | InterruptedException e) {
            log.error("Error while requesting target url {}", configuration.getHttpTarget(), e);
        }
    }

    public InfoboardConfiguration getConfiguration() {
        return configuration;
    }

    public void saveConfiguration() {
        saveConfiguration(new File(CONFIGURATION_PATH));
    }

    private InfoboardConfiguration readConfiguration() throws IOException {
        final InfoboardConfiguration config;
        try (final FileReader reader = new FileReader(CONFIGURATION_PATH, StandardCharsets.UTF_8)) {
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
            final String configurationContent = Files.readString(Path.of(CONFIGURATION_PATH));
            final String newConfigurationContent = gson.toJson(configuration);

            return !configurationContent.equals(newConfigurationContent);
        } catch (final IOException ignored) {
        }

        return false;
    }

    public String getLastFileChooserPath() {
        return lastFileChooserPath != null ? lastFileChooserPath : System.getProperty("user.home");
    }

    public void setLastFileChooserPath(final String lastFileChooserPath) {
        this.lastFileChooserPath = lastFileChooserPath;
    }

    public boolean isProduction() {
        return production;
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