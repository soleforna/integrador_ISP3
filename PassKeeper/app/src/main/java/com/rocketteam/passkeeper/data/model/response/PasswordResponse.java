package com.rocketteam.passkeeper.data.model.response;

public class PasswordResponse {
    private int id;

    private String name;

    private String username;

    private String password;

    private String url;

    private String description;

    public PasswordResponse(int id, String name, String username, String password, String url, String description){
        this.id=id;
        this.name = name;
        this.password = password;
        this.username = username;
        this.url = url;
        this.description = description;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}