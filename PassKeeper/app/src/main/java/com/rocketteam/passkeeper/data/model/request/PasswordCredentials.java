package com.rocketteam.passkeeper.data.model.request;

/**
 * Clase que representa las credenciales de una contraseña.
 */
public class PasswordCredentials {
    private String username;
    private String url;
    private String keyword;
    private String description;
    private String name;
    private Integer userId;

    public PasswordCredentials(String username, String url, String keyword, String description,String name, Integer userId){
        this.username = username;
        this.url = url;
        this.keyword = keyword;
        this.description = description;
        this.name = name;
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
