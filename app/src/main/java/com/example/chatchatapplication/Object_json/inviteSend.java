package com.example.chatchatapplication.Object_json;

import java.util.List;

/**
 * Created by Neramit777 on 9/28/2017 at 8:12 AM.
 */

public class inviteSend {
    private String groupName,groupOwner,groupImageURL,groupPassword;
    private int groupMemberNum;
    private List<Friend> inviteList;

    public inviteSend (String groupName,String groupOwner,String groupImageURL,String groupPassword,int groupMemberNum,List<Friend> inviteList){
        this.groupName = groupName;
        this.groupOwner = groupOwner;
        this.groupImageURL = groupImageURL;
        this.groupPassword = groupPassword;
        this.groupMemberNum = groupMemberNum;
        this.inviteList = inviteList;
    }

    public inviteSend (){}

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

    public int getGroupMemberNum() {
        return groupMemberNum;
    }

    public void setGroupMemberNum(int groupMemberNum) {
        this.groupMemberNum = groupMemberNum;
    }

    public List<Friend> getInviteList() {
        return inviteList;
    }

    public void setInviteList(List<Friend> inviteList) {
        this.inviteList = inviteList;
    }
}
