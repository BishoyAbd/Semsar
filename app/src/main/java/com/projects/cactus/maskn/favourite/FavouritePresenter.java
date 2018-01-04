package com.projects.cactus.maskn.favourite;

import android.database.Cursor;

import com.projects.cactus.maskn.data.ApartmentRepository;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.data.local.ApartmentSqlHelper;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * Created by bisho on 11/11/2017.
 */

public class FavouritePresenter implements FavouriteContract.Presenter {


    private ApartmentRepository apartmentRepository;
    private FavouriteContract.View view;
    private CompositeDisposable compositeDisposable;

    public FavouritePresenter(FavouriteContract.View view, ApartmentRepository apartmentRepository) {
        this.view = view;
        this.apartmentRepository = apartmentRepository;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        getAllFavouriteApartments();

    }

    @Override
    public void unsubscribe() {
        apartmentRepository.closeLocalDatabase();
        compositeDisposable.clear();

    }


    @Override
    public void getAllFavouriteApartments() {

        final ArrayList<Apartment> apartmentList = new ArrayList<>();
        view.showLoading();
        compositeDisposable.clear();
        Disposable disposable = apartmentRepository.getAllFavouriteApartments().
                observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SqlBrite.Query>() {


                    @Override
                    public void onNext(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        Timber.d("number of apartments ---> " + cursor.getCount());
                        while (cursor.moveToNext()) {

                            Apartment apartment = new Apartment();
                            apartment.setAppartmentId(cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_ID)));
                            apartment.setmOwnerName(cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_OWNER_NAME)));
                            apartment.setmPhoneNumber(cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_PHONE)));
                            apartment.setCity(cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_CITY)));
                            apartment.setPrice(cursor.getInt(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_PRICE)));
                            apartment.setRoomsNumber(cursor.getInt(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_ROOMS)));
                            apartment.setNumOfViews(cursor.getInt((cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_VIEWS))));
                            apartment.setAddress(cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_ADDRESS)));
                            apartment.setDate(cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_DATE)));
                            apartment.setDescription(cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_DESCRIPTION)));
                            String imagesUrls = cursor.getString(cursor.getColumnIndex(ApartmentSqlHelper.COLUMN_URL));
                            Timber.d("current apart id -->" + apartment.getAppartmentId());
                            apartment.setmApartmentImages(getImages(imagesUrls));
                            boolean added = apartmentList.add(apartment);
                            Timber.d("apartment added --->" + added);
                        }

                        view.hideLoading();
                        if (apartmentList.isEmpty())
                            view.showNoResult();
                        else
                            view.ShowFavouriteApartments(apartmentList);
                    }


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    private List<String> getImages(String string) {
        Timber.d("images to split -->" + string);
        List<String> urls = new ArrayList<>(Arrays.asList(string.split(",")));
        Timber.d("final list--> " + urls.toString());
        return urls;
    }

}
