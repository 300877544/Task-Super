package com.dexlock.task.controller;

import com.dexlock.task.config.JwtTokenUtil;
import com.dexlock.task.message.ResponseFile;
import com.dexlock.task.message.ResponseMessage;
import com.dexlock.task.models.Comments;
import com.dexlock.task.models.FileDB;
import com.dexlock.task.models.Task;
import com.dexlock.task.models.User;
import com.dexlock.task.payload.response.JwtResponses;
import com.dexlock.task.repository.CommentRepository;
import com.dexlock.task.repository.FileDBRepository;
import com.dexlock.task.repository.ITaskService;
import com.dexlock.task.repository.TaskRepository;
import com.dexlock.task.services.JwtUserDetailsService;
import com.dexlock.task.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService service;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FileDBRepository fileDBRepository;

    @Autowired
    private ITaskService taskService;

    String jwtToken;
    String username;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authenticationRequest) throws Exception{
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final  String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponses(token));
    }
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
        catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user, HttpServletRequest request) {
        String jwtToken = null;
        String username = null;
        final String requestTokenHeader = request.getHeader("Authorization");
        jwtToken = requestTokenHeader.substring(7);
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if (username.equals("Super-Admin")) {
            return service.saveUser(user);
        }
        return null;
    }

    @PostMapping("/task")
    public Task addTask(@RequestBody Task task)
    {
        return service.saveTask(task);
    }

    @PutMapping("/task")
    public Task updateTask(@RequestBody Task task){ return service.updateTask(task);}

    @GetMapping("/getInfo/{taskId}")
    public List<Comments> getJoinInformation(@PathVariable Long taskId) {
        //displays comments based on the task id
        return commentRepository.getCommentsInformation(taskId);
    }

    @GetMapping("/getFile/{task_id}")
    public List<FileDB> getFileInformation(@PathVariable Long task_id) {
        // //displays files based on the task id
        return fileDBRepository.getFileDBInformation(task_id);
    }

    @PostMapping("/{taskId}/comments")
    public Comments saveComment(@RequestBody Comments comments, @PathVariable("taskId") Long taskId, HttpServletRequest request) {
        String jwtToken = null;
        String username = null;
        final String requestTokenHeader = request.getHeader("Authorization");
        jwtToken = requestTokenHeader.substring(7);
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        comments.setUsername(username);
        Calendar calendar = Calendar.getInstance();
        comments.setUpdatedTime(calendar.getTimeInMillis());
        comments.setTaskId(taskId);
        return commentRepository.save(comments);
    }

    @PutMapping("/{taskId}/comments/{id}")
    public Comments updateComments(@PathVariable Long id, @PathVariable Long taskId, @RequestBody Comments comments, HttpServletRequest request) {
        Comments existingComments = service.getComments(id);
        final String requestTokenHeader = request.getHeader("Authorization");
        jwtToken = requestTokenHeader.substring(7);
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        existingComments.setUsername(username);
        Calendar calendar = Calendar.getInstance();
        existingComments.setUpdatedTime(calendar.getTimeInMillis());
        existingComments.setMessage(comments.getMessage());
        existingComments.setTaskId(taskId);
        return commentRepository.save(existingComments);
    }


    @DeleteMapping("/comments/{id}")
    public String deleteComments(@PathVariable Long id, HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        jwtToken = requestTokenHeader.substring(7);
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if (username.equals("Super-Admin") || username.equals("Project-Manager")) {
            return service.deleteId(id);
        }
         return null;
    }

    @PostMapping("/{taskId}/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile file)
    {
        String message="";
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
            fileDB.setTaskId(taskId);
            service.store(fileDB);
            message = "Uploaded file successfully: " + file.getOriginalFilename();
            return  ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
        catch (Exception e)
        {
            message = "Could not update file " + file.getOriginalFilename() ;
            return  ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles(){
        List<ResponseFile> files =  service.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files")
                    .path(dbFile.getId())
                    .toUriString();
            return  new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id){
        //gets file based on the id
        FileDB fileDB = service.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, " attachment; filename= " + fileDB.getName() + "/")
                .body(fileDB.getData());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String id, HttpServletRequest request) {
        //deletes file based on id
        String message = "";
        try {
            service.deleteBd(id);
            message = " file deleted successfully: " ;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        }
        catch (Exception e) {
            message = "Could not delete file ";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/task/{pageNo}/{pageSize}")//pagination
    public List<Task> getPaginatedTask(@PathVariable int pageNo, @PathVariable int pageSize){
        return taskService.findPaginated(pageNo,pageSize);
    }

    @GetMapping("/taskFindDesc")
    public List<Task> findDescription
            (@RequestParam Optional<String> description){
        return taskRepository.findByDescription(description.orElse("_"));
        //returns by description
    }
}
