package com.dexlock.task.dto;


public class TaskResponse {
    private String username;
    private  String message;

    @Override
    public String toString() {
        return "TaskResponse{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public TaskResponse() {
    }

    public TaskResponse(String username, String message) {
        this.username = username;
        this.message = message;
    }
}
