package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 8/31/2017.
 */

public class checkEmailSend {
    private String module;
    private String target;
    private User data;

    public checkEmailSend() {
    }

    public checkEmailSend(String module, String target, User data) {
        this.module = module;
        this.target = target;
        this.data = data;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
