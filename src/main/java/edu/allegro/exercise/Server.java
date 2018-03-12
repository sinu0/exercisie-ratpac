package edu.allegro.exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import edu.allegro.exercise.handlers.GetUserRepoInfoService;
import ratpack.error.ClientErrorHandler;
import ratpack.error.ServerErrorHandler;
import ratpack.error.internal.DefaultProductionErrorHandler;
import ratpack.handling.RequestLogger;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

public class Server {
    static void start(ServerConfig config, GetUserRepoInfoService userRepoInfo) throws Exception {
        RatpackServer.start(
                ratpackServerSpec -> ratpackServerSpec
                        .serverConfig(config)
                        .registry(Registry.builder()
                                .add(ClientErrorHandler.class, new DefaultProductionErrorHandler())
                                .add(ServerErrorHandler.class, new DefaultProductionErrorHandler())
                                .with(registrySpec ->
                                        registrySpec.add(new ObjectMapper().registerModule(new Jdk8Module())))
                                .build())
                        .handlers(userRepoInfo.create().append(
                                chain ->
                                        chain.all((RequestLogger.ncsa()))
                                )
                        )
        );
    }

}
