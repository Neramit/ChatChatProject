package com.example.chatchatapplication.Object_json;

import java.util.List;

/**
 * Created by Neramit777 on 9/21/2017 at 1:03 PM.
 */

public class Group2 {

    private String groupName,groupOwner;
    private String groupImageURL;
    private String groupPassword;
    private List<Friend> groupMember;
    private int groupMemberNum;
    private int groupUID;
    private int groupStatus;

    public int getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
    }

    public int getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(int groupUID) {
        this.groupUID = groupUID;
    }

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

    public List<Friend> getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(List<Friend> groupMember) {
        this.groupMember = groupMember;
    }

    public int getGroupMemberNum() {
        return groupMemberNum;
    }

    public void setGroupMemberNum(int groupMemberNum) {
        this.groupMemberNum = groupMemberNum;
    }
}
