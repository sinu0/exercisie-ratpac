package edu.allegro.exercise;

import edu.allegro.exercise.model.github.ErrorResponse;

public enum ResultCodes {
    BAD_REQUEST(400, 500, "Github client: Bad request"),
    NOT_FOUND(404, 404, "Github client:  Given repository or owner doesn't exist"),
    FORBIDDEN(403, 403, "Github client: API rate limit exceeded");


    private final int statusCode;
    private final int internalCode;
    private final String statusDescription;

    ResultCodes(int statusCode, int internalCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.internalCode = internalCode;
    }

    public static ErrorResponse fromCode(int statusCode) {
        for (ResultCodes resultCodes : ResultCodes.values()) {
            if (resultCodes.statusCode == statusCode) {
                return new ErrorResponse(resultCodes.internalCode, resultCodes.statusDescription);
            }
        }
        return new ErrorResponse(500, "Server internal error");
    }
}
