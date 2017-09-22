package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 9/21/2017 at 1:06 PM.
 */

public class Member {
    private String memberUsername;
    private int memberStatus;

    public Member(String memberUsername, int memberStatus) {
        this.memberUsername = memberUsername;
        this.memberStatus = memberStatus;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }
}
