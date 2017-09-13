package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 8/30/2017.
 */

public class enterEmailSend {
    private String module;
    private String target;
    private String data;

    public enterEmailSend() {
    }

    public enterEmailSend(String module, String target, String data) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
