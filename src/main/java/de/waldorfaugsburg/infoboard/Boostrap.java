package de.waldorfaugsburg.infoboard;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Boostrap {

    public static void main(final String[] args) {
        final InfoboardApplication application = new InfoboardApplication();

        // Shutdown hook for doing important things on CTRL + C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                application.shutdown();
            } catch (final Exception e) {
                log.error("Unhandled exception occurred on shutdown", e);
            }
        }));

        // Run application
        try {
            application.startup();
        } catch (final Exception e) {
            log.error("Unhandled exception occurred on startup", e);
        }
    }

}
