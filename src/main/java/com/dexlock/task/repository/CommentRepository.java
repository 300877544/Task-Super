package com.dexlock.task.repository;

import com.dexlock.task.dto.TaskResponse;
import com.dexlock.task.models.Comments;
import com.dexlock.task.models.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    @Query(
            value = "SELECT * from comments where task_id=?",
            nativeQuery = true)
    public List<Comments> getCommentsInformation(Long taskId);


}


