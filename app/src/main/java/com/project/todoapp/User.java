package com.project.todoapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private String username;
    private String password,name;

    public String getUsername(){return this.username;}
    public void setUsername(String username){this.username=username;}

    public String getPassword(){return this.password;}
    public void setPassword(String password){this.password=password;}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
