package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 9/21/2017 at 1:06 PM.
 */

public class Member {
    private String memberUsername;
    private String memberDisplayName;
    private String memberImageURL;
    private int memberStatus;
    private boolean checkInvite;

    public boolean isCheckInvite() {
        return checkInvite;
    }

    public void setCheckInvite(boolean checkInvite) {
        this.checkInvite = checkInvite;
    }

    public String getMemberDisplayName() {
        return memberDisplayName;
    }

    public void setMemberDisplayName(String memberDisplayName) {
        this.memberDisplayName = memberDisplayName;
    }

    public String getMemberImageURL() {
        return memberImageURL;
    }

    public void setMemberImageURL(String memberImageURL) {
        this.memberImageURL = memberImageURL;
    }

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
