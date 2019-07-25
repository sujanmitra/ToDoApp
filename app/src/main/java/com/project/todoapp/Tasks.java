package com.project.todoapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tasks extends RealmObject {

    @PrimaryKey
    private long id;
    private String username,task_details,date,task_name,task_color;
    boolean check_task;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTask_details() {
        return task_details;
    }

    public void setTask_details(String task_details) {
        this.task_details = task_details;
    }

    public boolean isCheck_task() {
        return check_task;
    }

    public void setCheck_task(boolean check_task) {
        this.check_task = check_task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_color(){return task_color;}

    public void setTask_color(String color){task_color=color;}
}
