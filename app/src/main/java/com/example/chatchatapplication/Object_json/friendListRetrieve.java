package com.example.chatchatapplication.Object_json;

import java.util.ArrayList;

/**
 * Created by Neramit777 on 9/14/2017.
 */

public class friendListRetrieve {
    private int status;
    private String message;
    private ArrayList<Friend> data;

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

    public ArrayList<Friend> getData() {
        return data;
    }

    public void setData(ArrayList<Friend> data) {
        this.data = data;
    }


}
