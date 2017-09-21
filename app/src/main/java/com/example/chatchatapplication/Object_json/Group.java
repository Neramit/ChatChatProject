package com.example.chatchatapplication.Object_json;

import java.util.List;

/**
 * Created by Neramit777 on 9/21/2017 at 1:03 PM.
 */

public class Group {

    private String groupName,groupOwner;
    private String groupImageURL;
    private String groupPassword;
    private List<Member> groupMember;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getGroupImageURL() {
        return groupImageURL;
    }

    public void setGroupImageURL(String groupImageURL) {
        this.groupImageURL = groupImageURL;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public List<Member> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(List<Member> groupMember) {
        this.groupMember = groupMember;
    }



}
