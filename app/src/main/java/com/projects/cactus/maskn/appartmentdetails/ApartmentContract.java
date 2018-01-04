package com.projects.cactus.maskn.appartmentdetails;

import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;

import java.util.List;

/**
 * Created by el on 10/24/2017.
 */

public interface ApartmentContract {


    interface View extends BaseView<Presenter> {
        void displayApartment(Apartment apartment);

        void openUserProfile();

        void dialPhoneNumber(String phoneNumber);

        void showMessage(String s);

        void markFavourite(boolean b);

        void displayApartmentOwner(boolean userOwnThisApartment);

        void initializeSlider(List<String> strings);

        void showAds(String url);
    }


    interface Presenter extends BasePresenter {

        void getApartmentById(String apartmentId);

        void getAds();

        void incrementViews(String apartmentId);

        void deleteFavourite(String appartmentId);

        void addToFavourite(Apartment apartment);

        void checkInFavouriteOrNot(Apartment apartment);

        void listenToBus();


        void checkApartmentBelongsToUserOrNot(String savedUserId, String apartmentOwnerId);
    }


}
