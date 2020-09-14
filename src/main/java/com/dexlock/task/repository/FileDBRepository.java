package com.dexlock.task.repository;

import com.dexlock.task.models.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
    @Query(
            value = "Select * from files where task_id=?",
            nativeQuery = true
    )
    public List<FileDB> getFileDBInformation(Long task_id);
}
