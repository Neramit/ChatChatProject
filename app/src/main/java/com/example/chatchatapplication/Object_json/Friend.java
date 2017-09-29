package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 9/12/2017.
 */

public class Friend {
    private String ownerUsername;
    private String friendUsername;
    private int friendStatus;
    private boolean isFavorite;
    private int chatroomUID;
    private String displayName;
    private String displayPictureURL;

    public String getFriendRegistrationID() {
        return friendRegistrationID;
    }

    public void setFriendRegistrationID(String friendRegistrationID) {
        this.friendRegistrationID = friendRegistrationID;
    }

    private String friendRegistrationID;

    private boolean checkInvite;

    public boolean getCheckInvite() {
        return checkInvite;
    }

    public void setCheckInvite(Boolean checkInvite) {
        this.checkInvite = checkInvite;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayPictureURL() {
        return displayPictureURL;
    }

    public void setDisplayPictureURL(String displayPictureURL) {
        this.displayPictureURL = displayPictureURL;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public int getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(int friendStatus) {
        this.friendStatus = friendStatus;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Integer getChatroomUID() {
        return chatroomUID;
    }

    public void setChatroomUID(Integer chatroomUID) {
        this.chatroomUID = chatroomUID;
    }
}
