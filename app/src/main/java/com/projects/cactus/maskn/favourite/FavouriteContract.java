package com.projects.cactus.maskn.favourite;

import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by bisho on 11/11/2017.
 */

public interface FavouriteContract {

    interface View extends BaseView<Presenter> {
        void showNoResult();

        void ShowFavouriteApartments(List<Apartment> apartments);
        void showMessage(String s);
    }


    interface Presenter extends BasePresenter {



        void getAllFavouriteApartments();

    }

}
