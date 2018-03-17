package edu.allegro.exercise;

import edu.allegro.exercise.model.github.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class ResultCodesTest {

    @Test
    public void errorResponseShouldBe404Test() {
        ErrorResponse errorResponse = ResultCodes.fromCode(404);
        assertTrue("Github error server 404 should be mapped on 404 error response", errorResponse.getStatusCode() == 404);
        assertTrue("And description should not be empty", StringUtils.isNotEmpty(errorResponse.getStatusDescription()));
    }


    @Test
    public void errorResponseShouldBe400Test() {
        ErrorResponse errorResponse = ResultCodes.fromCode(403);
        assertTrue("Github error server 403 should be mapped on 400 error response", errorResponse.getStatusCode() == 403);
        assertTrue("And description should not be empty", StringUtils.isNotEmpty(errorResponse.getStatusDescription()));
    }


    @Test
    public void errorResponseShouldBe500Test() {
        ErrorResponse errorResponse = ResultCodes.fromCode(500);
        assertTrue("Github error server 500 should be mapped on 500 error response", errorResponse.getStatusCode() == 500);
        assertTrue("And description should not be empty", StringUtils.isNotEmpty(errorResponse.getStatusDescription()));
    }

    @Test
    public void anyErrorDifferentThanAboveShouldBePresentedAs500() {
        Set<Integer> notUsed = new HashSet<>();
        for (ResultCodes resultCodes : ResultCodes.values()) {
            notUsed.add(resultCodes.getStatusCode());
        }
        for (int i = 100; i < 511; i++) {
            if (!notUsed.contains(i)) {
                ErrorResponse errorResponse = ResultCodes.fromCode(i);
                assertTrue("For error code: " + i + " ErrorResponse should be 500", errorResponse.getStatusCode() == 500);
                assertTrue("And description should not be empty", StringUtils.isNotEmpty(errorResponse.getStatusDescription()));
            }
        }
    }
}