package com.projects.cactus.maskn.appartments;

import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.search.SearchInput;

import java.util.List;

/**
 * Created by el on 10/8/2017.
 */

public interface SearchContract {


    interface View extends BaseView<Presenter> {

        void showAllApartmnets(List<Apartment> apartments);
        void navigateToSearchActivity(String city,int numOfRooms);
        void openApartmentDetails(String apartmentId);
        void showAddApartment();
        void showUserIsNotLoggedInDialog(); //login & signup if new user
        void showNoResult();
        void setTitle(String title);
    }


    interface Presenter extends BasePresenter{

       // void search(String city, int numOfRooms,int priceFrom,int priceTo);

        void search(SearchInput searchInput);

        void listenToBus();

        void onClickRefresh();
//        void isUserLoggedIn();
//        void isUserRegistered();


    }


}
