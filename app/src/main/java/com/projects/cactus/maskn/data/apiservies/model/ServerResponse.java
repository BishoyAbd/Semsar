package com.projects.cactus.maskn.data.apiservies.model;

import com.projects.cactus.maskn.authentication.model.User;

/**
 * Created by el on 10/15/2017.
 */
public class ServerResponse {

    private String error;
    private String message;
    private String result;
    private User user;


    public ServerResponse(){

   }
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}