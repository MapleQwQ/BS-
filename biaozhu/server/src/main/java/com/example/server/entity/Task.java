package com.example.server.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Task {
    @Id
    private Long taskId;
    private String taskName;
    private Timestamp release_time;
    private Timestamp finish_time;
    private int state;      //=0未领取，=1已领取，=2为已标注
    private Long rPersonId;
    private Long fPersonId;
    private String taskText;

    public Task() {
    }

    public Task(Long taskId, String taskName, Timestamp release_time, Timestamp finish_time, int state, Long rPersonId, Long fPersonId, String taskText) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.release_time = release_time;
        this.finish_time = finish_time;
        this.state = state;
        this.rPersonId = rPersonId;
        this.fPersonId = fPersonId;
        this.taskText = taskText;
    }

    public Task(String taskName, Timestamp release_time, Timestamp finish_time, int state, Long rPersonId, Long fPersonId, String taskText) {
        this.taskName = taskName;
        this.release_time = release_time;
        this.finish_time = finish_time;
        this.state = state;
        this.rPersonId = rPersonId;
        this.fPersonId = fPersonId;
        this.taskText = taskText;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Timestamp getRelease_time() {
        return release_time;
    }

    public void setRelease_time(Timestamp release_time) {
        this.release_time = release_time;
    }

    public Timestamp getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Timestamp finish_time) {
        this.finish_time = finish_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getrPersonId() {
        return rPersonId;
    }

    public void setrPersonId(Long rPersonId) {
        this.rPersonId = rPersonId;
    }

    public Long getfPersonId() {
        return fPersonId;
    }

    public void setfPersonId(Long fPersonId) {
        this.fPersonId = fPersonId;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", release_time=" + release_time +
                ", finish_time=" + finish_time +
                ", state=" + state +
                ", rPersonId=" + rPersonId +
                ", fPersonId=" + fPersonId +
                ", taskText='" + taskText + '\'' +
                '}';
    }
}
