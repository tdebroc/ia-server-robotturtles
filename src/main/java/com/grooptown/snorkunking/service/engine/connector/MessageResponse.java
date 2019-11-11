package com.grooptown.snorkunking.service.engine.connector;

/**
 * Created by thibautdebroca on 03/04/16.
 */
public class MessageResponse {
    private String error;
    private String message;
    public MessageResponse() {}
    public MessageResponse(String error, String message) {
        this.setError(error);
        this.setMessage(message);}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
