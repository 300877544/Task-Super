package com.dexlock.task.models;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(	name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;

    @Column
    private String title;

    @Column
    private String type;

    @Column
    private String description;

    @Column
    private String acceptanceCriteria;

    @Column
    private String status;

    @Column
    private Long startdate;

    @Column
    private Long duedate;

    @Column
    private String originalestimate;

    @Column
    private String reporter;

    @OneToMany(targetEntity = Comments.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "tc_fk", referencedColumnName = "taskId")
   private List<Comments> comments;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;

    }

    public Long getDuedate() {
        return duedate;
    }

    public void setDuedate(Long duedate) {
        this.duedate = duedate;
    }

    public String getOriginalestimate() {
        return originalestimate;
    }

    public void setOriginalestimate(String originalestimate) {
        this.originalestimate = originalestimate;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Task(Long taskId, String title, String type, String description, String acceptanceCriteria, String status, Long startdate, Long duedate, String originalestimate, String reporter) {
        this.taskId = taskId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.acceptanceCriteria = acceptanceCriteria;
        this.status = status;
        this.startdate = startdate;
        this.duedate = duedate;
        this.originalestimate = originalestimate;
        this.reporter = reporter;
    }

    public Task() {}


    public Task(Long taskId, String title, String type, String description, String acceptanceCriteria, String status, Long startdate, Long duedate, String originalestimate, String reporter, List<Comments> comments) {
        this.taskId = taskId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.acceptanceCriteria = acceptanceCriteria;
        this.status = status;
        this.startdate = startdate;
        this.duedate = duedate;
        this.originalestimate = originalestimate;
        this.reporter = reporter;
        this.comments = comments;
    }

    public Task(String title, String type, String description, String acceptanceCriteria, String status, Long startdate, Long duedate, String originalestimate, String reporter) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.acceptanceCriteria = acceptanceCriteria;
        this.status = status;
        this.startdate = startdate;
        this.duedate = duedate;
        this.originalestimate = originalestimate;
        this.reporter = reporter;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
