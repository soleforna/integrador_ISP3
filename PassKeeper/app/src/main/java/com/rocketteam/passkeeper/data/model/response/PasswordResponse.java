package com.rocketteam.passkeeper.data.model.response;

public class PasswordResponse {
    private int id;
    private String username;
    private String url;
    private String keyword;
    private String description;
    private String name;

    public PasswordResponse(int id, String username, String url,String keyword, String description, String name){
        this.id=id;
        this.username = username;
        this.url = url;
        this.keyword = keyword;
        this.description = description;
        this.name = name;


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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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