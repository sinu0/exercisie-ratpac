package edu.allegro.exercise;

import edu.allegro.exercise.model.github.ErrorResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class ResultCodesTest {

    @Test
    public void errorResponseShouldBe404Test() {
        ErrorResponse errorResponse = ResultCodes.fromCode(404);
        assertTrue("Github error server 404 should be mapped on 404 error response", errorResponse.getStatusCode() == 404);
    }


    @Test
    public void errorResponseShouldBe400Test() {
        ErrorResponse errorResponse = ResultCodes.fromCode(403);
        assertTrue("Github error server 403 should be mapped on 400 error response", errorResponse.getStatusCode() == 403);
    }


    @Test
    public void errorResponseShouldBe500Test() {
        ErrorResponse errorResponse = ResultCodes.fromCode(500);
        assertTrue("Github error server 500 should be mapped on 500 error response", errorResponse.getStatusCode() == 500);
    }

    @Test
    public void anyErrorDiffrentThanAboveShouldBePresentedAs500() {
        Set<Integer> notUsed = new HashSet<>(Arrays.asList(400, 403, 404));
        for (int i = 100; i < 511; i++) {
            if (!notUsed.contains(i)) {
                ErrorResponse errorResponse = ResultCodes.fromCode(i);
                assertTrue("Github error server 404 should be mapped on 404 error response", errorResponse.getStatusCode() == 500);
            }
        }
    }
}