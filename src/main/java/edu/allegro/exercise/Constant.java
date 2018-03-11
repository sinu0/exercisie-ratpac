package edu.allegro.exercise;

public enum Constant {
    APP_CONFIG("/app"),
    GITHUB_USER_AGENT_HEADER("githubUserAgent"),
    GITHUB_ACCEPT_HEADER("githubAcept"),
    GITHUB_REPOSITORY_OWNER_INFO("githubResourceV3"),
    ACCEPT("Accept"),
    USER_AGENT("User-Agent"),
    CONFIG_YAML("config.yaml"),
    GITHUB_USER("githubUser"),
    GITHUB_PASSWORD("githubPassword"), CLIENT_POOL_SIZE("clientPoolSize"), CLIENT_TIMEOUT("clientTimeout");

    private final String prop;

    Constant(String prop) {
        this.prop = prop;
    }

    public String getName() {
        return prop;
    }

    @Override
    public String toString() {
        return prop;
    }
}
