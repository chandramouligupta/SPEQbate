package com.qbate;

public class CommentItem {
    private String commentId;
    private String topicId;
    private String categoryId;
    private String username;
    private String creatorId; // Unique Key Generated by firebase i.e USERIDKEY
    private String email; // email id of the creator
    private long timestamp;
    private String photoUrl;
    private String commentTitle;

    public CommentItem() {
    }

    public CommentItem(String commentId, String topicId, String categoryId, String username, String creatorId, String email, long timestamp, String photoUrl, String commentTitle) {
        this.commentId = commentId;
        this.topicId = topicId;
        this.categoryId = categoryId;
        this.username = username;
        this.creatorId = creatorId;
        this.email = email;
        this.timestamp = timestamp;
        this.photoUrl = photoUrl;
        this.commentTitle = commentTitle;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }
}
