package com.projects.cactus.maskn.profile;

import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.authentication.model.User;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;

import java.util.List;

/**
 * Created by el on 10/13/2017.
 */

public interface ProfileContract {


    interface View extends BaseView<Presenter>{

        void showUserInfo(User user);
        void EditProfile(boolean isAuthorized);
        void NavigateToAddApartmentActivity();
        void showUserApartmentList(List<Apartment> apartments);
        void openApartmentActivity(String apartmentId);
        void openApartmentActivity(Apartment apartment);

    }


    interface Presenter extends BasePresenter{

        void  getUserInfo(String UserId);//including added apartments
        void  getApartmentsList(String userId);
        void logOut();


    }




}
