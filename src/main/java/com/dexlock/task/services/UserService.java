package com.dexlock.task.services;


import com.dexlock.task.models.Comments;
import com.dexlock.task.models.FileDB;
import com.dexlock.task.models.Task;
import com.dexlock.task.models.*;
import com.dexlock.task.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class UserService implements ITaskService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FileDBRepository fileDBRepository;

    @Autowired
    private CommentRepository commentRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findById(task.getTaskId()).orElse(null);
        existingTask.setTitle(task.getTitle());
        existingTask.setAcceptanceCriteria(task.getAcceptanceCriteria());
        existingTask.setDescription(task.getDescription());
        existingTask.setDuedate(task.getDuedate());
        existingTask.setStartdate(task.getStartdate());
        existingTask.setOriginalestimate(task.getOriginalestimate());
        existingTask.setReporter(task.getReporter());
        existingTask.setStatus(task.getStatus());
        existingTask.setType(task.getType());
        return taskRepository.save(existingTask);
    }



    public FileDB store(FileDB fileDB) throws IOException {
        return fileDBRepository.save(fileDB);

    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }


    public void deleteBd(String id) {
        fileDBRepository.deleteById(id);
    }

    public Comments getComments(Long id) {
        return commentRepository.findById(id).get();
    }

    public Task getTaskBy(Long taskid) {
        return taskRepository.findById(taskid).get();
    }


    public String deleteId(Long id)
    {
        commentRepository.deleteById(id);
        return + id + " is deleted";
    }

    @Override
    public List<Task> findPaginated(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo,pageSize);
        Page<Task> pagedResult = taskRepository.findAll(paging);
        return pagedResult.toList();
    }
}