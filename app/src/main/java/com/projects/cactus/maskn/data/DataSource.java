package com.projects.cactus.maskn.data;

import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;
import com.projects.cactus.maskn.search.SearchInput;
import com.squareup.sqlbrite2.QueryObservable;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;
import java.util.Observer;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;

/**
 * Created by el on 10/13/2017.
 */

public interface DataSource {


    //loading apartment
    io.reactivex.Observable<Apartment> getApartment(String apartmentId);
    io.reactivex.Observable<List<Apartment>> getApartments(String city, int numOfRooms, int priceFrom, int priceTo);

    //manipulating apartment
    Single<ServerResponse> addApartment(Apartment apartment);
    void removeApartment(String apartmentId);
    void removeApartment(Apartment apartment);

    boolean isApartmentAvailable(String apartmentId);
    void markApartmentRented(String apartmentId);

    Single<ServerResponse> addApartment(List<MultipartBody.Part> apartmentImages, Apartment apartment);

    Observable<List<Apartment>> getApartments(SearchInput searchInput);

    Observable<List<Apartment>> getUserApartment(String userId);

    Single<List<String>> getCities();

    Completable incrementApartmentViews(String apartmentId);

    void deleteApartmentFromFavourite(String id);

    long addApartmentToFavourite(Apartment apartment);

    void closeLocalDatabase();

    Observable<SqlBrite.Query> checkInFavouriteOrNot(Apartment apartment);

    Single<String> getAds();
}
