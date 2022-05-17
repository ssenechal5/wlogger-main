package com.actility.thingpark.wlogger;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoInitializer implements QuarkusTestResourceLifecycleManager {

    public static final String MONGO_INITDB_ROOT_USERNAME = "admin";
    public static final String MONGO_INITDB_ROOT_PASSWORD = "admin";
    private final static int MONGO_PORT = 27017;
    private Logger logger = Logger.getLogger(MongoInitializer.class.getName());
    @SuppressWarnings("rawtypes")
    private GenericContainer mongoContainer;

    @Override
    public Map<String, String> start() {
        //noinspection rawtypes
        mongoContainer = new GenericContainer("mongo:3.4").
                withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_INITDB_ROOT_USERNAME).
                withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_INITDB_ROOT_PASSWORD).
                withEnv("MONGO_INITDB_DATABASE", "wireless").
                withExposedPorts(MONGO_PORT).
                withCopyFileToContainer(MountableFile.forClasspathResource("/mongo/deviceHistorySeed.js"),
                        "/docker-entrypoint-initdb.d/deviceHistorySeed.js");
        try {
            mongoContainer.start();
        } catch (RuntimeException e) {
            // try to log container logs
            try {
                logger.log(Level.SEVERE, mongoContainer.getLogs());
            } catch (Exception ex) {
                logger.log(Level.SEVERE,"Failed to get container logs", ex);
            }
            throw e;
        }
        final String address = mongoContainer.getContainerIpAddress();
        final Integer port = mongoContainer.getFirstMappedPort();
        final Map<String, String> conf = new HashMap<>();
        conf.put("quarkus.mongodb.connection-string", String.format("mongodb://%s:%s@%s:%d",
                MONGO_INITDB_ROOT_USERNAME, MONGO_INITDB_ROOT_PASSWORD, address, port));
        return conf;
    }

    @Override
    public void stop() {
        if (mongoContainer != null) {
            mongoContainer.stop();
            mongoContainer = null;
        }
    }

}
