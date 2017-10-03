package com.example.chatchatapplication.Object_json;

import java.util.List;

/**
 * Created by Neramit777 on 10/3/2017 at 4:49 PM.
 */

public class groupListRetrieve {
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

    public List<Group> getData() {
        return data;
    }

    public void setData(List<Group> data) {
        this.data = data;
    }

    private int status;
    private String message;
    private List<Group> data;
}
