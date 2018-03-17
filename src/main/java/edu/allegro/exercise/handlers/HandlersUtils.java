package edu.allegro.exercise.handlers;

import edu.allegro.exercise.ResultCodes;
import edu.allegro.exercise.model.github.ErrorResponse;
import org.slf4j.Logger;
import ratpack.handling.Context;
import ratpack.http.client.ReceivedResponse;

import static ratpack.jackson.Jackson.json;

class HandlersUtils {
    static void handleError(Context ctx, int code) {
        ErrorResponse errorResponse = ResultCodes.fromCode(code);
        ctx.getResponse().status(errorResponse.getStatusCode());
        ctx.render(json(errorResponse));
    }

    static void logErrorResponse(ReceivedResponse response, Logger log) {
        log.info(response.getHeaders().asMultiValueMap().toString());
        log.info(response.getStatus().toString());
        log.info(response.getBody().getText());
    }
}
