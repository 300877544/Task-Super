package com.dexlock.task.dto;

import com.dexlock.task.models.Task;

public class TaskRequest {
    private Task task;

    @Override
    public String toString() {
        return "TaskRequest{" +
                "task=" + task +
                '}';
    }

    public TaskRequest(Task task) {
        this.task = task;
    }

    public TaskRequest() {
    }
}
