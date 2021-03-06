package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 10/2/2017 at 3:06 PM.
 */

public class GroupSend {
    private String module;
    private String target;
    private String token;
    private Group2 data;

    public GroupSend (String module,String target,String token,Group2 data){
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Group2 getData() {
        return data;
    }

    public void setData(Group2 data) {
        this.data = data;
    }
}
