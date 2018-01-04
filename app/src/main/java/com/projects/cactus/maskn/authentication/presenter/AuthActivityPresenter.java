package com.projects.cactus.maskn.authentication.presenter;

import com.projects.cactus.maskn.authentication.model.AuthenticationDataManager;
import com.projects.cactus.maskn.authentication.view.AuthActivityContract;
import com.projects.cactus.maskn.utils.Util;


/**
 * Created by el on 6/8/2017.
 */

public class AuthActivityPresenter implements AuthActivityContract.Presenter {

    private AuthenticationDataManager authenticationDataManager;

    private AuthActivityContract.View view;

    public AuthActivityPresenter(AuthActivityContract.View view, AuthenticationDataManager authenticationDataManager) {
        this.view = view;
        this.authenticationDataManager = authenticationDataManager;
    }

    public void keepMeLogedIn(String keyLogedIn, String keyUserId, String userId) {
        authenticationDataManager.keepMeloggededIn(keyLogedIn, keyUserId, userId);
    }


    public String getUserId(String key) {

        return authenticationDataManager.getUserId(key);
    }


    public void checkLogedIn() {
        if (authenticationDataManager.isLoggedin(AuthenticationDataManager.KEY_LOG_SATE, Util.KEY_USER_ID))
            view.openProfileActivity(authenticationDataManager.getUserId(Util.KEY_USER_ID));


        else
            view.replaceLoginFragment();

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
    }
}
