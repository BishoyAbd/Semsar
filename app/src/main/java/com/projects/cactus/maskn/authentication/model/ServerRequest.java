package com.projects.cactus.maskn.authentication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bishoy on 2/28/2017.
 */

//server request for user login and sign up operations
public class ServerRequest {

    @SerializedName("operation")
    private String operation;
    @SerializedName("user")
    private User user;


    public ServerRequest(){

    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }
}