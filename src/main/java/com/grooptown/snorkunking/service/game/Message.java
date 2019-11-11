package com.grooptown.snorkunking.service.game;

/**
 * Created by thibautdebroca on 29/11/2017.
 */
public class Message {
    private String message;
    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
