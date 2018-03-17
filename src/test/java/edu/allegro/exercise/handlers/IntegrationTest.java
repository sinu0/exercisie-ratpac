package edu.allegro.exercise.handlers;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.http.TestHttpClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class IntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(IntegrationTest.class);
    private static final String GITHUB_OWNER = "sinu0";
    private static final String GIHTUB_REPO = "SiSPD";
    private static final String GIHTUB_REPO_NOK = "SiSPDs";
    private static final String GITHUB_REPO_FORBIDDEN = "Forbidden";
    private final EmbeddedApp app;

    @Rule
    public WireMockRule rule = new WireMockRule(wireMockConfig().port(1111).notifier(new Slf4jNotifier(true)));

    public IntegrationTest() throws Exception {
        rule.start();
        GetUserRepoInfoService getUserRepoInfoService = GetUserRepoInfoTest.userRepoTest();
        app = EmbeddedApp.fromServer(RatpackServer.of(ratpackServerSpec ->
                ratpackServerSpec
                        .serverConfig(ServerConfigBuilder::build)
                        .handlers(getUserRepoInfoService.create())));
    }


    @After
    public void endTests() throws Exception {
        rule.stop();
        app.getServer().stop();
    }

    @Test
    public void shouldReturnForbiddenTest() throws Exception {
        app.test(this::githubShouldReturnForbiddenAppShouldReturn403);
    }


    @Test
    public void shouldReturnOKInfoAboutOwnerRepoTest() throws Exception {
        app.test(this::shouldReturnOKInfoAboutOwnerRepo);
    }

    @Test
    public void forNotExistingRepoShouldReturn404Test() throws Exception {
        app.test(this::forNotExistingReposhouldReturn404);
    }

    @Test
    public void WhenGithubShutdownShouldReturn500OnGithubUnavailabilityTest() throws Exception {
        rule.stop();
        app.test(this::forNotExistingReposhouldReturn404);
    }

    private void githubShouldReturnForbiddenAppShouldReturn403(TestHttpClient testHttpClient) {
        ReceivedResponse receivedResponse = testHttpClient.get(String.format("/repositories/%s/%s", GITHUB_OWNER, GITHUB_REPO_FORBIDDEN));
        Assert.assertTrue("Response should be nok for owner: " + GITHUB_OWNER + " and for repo: " + GITHUB_REPO_FORBIDDEN, receivedResponse.getStatusCode() == HttpResponseStatus.FORBIDDEN.code());
    }

    private void shouldReturnOKInfoAboutOwnerRepo(TestHttpClient testHttpClient) {
        ReceivedResponse receivedResponse = testHttpClient.get(String.format("/repositories/%s/%s", GITHUB_OWNER, GIHTUB_REPO));
        logResponse(receivedResponse);
        Assert.assertTrue("Response should be ok for owner: " + GITHUB_OWNER + " and for repo: " + GIHTUB_REPO, receivedResponse.getStatusCode() == HttpResponseStatus.OK.code());
    }

    private void forNotExistingReposhouldReturn404(TestHttpClient testHttpClient) {
        ReceivedResponse receivedResponse = testHttpClient.get(String.format("/repositories/%s/%s", GITHUB_OWNER, GIHTUB_REPO_NOK));
        Assert.assertTrue("Response should be nok for owner: " + GITHUB_OWNER + " and for repo: " + GIHTUB_REPO_NOK, receivedResponse.getStatusCode() != HttpResponseStatus.OK.code());
    }

    private void logResponse(ReceivedResponse response) {
        log.info(response.getHeaders().asMultiValueMap().toString());
        log.info(response.getStatus().toString());
        log.info(response.getBody().getText());
    }

}
