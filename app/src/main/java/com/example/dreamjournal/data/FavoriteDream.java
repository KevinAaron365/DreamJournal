package com.example.dreamjournal.data;

public class FavoriteDream {
    private String userID;
    private String dreamID;
    private long date;
    private String title;
    private String description;

    private String isPublic;

    private String sender;
    private boolean starred;

    public FavoriteDream(String userID, String dreamID, long date, String title, String description, String isPublic, String sender, boolean starred) {
        this.userID = userID;
        this.dreamID = dreamID;
        this.date = date;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.sender = sender;
        this.starred = starred;
    }

    public FavoriteDream(String userID, String dreamID, long date, String title, String description, String isPublic, String sender) {
        this.userID = userID;
        this.dreamID = dreamID;
        this.date = date;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.sender = sender;
    }


    public FavoriteDream() {

    }


    public String getIsPublic() {
        return isPublic;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDreamID() {
        return dreamID;
    }

    public void setDreamID(String dreamID) {
        this.dreamID = dreamID;
    }

    public boolean isStarred() {

        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
