package edu.allegro.exercise.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.error.ClientErrorHandler;
import ratpack.error.ServerErrorHandler;
import ratpack.error.internal.DefaultProductionErrorHandler;
import ratpack.http.client.ReceivedResponse;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.http.TestHttpClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


public class IntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(IntegrationTest.class);
    private static final String GITHUB_OWNER = "sinu0";
    private static final String GIHTUB_REPO = "SiSPD";
    private static final String GIHTUB_REPO_NOT_FOUND = "SiSPDs";
    private static final String GITHUB_REPO_FORBIDDEN = "Forbidden";
    private static final String GITHUB_REPO_TIMEOUT = "timeout";
    private final EmbeddedApp app;

    @Rule
    public WireMockRule rule = new WireMockRule(wireMockConfig().port(1111).notifier(new Slf4jNotifier(true)));

    public IntegrationTest() throws Exception {
        rule.start();
        GetUserRepoInfoService getUserRepoInfoService = HandlerUtilTest.userRepoTest();
        app = EmbeddedApp.fromServer(RatpackServer.of(ratpackServerSpec ->
                ratpackServerSpec
                        .serverConfig(conf -> conf
                                .threads(1)
                        )
                        .registry(Registry.builder()
                                .add(ClientErrorHandler.class, new DefaultProductionErrorHandler())
                                .add(ServerErrorHandler.class, new ErrorHandler())
                                .with(registrySpec ->
                                        registrySpec.add(new ObjectMapper().registerModule(new Jdk8Module())))
                                .build())
                        .handlers(getUserRepoInfoService.create())));
    }


    @After
    public void endTests() throws Exception {
        rule.stop();
        app.getServer().stop();
    }

    @Test
    public void shouldReturnForbiddenTest() throws Exception {
        app.test(httpClient -> {
            ReceivedResponse receivedResponse = whenClientCall(httpClient, GITHUB_REPO_FORBIDDEN);

            assertTrue("Response should be FORBIDDEN for owner: " + GITHUB_OWNER + " and for repo: " + GITHUB_REPO_FORBIDDEN, receivedResponse.getStatusCode() == HttpResponseStatus.FORBIDDEN.code());
            assertTrue("And description should not be empty", StringUtils.isNotEmpty(receivedResponse.getBody().getText()));

        });
    }

    @Test
    public void shouldReturnOKInfoAboutOwnerRepoTest() throws Exception {
        app.test(httpClient -> {
            String jsonExpected = HandlerUtilTest.getFileFromResourceAsString("jsonExpected.json");
            ReceivedResponse receivedResponse = whenClientCall(httpClient, GIHTUB_REPO);
            String response = receivedResponse.getBody().getText();
            logResponse(receivedResponse);

            assertTrue("Response should be OK for owner: " + GITHUB_OWNER + " and for repo: " + GIHTUB_REPO, receivedResponse.getStatusCode() == HttpResponseStatus.OK.code());
            assertTrue("Description should not be empty", StringUtils.isNotEmpty(receivedResponse.getBody().getText()));
            assertEquals(jsonExpected, response, true);
        });
    }

    @Test
    public void forNotExistingRepoShouldReturn404Test() throws Exception {
        app.test(httpClient -> {
            ReceivedResponse receivedResponse = whenClientCall(httpClient, GIHTUB_REPO_NOT_FOUND);

            assertTrue("Response should be NOT_FOUND for owner: " + GITHUB_OWNER + " and for repo: " + GIHTUB_REPO_NOT_FOUND, receivedResponse.getStatusCode() == HttpResponseStatus.NOT_FOUND.code());
            assertTrue("Description should not be empty", StringUtils.isNotEmpty(receivedResponse.getBody().getText()));
        });
    }

    @Test
    public void whenGithubShutdownShouldReturn503OnGithubUnavailabilityTest() throws Exception {
        rule.stop();
        app.test(httpClient -> {
            ReceivedResponse receivedResponse = whenClientCall(httpClient, GIHTUB_REPO);

            assertTrue("Response should be  for owner: " + GITHUB_OWNER + " and for repo: " + GIHTUB_REPO, receivedResponse.getStatusCode() == HttpResponseStatus.SERVICE_UNAVAILABLE.code());
            assertTrue("Description should not be empty", StringUtils.isNotEmpty(receivedResponse.getBody().getText()));
        });
    }

    @Test
    public void whenGithubIsVerySlowShouldReturnTimeout() throws Exception {
        app.test(httpClient -> {
            ReceivedResponse receivedResponse = whenClientCall(httpClient, GITHUB_REPO_TIMEOUT);

            assertTrue("Response should be 504 for owner: " + GITHUB_OWNER + " and for repo: " + GITHUB_REPO_TIMEOUT, receivedResponse.getStatusCode() == HttpResponseStatus.GATEWAY_TIMEOUT.code());
            assertTrue("Description should not be empty", StringUtils.isNotEmpty(receivedResponse.getBody().getText()));
        });
    }

    private ReceivedResponse whenClientCall(TestHttpClient httpClient, String gihtubRepo) {
        return httpClient.get(String.format("/repositories/%s/%s", GITHUB_OWNER, gihtubRepo));
    }

    private void logResponse(ReceivedResponse response) {
        log.info(response.getHeaders().asMultiValueMap().toString());
        log.info(response.getStatus().toString());
        log.info(response.getBody().getText());
    }

}
