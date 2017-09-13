package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 9/6/2017.
 */

public class searchRetrieve {
    private int status;
    private String message;
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
