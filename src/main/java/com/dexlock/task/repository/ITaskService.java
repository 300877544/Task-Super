package com.dexlock.task.repository;

import com.dexlock.task.models.Task;

import java.util.List;

public interface ITaskService {
    List<Task> findPaginated(int pageNo, int pageSize);
}
