package com.example.ReExam.models;

public class MessageStatistic {
    private final String username;
    private final Long count;

    public MessageStatistic(String username, Long count) {
        this.username = username;
        this.count = count;
    }

    public String getUsername() {
        return username;
    }

    public Long getCount() {
        return count;
    }
}
