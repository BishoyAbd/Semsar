package com.projects.cactus.maskn.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.projects.cactus.maskn.data.DataSource;
import com.projects.cactus.maskn.data.apiservies.ApartmentsApi;
import com.projects.cactus.maskn.data.apiservies.ServiceGenerator;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;
import com.projects.cactus.maskn.search.SearchInput;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by el on 10/15/2017.
 */

public class RemoteDataSource implements DataSource {


    @NonNull
    private static RemoteDataSource instance;


    private RemoteDataSource() {
    }

    public static RemoteDataSource getInstance() {
        if (instance == null)
            return new RemoteDataSource();

        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }


    @Deprecated
    @Override
    public Observable<Apartment> getApartment(String apartmentId) {
        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);

        return null;
    }

    @Deprecated
    @Override
    public Observable<List<Apartment>> getApartments(String city, int numOfRooms, int priceFrom, int priceTo) {

        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);

        Observable<List<Apartment>> listObservable = apartmentsApi.getAllApartments(city, numOfRooms, priceFrom, priceTo);

        return listObservable;
    }


    @Override
    public Single<ServerResponse> addApartment(Apartment apartment) {
//
//        ApartmentsApi apartmentsApi= ServiceGenerator.createService(ApartmentsApi.class);
//        Single<ServerResponse> singleObserver=apartmentsApi.addApartment(apartment);
        return null;

    }

    @Override
    public void removeApartment(String apartmentId) {

    }

    @Override
    public void removeApartment(Apartment apartment) {

    }

    @Override
    public boolean isApartmentAvailable(String apartmentId) {
        return false;
    }

    @Override
    public void markApartmentRented(String apartmentId) {

    }

    @Override
    public Single<ServerResponse> addApartment(List<MultipartBody.Part> apartmentImages, Apartment apartment) {
        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);
        Single<ServerResponse> singleObserver = apartmentsApi.addApartment(apartmentImages, apartment);
        return singleObserver;
    }


    @Override
    public Observable<List<Apartment>> getApartments(SearchInput searchInput) {
        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);
        Observable<List<Apartment>> listObservable = apartmentsApi.getAllApartments(searchInput);
        return listObservable;
    }

    @Override
    public Observable<List<Apartment>> getUserApartment(String userId)
    {
        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);

        return apartmentsApi.getUserApartments(userId);
    }

    @Override
    public Single<List<String>> getCities() {

        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);
        return apartmentsApi.getCities();
    }

    @Override
    public Completable incrementApartmentViews(String apartmentId) {
        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);
        return apartmentsApi.incrementApartmentViews(apartmentId);
    }

    @Override
    public void deleteApartmentFromFavourite(String id) {

    }

    @Override
    public long addApartmentToFavourite(Apartment apartment) {
        return 0;
    }

    @Override
    public void closeLocalDatabase() {

    }

    @Override
    public Observable<SqlBrite.Query> checkInFavouriteOrNot(Apartment apartment) {
        return null;
    }

    @Override
    public Single<String> getAds() {
        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);
        return apartmentsApi.getAds();
    }
}
