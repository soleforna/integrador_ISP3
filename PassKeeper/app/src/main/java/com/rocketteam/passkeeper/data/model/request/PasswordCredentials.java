package com.rocketteam.passkeeper.data.model.request;

/**
 * Clase que representa las credenciales de una contraseña.
 */
public class PasswordCredentials {
    private Integer userId;

    private String name;

    private String user;

    private String password;

    private String url;

    private String description;

    public PasswordCredentials(Integer userId, String name, String user, String password, String url, String description){

        this.userId = userId;
        this.name = name;
        this.password = password;
        this.user = user;
        this.url = url;
        this.description = description;


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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
