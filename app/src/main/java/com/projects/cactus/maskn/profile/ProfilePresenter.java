package com.projects.cactus.maskn.profile;

import com.projects.cactus.maskn.authentication.model.AuthenticationDataManager;
import com.projects.cactus.maskn.authentication.model.User;
import com.projects.cactus.maskn.data.ApartmentRepository;
import com.projects.cactus.maskn.data.DataSource;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by el on 10/13/2017.
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View profileView;
    private AuthenticationDataManager authenticationDataManager;
    private CompositeDisposable compositeDisposable;
    private DataSource mApartmentRepository;

    public ProfilePresenter(ProfileContract.View profileView, ApartmentRepository mApartmentRepository, AuthenticationDataManager authenticationDataManager) {
        this.profileView = profileView;
        this.authenticationDataManager = authenticationDataManager;
        this.mApartmentRepository = mApartmentRepository;
        compositeDisposable = new CompositeDisposable();


    }

    @Override
    public void getUserInfo(String useId) {

        profileView.showLoading();
        compositeDisposable.clear();
        Disposable disposable = authenticationDataManager.getUserInfo(useId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {

                    @Override
                    public void onSuccess(@NonNull User user) {
                        profileView.hideLoading();
                        profileView.showUserInfo(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        profileView.showError();
                        profileView.hideLoading();
                    }
                });

        compositeDisposable.add(disposable);



    }

    //could place both function in one func and call it getData()


    @Override
    public void getApartmentsList(String userId) {


        Disposable disposal =
                mApartmentRepository.getUserApartment(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Apartment>>() {

                            @Override
                            public void onNext(@NonNull List<Apartment> apartments) {
                                profileView.showUserApartmentList(apartments);
                                Timber.d("user apartments ---> "+apartments.toString());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Timber.e(e);
                            }

                            @Override
                            public void onComplete() {
                                Timber.i("onComplete is called");
                            }
                        });


        compositeDisposable.add(disposal);
    }

    @Override
    public void logOut() {
        authenticationDataManager.logOut(AuthenticationDataManager.KEY_LOG_SATE);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
//        profileView = null;
//        authenticationDataManager = null;
//        mApartmentRepository = null;
        compositeDisposable.clear();
    }



}
