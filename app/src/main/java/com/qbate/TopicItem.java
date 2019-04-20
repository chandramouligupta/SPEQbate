package com.qbate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Timestamp;

public class TopicItem {
    private String topicId;
    private String topicCategoryId;
    private String topicTitle;
    private long timestamp;
    private String creator; //user id that created this topic

    public TopicItem() {
    }

    public TopicItem(String topicId, String topicCategoryId, String topicTitle, long timestamp, String creator) {
        this.topicId = topicId;
        this.topicCategoryId = topicCategoryId;
        this.topicTitle = topicTitle;
        this.timestamp = timestamp;
        this.creator = creator;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicCategoryId() {
        return topicCategoryId;
    }

    public void setTopicCategoryId(String topicCategoryId) {
        this.topicCategoryId = topicCategoryId;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
