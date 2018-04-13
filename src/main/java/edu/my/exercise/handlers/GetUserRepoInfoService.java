package edu.my.exercise.handlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.my.exercise.model.github.UserRepoInfo;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.handling.Chain;
import ratpack.handling.Context;
import ratpack.http.client.ReceivedResponse;
import ratpack.jackson.Jackson;
import ratpack.jackson.JsonRender;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static edu.my.exercise.handlers.HandlersUtils.handleError;
import static edu.my.exercise.handlers.HandlersUtils.logErrorResponse;

public class GetUserRepoInfoService {
    private static final Logger log = LoggerFactory.getLogger(GetUserRepoInfoService.class);
    private static final String OWNER = "owner";
    private static final String REPO = "repo";
    private static final String PATH = ":" + OWNER + "/:" + REPO;
    private static final String REPOSITORIES = "repositories";
    private final RepositoryInfo repositoryInfo;
    private final ObjectMapper mapper;

    public GetUserRepoInfoService(RepositoryInfo repositoryInfo) {
        this.repositoryInfo = repositoryInfo;
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Action<Chain> create() {
        return chain ->
                chain.prefix(REPOSITORIES, action ->
                        action.get(PATH, this::handle));
    }

    private void handle(Context ctx) throws URISyntaxException {
        Promise<JsonRender> response = repositoryInfo.getInfo(getToken(ctx, OWNER), getToken(ctx, REPO))
                .mapIf(this::ifSuccess, handleOK(), handleNOK(ctx))
                .map(responseOp -> responseOp.map(this::getUserRepoInfo))
                .map(Jackson::json);
        ctx.render(response);
    }

    private boolean ifSuccess(ReceivedResponse response) {
        return response.getStatusCode() < HttpResponseStatus.BAD_REQUEST.code();
    }

    private Function<ReceivedResponse, Optional<ReceivedResponse>> handleOK() {
        return Optional::of;
    }

    private Function<ReceivedResponse, Optional<ReceivedResponse>> handleNOK(Context ctx) {
        return response -> {
            logErrorResponse(response, log);
            handleError(ctx, response.getStatusCode());
            return Optional.empty();
        };
    }

    private String getToken(Context ctx, String token) {
        return ctx.getPathTokens().get(token);
    }

    private Optional<UserRepoInfo> getUserRepoInfo(ReceivedResponse res) {
        try {
            return Optional.of(mapper.readValue(res.getBody().getText(), UserRepoInfo.class));
        } catch (IOException e) {
            return Optional.empty();
        }

    }
}
