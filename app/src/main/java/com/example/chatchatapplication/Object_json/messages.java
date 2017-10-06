package com.example.chatchatapplication.Object_json;

import java.util.Date;

/**
 * Created by Neramit777 on 9/14/2017.
 */

public class messages {
    private String messageText;
    private String messageUser;
    private String messageDisplayName;
    private String messagePictureURL;
    private long messageTime;

    public String getMessagePictureURL() {
        return messagePictureURL;
    }

    public void setMessagePictureURL(String messagePictureURL) {
        this.messagePictureURL = messagePictureURL;
    }

    public String getMessageDisplayName() {
        return messageDisplayName;
    }

    public void setMessageDisplayName(String messageDisplayName) {
        this.messageDisplayName = messageDisplayName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public messages(){}

    public messages(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        // Initialize to current time
        this.messageTime = new Date().getTime();
    }

    public messages(String messageText, String messageUser, String messageDisplayName, String messagePictureURL) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageDisplayName = messageDisplayName;
        this.messagePictureURL = messagePictureURL;
        // Initialize to current time
        this.messageTime = new Date().getTime();
    }
}
