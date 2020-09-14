package com.dexlock.task.repository;

import com.dexlock.task.dto.TaskResponse;
import com.dexlock.task.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long>, JpaRepository<Task, Long> {
    @Query(value = "Select * from task where description like %?1%", nativeQuery = true)
    List<Task> findByDescription(String description);
}
