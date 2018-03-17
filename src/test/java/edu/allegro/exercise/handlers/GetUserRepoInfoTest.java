package edu.allegro.exercise.handlers;

import java.util.HashMap;
import java.util.Map;

public class GetUserRepoInfoTest {

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

}