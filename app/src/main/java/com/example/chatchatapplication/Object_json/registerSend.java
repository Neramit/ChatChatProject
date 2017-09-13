package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 8/27/2017.
 */

public class registerSend {
    private String module;
    private String target;
    private String token;
    private User data;
    //    private String data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public registerSend(){}

    public registerSend(String module,String target,User data){
        this.module = module;
        this.target = target;
        this.data = data;
    }

    public registerSend(String module,String target,String token,User data){
        this.module = module;
        this.target = target;
        this.token = token;
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

    public User getUser() {
        return data;
    }

    public void setUser(User data) {
        this.data = data;
    }
}
