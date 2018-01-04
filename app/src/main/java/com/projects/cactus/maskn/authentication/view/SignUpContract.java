package com.projects.cactus.maskn.authentication.view;


import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.authentication.model.User;
import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;

import io.reactivex.Single;

/**
 * Created by el on 6/7/2017.
 */

public interface SignUpContract {


    interface SignUpView extends BaseView<Presenter> {

        void setPresenter(LoginContract.Presenter presenter);

        void onSignUpSuccess(User userId);

        void showMessage(String message);
    }


    interface Presenter extends BasePresenter {
        void signUp(User user);
    }

}
