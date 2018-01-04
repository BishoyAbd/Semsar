package com.projects.cactus.maskn.data;

import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;
import com.projects.cactus.maskn.data.local.LocalDataSource;
import com.projects.cactus.maskn.data.remote.RemoteDataSource;
import com.projects.cactus.maskn.search.SearchInput;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;

/**
 * Created by el on 10/13/2017.
 */

/**
 * this class will wrap all data operation(requests and synchronization of remote and local data )
 */
public class ApartmentRepository implements DataSource {

    //singleton instance
    private static ApartmentRepository instance=null;
    private RemoteDataSource mRemoteDataSource;
    private LocalDataSource mLocalDataSource;


    //to prevent direct instantiation
    private ApartmentRepository(RemoteDataSource mRemoteDataSource, LocalDataSource mLocalDataSource) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
    }

    public static ApartmentRepository getInstance(RemoteDataSource mRemoteDataSource, LocalDataSource mLocalDataSource) {
        if (instance == null)
            return new ApartmentRepository(mRemoteDataSource, mLocalDataSource);

        return instance;

    }


    @Override
    public Observable<Apartment> getApartment(String apartmentId)

    {
        return mRemoteDataSource.getApartment(apartmentId);
    }

    @Deprecated
    @Override
    public Observable<List<Apartment>> getApartments(String city, int numOfRooms, int priceFrom, int priceTo) {
        //will be delegated to remote data source
        return mRemoteDataSource.getApartments(city, numOfRooms, priceFrom, priceTo);
    }

    @Override
    public Single<ServerResponse> addApartment(Apartment apartment) {
        return mRemoteDataSource.addApartment(apartment);
    }

    @Override
    public void removeApartment(String apartmentId) {
        mRemoteDataSource.removeApartment(apartmentId);
    }

    @Override
    public void removeApartment(Apartment apartment) {
        mRemoteDataSource.removeApartment(apartment);
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
        return mRemoteDataSource.addApartment(apartmentImages, apartment);
    }

    @Override
    public Observable<List<Apartment>> getApartments(SearchInput searchInput) {
        return mRemoteDataSource.getApartments(searchInput);
    }

    @Override
    public Observable<List<Apartment>> getUserApartment(String userId) {
        return mRemoteDataSource.getUserApartment(userId);
    }

    @Override
    public Single<List<String>> getCities() {
        return mRemoteDataSource.getCities();
    }

    @Override
    public Completable incrementApartmentViews(String apartmentId) {
        return mRemoteDataSource.incrementApartmentViews(apartmentId);
    }

    @Override
    public void deleteApartmentFromFavourite(String id)  {
        mLocalDataSource.deleteApartmentFromFavourite(id);
    }

    @Override
    public long addApartmentToFavourite(Apartment apartment) {
       return mLocalDataSource.add(apartment);
    }

    @Override
    public void closeLocalDatabase() {
        mLocalDataSource.close();

    }

    @Override
    public Observable<SqlBrite.Query> checkInFavouriteOrNot(Apartment apartment) {
        return   mLocalDataSource.checkInFavouriteOrNot(apartment);

    }

    @Override
    public Single<String> getAds() {
        return mRemoteDataSource.getAds();
    }

    public Observable<SqlBrite.Query> getAllFavouriteApartments() {
        return   mLocalDataSource.getAllFavs();


    }

}
