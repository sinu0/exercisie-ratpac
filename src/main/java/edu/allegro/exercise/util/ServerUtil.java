package edu.allegro.exercise.util;

import ratpack.server.ServerConfig;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static edu.allegro.exercise.Constant.APP_CONFIG;
import static edu.allegro.exercise.Constant.CONFIG_YAML;

public class ServerUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getConfiguration(ServerConfig config) {
        return config.get(APP_CONFIG.getName(), Map.class);
    }

    public static URL getConfigurationFile() throws MalformedURLException {
        File file = new File(CONFIG_YAML.getName());
        if (file.exists()) {
            return file.toURI().toURL();
        } else {
            return Thread.currentThread().getContextClassLoader().getResource(CONFIG_YAML.getName());
        }
    }
}
