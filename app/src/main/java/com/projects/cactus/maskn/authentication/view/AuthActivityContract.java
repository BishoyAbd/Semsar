package com.projects.cactus.maskn.authentication.view;

import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;

/**
 * Created by el on 6/8/2017.
 */

public interface AuthActivityContract {


    interface View extends BaseView<Presenter> {

        void openProfileActivity(String userId);

        void replaceLoginFragment();
    }

    interface Presenter extends BasePresenter {

        void checkLogedIn();

    }


}
