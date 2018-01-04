package com.projects.cactus.maskn.authentication.view;


import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.authentication.model.User;

/**
 * Created by el on 6/8/2017.
 */

public interface LoginContract {


    interface LoginView extends BaseView<Presenter> {

        void setPresenter(Presenter presenter);

        void onLoginSuccess(User user);


        void showMessage(String message);

        void enableInput(boolean b);
    }


    interface Presenter extends BasePresenter {
        void login(String phone, String pass);

        boolean isLoggedIn(String keyLogSate, String keyUserId);
    }

}
