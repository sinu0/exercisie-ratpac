package edu.allegro.exercise.handlers;

import java.util.HashMap;
import java.util.Map;

public class GetUserRepoInfoTest {

    private static final Map<String, Object> CONFIGURATION = new HashMap<String, Object>() {{
        put("githubResourceV3", "https://api.github.com/repos/%s/%s");
        put("githubUserAgent", "Allegro-exercise");
        put("githubAcept", "application/vnd.github.v3+json");
    }};


    public static GetUserRepoInfoService userRepotTest() throws Exception {
        return new GetUserRepoInfoService(CONFIGURATION);

    }

}