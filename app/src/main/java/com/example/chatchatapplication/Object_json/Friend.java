package com.example.chatchatapplication.Object_json;

/**
 * Created by Neramit777 on 9/12/2017.
 */

public class Friend {
    private String ownerUsername;
    private String friendUsername;
    private String friendStatus;
    private Boolean isFavorite;
    private Integer chatroomUID;
    private String displayName;
    private String displayPictureURL;

    public String getFriendRegistrationID() {
        return friendRegistrationID;
    }

    public void setFriendRegistrationID(String friendRegistrationID) {
        this.friendRegistrationID = friendRegistrationID;
    }

    private String friendRegistrationID;

    private Boolean checkInvite;

    public Boolean getCheckInvite() {
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

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Integer getChatroomUID() {
        return chatroomUID;
    }

    public void setChatroomUID(Integer chatroomUID) {
        this.chatroomUID = chatroomUID;
    }
}
