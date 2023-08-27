package de.waldorfaugsburg.infoboard.config;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;
import java.util.UUID;

public class ApplicationConfig extends Properties {

    private final File file;

    public ApplicationConfig(final File file) throws IOException {
        this.file = file;
        load();
    }

    public void setUUID(final String key, final UUID value) {
        setProperty(key, value.toString());
    }

    public UUID getUUID(final String key) {
        final String property = getProperty(key);
        return property == null ? null : UUID.fromString(property);
    }

    public void setString(final String key, final String value) {
        setProperty(key, value);
    }

    public String getString(final String key) {
        return getProperty(key);
    }

    public String getStringOrDefault(final String key, final String defaultValue) {
        return containsKey(key) ? getString(key) : defaultValue;
    }

    public void setBoolean(final String key, final boolean value) {
        setProperty(key, Boolean.toString(value));
    }

    public Boolean getBoolean(final String key) {
        final String property = getProperty(key);
        return property == null ? null : Boolean.parseBoolean(property);
    }

    public boolean getBooleanOrDefault(final String key, final boolean defaultValue) {
        return containsKey(key) ? getBoolean(key) : defaultValue;
    }

    public void setInt(final String key, final int value) {
        setProperty(key, Integer.toString(value));
    }

    public int getInt(final String key) {
        return Integer.parseInt(getProperty(key));
    }

    public int getIntOrDefault(final String key, final int defaultValue) {
        return containsKey(key) ? getInt(key) : defaultValue;
    }

    public void save() throws IOException {
        store(new BufferedWriter(new FileWriter(file)), "Infoboard configuration");
    }

    private void load() throws IOException {
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }

        load(new BufferedInputStream(new FileInputStream(file)));
    }
}
