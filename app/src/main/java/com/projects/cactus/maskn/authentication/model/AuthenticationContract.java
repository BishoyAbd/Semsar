package com.projects.cactus.maskn.authentication.model;

import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;

import io.reactivex.Single;

/**
 * Created by el on 6/7/2017.
 */

interface AuthenticationContract {

    Single<ServerResponse> SignUpUer(User user);

    Single<ServerResponse> loginUser(String name, String password);

    boolean isLoggedin(String key_log_state, String key_user_id);

    String getUserId(String key);

    void keepMeloggededIn(String keyLogedIn, String keyUser, String userId);

}