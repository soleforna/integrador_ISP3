package com.rocketteam.passkeeper.data.model.request;

/**
 * Clase que representa las credenciales de una contraseña.
 */
public class PasswordCredentials {
    private String name;

    private String user;

    private String password;

    private String url;

    private String description;

    public PasswordCredentials(String name, String user, String password, String url, String description){
        this.name = name;
        this.password = password;
        this.user = user;
        this.url = url;
        this.description = description;


    }

}
