package edu.my.exercise;

import edu.my.exercise.handlers.GetUserRepoInfoService;
import edu.my.exercise.handlers.RepositoryInfo;
import edu.my.exercise.util.ServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.server.ServerConfig;

import java.util.Map;


public class Main {
    static final private Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        ServerConfig config = ServerConfig
                .builder()
                .yaml(ServerUtil.getConfigurationFile())
                .onError(throwable -> log.error("Cannot load config file", throwable))
                .build();

        Map<String, Object> configuration = ServerUtil.getConfiguration(config);
        RepositoryInfo info = new RepositoryInfo(configuration);
        GetUserRepoInfoService getUserRepoInfoService = new GetUserRepoInfoService(info);
        Server.start(config, getUserRepoInfoService);
    }
}
