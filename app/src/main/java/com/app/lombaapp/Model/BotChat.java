package com.app.lombaapp.Model;

public class BotChat {

    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    String message, sentBy;

    public BotChat(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
