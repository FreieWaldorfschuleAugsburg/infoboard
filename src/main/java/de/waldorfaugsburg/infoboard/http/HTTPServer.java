package de.waldorfaugsburg.infoboard.http;

import de.waldorfaugsburg.infoboard.InfoboardApplication;
import io.javalin.Javalin;
import io.javalin.json.JavalinGson;

public class HTTPServer {

    private final InfoboardApplication application;
    private final Javalin server;

    public HTTPServer(final InfoboardApplication application) {
        this.application = application;
        this.server = Javalin.create(config -> config.jsonMapper(new JavalinGson(application.getGson()))).start(application.getConfiguration().getHttpPort());
        registerRoutes();
    }

    private void registerRoutes() {
        server.get("/", context -> application.reload());
    }
}
