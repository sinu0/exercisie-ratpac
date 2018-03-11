package edu.allegro.exercise.model.github;

public class ErrorResponse {
    private int StatusCode;
    private String StatusDescription;

    public ErrorResponse(int statusCode, String statusDescription) {
        StatusCode = statusCode;
        StatusDescription = statusDescription;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }
}
