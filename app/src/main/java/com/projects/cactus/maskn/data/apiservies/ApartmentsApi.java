package com.projects.cactus.maskn.data.apiservies;


import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;
import com.projects.cactus.maskn.search.SearchInput;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by el on 10/7/2017.
 */

public interface ApartmentsApi {


    @Deprecated
    @FormUrlEncoded
    @POST("maskan/users/apartments_search.php")
    Observable<List<Apartment>> getAllApartments(@Field("city") String city, @Field("num_rooms") int numOfRooms,
                                                 @Field("min_price") int priceFrom, @Field("max_price") int priceTo);


    @POST("maskan/users/apartments_search.php")
    Observable<List<Apartment>> getAllApartments(@Body SearchInput searchInput);

//
//    @POST()
//    Observable<Apartment> getApartment(@Field("apartment_id") String apartmentId);

//    @POST()
//    Observable<ServerResponse> getApartmentImages(@Field("apartment_id")  String apartmentId);


    //http://bishoy.esy.es/retrofit/uploadToMaskan.php
    @Multipart
    @POST("maskan/users/add_apartment.php")
    Single<ServerResponse> addApartment(@Part List<MultipartBody.Part> images, @Part("apartment") Apartment apartment);


    @POST()
    Observable<ServerResponse> removeApartment(@Field("apartment_id") String apartmentId);

    @POST()
    Observable<ServerResponse> updateApartment(@Field("apartment_id") Apartment apartment);

    @FormUrlEncoded
    @POST("maskan/users/get_owners_apartments.php")
    Observable<List<Apartment>> getUserApartments(@Field("id") String userId);

    @POST("maskan/users/get_cities.php")
    Single<List<String>> getCities();

    @FormUrlEncoded
    @POST("maskan/users/increment_views.php")
    Completable incrementApartmentViews(@Field("id") String apartmentId);

    //getting ads in apartment
    @POST("maskan/users/ads.php")
    Single<String> getAds();

    @FormUrlEncoded
    @POST("maskan/users/report_problem.php")
    Completable reportProblem(@Field("problem") String problem );

}
