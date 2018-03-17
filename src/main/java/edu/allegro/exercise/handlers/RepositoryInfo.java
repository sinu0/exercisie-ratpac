package edu.allegro.exercise.handlers;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.util.internal.StringUtil;
import ratpack.exec.Promise;
import ratpack.http.Headers;
import ratpack.http.client.HttpClient;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.http.internal.NettyHeadersBackedHeaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;

import static edu.allegro.exercise.Constant.*;

public class RepositoryInfo {

    private static final String GITHUB_USER_BASIC = "githubUserBasic";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";
    private static final String REPOS = "/repos/";
    private final HttpClient client;
    private final String resourceLink;
    private final Headers headers;

    public RepositoryInfo(Map<String, Object> configuration) throws Exception {
        Integer poolSize = (Integer) configuration.get(CLIENT_POOL_SIZE.getName());
        Integer timeout = (Integer) configuration.get(CLIENT_TIMEOUT.getName());
        this.client = initHttpClient(poolSize, timeout);
        this.resourceLink = (String) configuration.get(GITHUB_REPOSITORY_OWNER_INFO.getName());
        this.headers = setHeaders(configuration);

    }

    private HttpClient initHttpClient(int poolSize, int timeout) throws Exception {
        return HttpClient.of(httpClientSpec ->
                httpClientSpec
                        .poolSize(poolSize)
                        .connectTimeout(Duration.ofSeconds(timeout))
                        .readTimeout(Duration.ofSeconds(timeout))
        );

    }

    public Promise<ReceivedResponse> getInfo(String owner, String repo) throws URISyntaxException {
        String url = resourceLink + REPOS + owner + "/" + repo;

        return client.get(new URI(url),
                this::setHeaders);
    }

    private void setHeaders(RequestSpec requestSpec) {
        requestSpec.getHeaders().copy(headers);
    }


    private Headers setHeaders(Map<String, Object> configuration) {
        DefaultHttpHeaders defaultHttpHeaders = new DefaultHttpHeaders();
        Headers headers = new NettyHeadersBackedHeaders(defaultHttpHeaders);
        defaultHttpHeaders.set(ACCEPT.getName(), configuration.get(GITHUB_ACCEPT_HEADER.getName()));
        defaultHttpHeaders.set(USER_AGENT.getName(), configuration.get(GITHUB_USER_AGENT_HEADER.getName()));
        setAuthorizationHeader(configuration, defaultHttpHeaders);
        return headers;
    }

    private void setAuthorizationHeader(Map<String, Object> configuration, DefaultHttpHeaders defaultHttpHeaders) {
        String pass = (String) configuration.get(GITHUB_USER_BASIC);
        if (!StringUtil.isNullOrEmpty(pass)) {
            defaultHttpHeaders.set(AUTHORIZATION, BASIC + pass);
        }
    }
}
