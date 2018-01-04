package com.projects.cactus.maskn.authentication.presenter;

import com.projects.cactus.maskn.authentication.model.User;
import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;

/**
 * Created by el on 10/31/2017.
 */

public class AuthServerResponse extends ServerResponse {

    private User user;

    public AuthServerResponse() {

    }

    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
