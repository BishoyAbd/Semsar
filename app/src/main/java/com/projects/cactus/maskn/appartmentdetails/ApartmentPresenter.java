package com.projects.cactus.maskn.appartmentdetails;


import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.authentication.model.AuthenticationDataManager;
import com.projects.cactus.maskn.data.ApartmentRepository;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.utils.Util;
import com.squareup.sqlbrite2.SqlBrite;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by el on 10/24/2017.
 */

public class ApartmentPresenter implements ApartmentContract.Presenter {

    private ApartmentRepository mApartmentRepository;
    private ApartmentContract.View view;
    private AuthenticationDataManager authenticationDataManager;
    private CompositeDisposable mCompositeDisposable;

    public ApartmentPresenter(ApartmentContract.View view, AuthenticationDataManager authenticationDataManager, ApartmentRepository mApartmentRepository) {
        checkNotNull(view, "ApartmentContract.View cant be null");
        checkNotNull(view, " ApartmentRepository cant be null");
        this.mApartmentRepository = mApartmentRepository;
        this.view = view;
        this.authenticationDataManager = authenticationDataManager;
        mCompositeDisposable = new CompositeDisposable();
        view.setPresenter(this);

    }

    @Override
    public void subscribe() {
        getAds();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getApartmentById(String apartmentId) {

    }


    @Override
    public void getAds() {
        mCompositeDisposable.clear();
        Disposable disposable = mApartmentRepository.getAds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {


                    @Override
                    public void onSuccess(String url) {
                        view.showAds(url);
                        Timber.d("ads url---> " + url);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void incrementViews(String apartmentId) {

        Timber.d("apartment id to increment views -->" + apartmentId);
        mApartmentRepository.incrementApartmentViews(apartmentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("view incremented successfully ");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d("error incrementing views --> " + e.getLocalizedMessage());
                    }
                })
        ;
    }


    @Override
    public void deleteFavourite(String appartmentId) {
        mApartmentRepository.deleteApartmentFromFavourite(appartmentId);
    }


    @Override
    public void addToFavourite(Apartment apartment) {
        long id = mApartmentRepository.addApartmentToFavourite(apartment);
        if (id > 0) {
            view.markFavourite(true);
        } else {
            view.markFavourite(false);
            mApartmentRepository.deleteApartmentFromFavourite(apartment.getAppartmentId());
        }
    }

    @Override
    public void checkInFavouriteOrNot(Apartment apartment) {
        mApartmentRepository.checkInFavouriteOrNot(apartment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SqlBrite.Query>() {
                    @Override
                    public void accept(SqlBrite.Query query) throws Exception {
                        if (query.run().getCount() > 0)
                            view.markFavourite(true);
                        else
                            view.markFavourite(false);
                    }
                });

    }

    @Override
    public void listenToBus() {
        view.showLoading();
        SemsarApplication.getRxBus().getBehaviorSubject().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Object>() {
                                   Disposable disposable;

                                   @Override
                                   public void onSubscribe(@NonNull Disposable d) {
                                       // d.dispose();
                                       Timber.d("subscribed");
                                       disposable = d;
                                   }

                                   @Override
                                   public void onNext(@NonNull Object o) {
                                       if (o instanceof Apartment) {
                                           Apartment apartment = (Apartment) o;
                                           Timber.d("apartment  recieved from allApartmentsActivity or from profile act  date is --> " + apartment.getDate());
                                           processApartment(apartment);
                                           disposable.dispose();
                                       }
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       Timber.e(e);
                                       view.hideLoading();
                                       view.showError();


                                   }

                                   @Override
                                   public void onComplete() {
                                       Timber.d("onComplete");
                                   }
                               }
                );


    }


    private void processApartment(Apartment apartment) {
        //process includes 5 operations
        //1-increment views of current apartment
        incrementViews(apartment.getAppartmentId());
        //2- check if it's added to favourite or not ..if yes change icon to gold
        checkInFavouriteOrNot(apartment);
        //3-display apartment
        view.displayApartment(apartment);
        //4-display images
        view.initializeSlider(apartment.getmApartmentImages());
        //5-hide or show profile section in apartment based on the current user
        checkApartmentBelongsToUserOrNot(authenticationDataManager.getUserId(Util.KEY_USER_ID), apartment.getOwnerId());
        view.hideLoading();
    }

    @Override
    public void checkApartmentBelongsToUserOrNot(String savedUserId, String apartmentOwnerId) {
        Timber.d("owner id form data base--> " + apartmentOwnerId + "  current userId --> " + savedUserId);
        //I dont store ownerId in favourites so it will be null
        if (null == apartmentOwnerId || apartmentOwnerId == "")
            view.displayApartmentOwner(false);
        else if (savedUserId.equals(apartmentOwnerId))
            view.displayApartmentOwner(false);
        else
            view.displayApartmentOwner(true);

    }


}
