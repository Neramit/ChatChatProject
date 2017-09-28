package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 9/28/2017 at 8:27 AM.
 */

public class inviteSend2 {
    private String module;
    private String target;
    private String token;
    private inviteSend data;

    public inviteSend2(String module,String target,inviteSend data){
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public inviteSend getData() {
        return data;
    }

    public void setData(inviteSend data) {
        this.data = data;
    }
}
