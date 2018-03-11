package edu.allegro.exercise.handlers;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.*;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.http.TestHttpClient;

public class IntegrationTest {

    private static final String GITHUB_OWNER = "sinu0";
    private static final String GIHTUB_REPO = "SiSPD";
    private static final String GIHTUB_REPO_NOK = "SiSPDs";
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
    private EmbeddedApp app;

    @Before
    public void setUp() throws Exception {
        GetUserRepoInfoService getUserRepoInfoService = GetUserRepoInfoTest.userRepotTest();
        app = EmbeddedApp.fromServer(RatpackServer.of(ratpackServerSpec ->
                ratpackServerSpec
                        .serverConfig(ServerConfigBuilder::build)
                        .handlers(getUserRepoInfoService.create())));

    }

    @After
    public void endTests() throws Exception {
        app.getServer().stop();
        app = null;
    }

    @Test
    public void shouldReturnOKInfoAboutOwnerRepo() throws Exception {
        app.test(this::shouldReturnOKInfoAboutOwnerRepo);
    }

    @Test
    public void shouldReturnNOKInfoAboutOwnerRepo() throws Exception {
        app.test(this::shouldReturnNOKInfoAboutOwnerRepo);
    }

    private void shouldReturnOKInfoAboutOwnerRepo(TestHttpClient testHttpClient) {
        ReceivedResponse receivedResponse = testHttpClient.get(String.format("/repositories/%s/%s", GITHUB_OWNER, GIHTUB_REPO));
        responseShouldBeValid(receivedResponse);
    }

    private void shouldReturnNOKInfoAboutOwnerRepo(TestHttpClient testHttpClient) {
        ReceivedResponse receivedResponse = testHttpClient.get(String.format("/repositories/%s/%s", GITHUB_OWNER, GIHTUB_REPO_NOK));
        responseShouldBeNotValid(receivedResponse);
    }

    private void responseShouldBeValid(ReceivedResponse receivedResponse) {
        System.out.println(receivedResponse.getHeaders().asMultiValueMap().toString());
        System.out.println(receivedResponse.getBody().getText());
        Assert.assertTrue("Response should be ok for owner:" + GITHUB_OWNER + " and for repo: " + GIHTUB_REPO, receivedResponse.getStatusCode() == HttpResponseStatus.OK.code());
    }

    private void responseShouldBeNotValid(ReceivedResponse receivedResponse) {
        System.out.println(receivedResponse.getHeaders().asMultiValueMap().toString());
        System.out.println(receivedResponse.getBody().getText());
        Assert.assertTrue("Response should be nok for owner:" + GITHUB_OWNER + " and for repo: " + GIHTUB_REPO_NOK, receivedResponse.getStatusCode() != HttpResponseStatus.OK.code());
    }

    private void performanceTest() {

    }
}
