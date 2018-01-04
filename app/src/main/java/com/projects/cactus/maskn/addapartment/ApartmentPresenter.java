package com.projects.cactus.maskn.addapartment;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.mobsandgeeks.saripaar.Validator;
import com.projects.cactus.maskn.data.ApartmentRepository;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.data.apiservies.model.ServerResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import timber.log.Timber;

/**
 * Created by el on 10/19/2017.
 */

public class ApartmentPresenter implements AddApartmentContract.Presenter {

    private static final int MALE = 1;
    private static final int FEMALE = 2;
    private static final int ALL = 3;
    @NonNull
    private final ApartmentRepository mApartmentRepository;
    private AddApartmentContract.View view;
    @NonNull
    private CompositeDisposable compositeDisposable;

    public ApartmentPresenter(AddApartmentContract.View view, @NonNull ApartmentRepository mApartmentRepository) {
        this.view = view;
        this.mApartmentRepository = mApartmentRepository;
        compositeDisposable = new CompositeDisposable();

    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

        compositeDisposable.clear();
    }


    @Override
    public void addApartment(final Apartment apartment) {
        view.showLoading();

        mApartmentRepository.addApartment(apartment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ServerResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull ServerResponse serverResponse) {
                        process(serverResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        view.setWindowEnabled(true);
                        view.hideLoading();
                        view.showError();

                    }
                });
        ;

    }

    @Override
    public void validate(Validator validator) {
        validator.validate();
    }

    @Override
    public void onResult(int REQUEST_CODE, Intent data) {
        if (REQUEST_CODE == AddApartmentActivity.REQUEST_CODE_APARTMENT_LIST_IMAGES) {
        }

    }

    @Override
    public void addApartment(List<MultipartBody.Part> apartmentImages, Apartment apartment) {


        if (apartmentImages!=null){
            view.setWindowEnabled(false);
            view.showLoading();
            mApartmentRepository.addApartment(apartmentImages, apartment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<ServerResponse>() {
                        @Override
                        public void onSuccess(@io.reactivex.annotations.NonNull ServerResponse serverResponse) {
                            process(serverResponse);

                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            view.setWindowEnabled(true);
                            view.hideLoading();
                            view.showError();

                        }
                    });
        }

        else
            view.showErrorNoImagesAdded();


    }

    @Override
    public void getCitiesList() {


        Disposable d = mApartmentRepository.getCities()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<String> cities) {
                        view.onGettingCities(cities);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });

        compositeDisposable.add(d);
    }

    private void process(ServerResponse serverResponse) {
        view.hideLoading();
        view.hideError();
        view.setWindowEnabled(true);

        Timber.d(serverResponse.toString());
        if (!Boolean.parseBoolean(serverResponse.getError()))
            view.showApartmentAddedSuccessfully();
        else
            view.showError();

    }


}
