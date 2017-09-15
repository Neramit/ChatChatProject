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
