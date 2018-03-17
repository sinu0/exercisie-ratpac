package edu.allegro.exercise.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.error.ServerErrorHandler;
import ratpack.handling.Context;
import ratpack.http.client.HttpClientReadTimeoutException;
import ratpack.path.InvalidPathEncodingException;

import static edu.allegro.exercise.handlers.HandlersUtils.handleError;
import static io.netty.handler.codec.http.HttpResponseStatus.GATEWAY_TIMEOUT;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

public class ErrorHandler implements ServerErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void error(Context context, Throwable throwable) throws Exception {
        if (throwable instanceof HttpClientReadTimeoutException) {
            handleError(context, GATEWAY_TIMEOUT.code());
        }
        handleError(context, INTERNAL_SERVER_ERROR.code());
        log.error("Internal error occur: ", throwable);
    }

    @Override
    public void error(Context context, InvalidPathEncodingException exception) throws Exception {
        handleError(context, INTERNAL_SERVER_ERROR.code());
        log.error("Internal error occur: ", exception);
    }
}
