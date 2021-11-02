package com.example.ReExam.models;

public class MessageFiltered {
    private final Long id;
    private final String stringDate;
    private final String username;
    private final String message;

    public MessageFiltered(Long id, String stringDate, String username, String message) {
        this.id = id;
        this.stringDate = stringDate;
        this.username = username;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getStringDate() {
        return stringDate;
    }
}
