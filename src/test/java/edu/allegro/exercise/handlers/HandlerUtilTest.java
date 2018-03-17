package edu.allegro.exercise.handlers;

import org.apache.logging.log4j.core.util.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HandlerUtilTest {

    private static final Map<String, Object> CONFIGURATION = new HashMap<String, Object>() {{
        put("githubResourceV3", "http://localhost:1111");
        put("githubUserAgent", "Allegro-exercise");
        put("githubAccept", "application/vnd.github.v3+json");
        put("githubUserBasic", "YWxsZWdyb0V4ZXJjaXNlOkNpSHVuQmFIaWR1andpNg==");
        put("clientPoolSize", 10);
        put("clientTimeout", 1);
    }};


    public static GetUserRepoInfoService userRepoTest() throws Exception {
        RepositoryInfo info = new RepositoryInfo(CONFIGURATION);
        return new GetUserRepoInfoService(info);

    }

    public static String getFileFromResourceAsString(String name) throws IOException {
        InputStreamReader reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(name));
        return IOUtils.toString(reader);
    }

}