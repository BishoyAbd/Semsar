package com.projects.cactus.maskn.appartments;

import android.support.annotation.NonNull;

import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.data.ApartmentRepository;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.search.SearchInput;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by el on 10/8/2017.
 */

//preseneter that will control allApartmentActivity
public class SearchManager implements SearchContract.Presenter {


    @NonNull
    private SearchContract.View view;
    @NonNull
    private ApartmentRepository mApartmentRepository;

    @NonNull
    private CompositeDisposable mSubscriptions;
    private SearchInput searchInput;

    public SearchManager(SearchContract.View view, ApartmentRepository repository) {
        checkNotNull(view, "SearchContract.View cant be null");
        this.view = view;
        checkNotNull(repository, "ApartmentRepository cant be null");
        this.mApartmentRepository = repository;
        mSubscriptions = new CompositeDisposable();
        view.setPresenter(this);
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Deprecated
    public void search(String city, int numOfRooms, int priceFrom, int priceTo) {
        view.showLoading();

        mSubscriptions.clear();
        Disposable disposable = mApartmentRepository.getApartments(city, numOfRooms, priceFrom, priceTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<List<Apartment>>() {
                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull List<Apartment> apartments) {
                                processApartments(apartments);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                view.hideLoading();
                                view.showError();
                            }

                            @Override
                            public void onComplete() {
                                view.hideError();
                                view.hideLoading();
                            }
                        }

                );


//      Disposable disposable= mApartmentRepository.getApartments(city, numOfRooms, priceFrom, priceTo)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//
//                        //on next
//                        this::processApartments
//                        ,
//                        //on error
//                        throwable -> {
//                            view.hideLoading();
//                            view.showError();
//
//                        },
//                        //onComplete
//                        () -> {
//                            view.hideError();
//                            view.hideLoading();
//                        }
//                );
        mSubscriptions.add(disposable);


    }

    private void processApartments(List<Apartment> apartments) {
        view.hideLoading();
        view.hideError();
        if (apartments.isEmpty())
            view.showNoResult();
        else
            view.showAllApartmnets(apartments);
    }

    @Override
    public void search(SearchInput searchInput) {
        view.setTitle(searchInput.getCity());
        view.showLoading();
        mSubscriptions.clear();
        Disposable disposable = mApartmentRepository.getApartments(searchInput)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<List<Apartment>>() {
                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull List<Apartment> apartments) {
                                processApartments(apartments);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                view.hideLoading();
                                view.showError();
                            }

                            @Override
                            public void onComplete() {
                                view.hideError();
                                view.hideLoading();
                            }
                        }

                );

        mSubscriptions.add(disposable);
    }

    @Override
    public void listenToBus() {

        SemsarApplication.getRxBus().getBehaviorSubject().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<Object>() {
                                   Disposable disposable;

                                   @Override
                                   public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                                       // d.dispose();
                                       Timber.d("subscribed");
                                       disposable = d;
                                   }

                                   @Override
                                   public void onNext(@io.reactivex.annotations.NonNull Object o) {

                                       if (o instanceof SearchInput) {

                                           searchInput = (SearchInput) o;
                                           search(searchInput);
                                           Timber.i("search prefs" + "\ncity : " + searchInput.getCity() +
                                                   "\ngender : " + searchInput.getGender()
                                                   + "\nprice: from -> " + searchInput.getFrom() +
                                                   " to -> " + searchInput.getTo() +
                                                   "\nrooms : " + searchInput.getArrayNumOfRooms() +
                                                   "\nbeds : " + searchInput.getArrayNumOfBeds() +
                                                   "\narrayNumOfBaths : " + searchInput.getArrayNumOfBaths());

                                           disposable.dispose();
                                       }

                                   }

                                   @Override
                                   public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                       Timber.e(e);
                                   }

                                   @Override
                                   public void onComplete() {
                                       Timber.d("onComplete");
                                   }
                               }
                );


    }

    @Override
    public void onClickRefresh() {
        if (searchInput != null)
            search(searchInput);
    }

}
