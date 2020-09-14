package com.dexlock.task.models;

import javax.persistence.*;


@Entity
@Table(	name = "comments")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    private Long taskId;

    private String username;

    private String message;


    private Long updatedTime;

    public Comments() {
    }

    public Comments(Long id, String username, String message, Long updatedTime) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.updatedTime = updatedTime;
    }

    public Comments(Long id, Long taskId, String username, String message, Long updatedTime) {
        this.id = id;
        this.taskId = taskId;
        this.username = username;
        this.message = message;
        this.updatedTime = updatedTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
