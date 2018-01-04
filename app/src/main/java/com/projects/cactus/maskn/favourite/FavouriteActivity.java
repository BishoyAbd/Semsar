package com.projects.cactus.maskn.favourite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.appartmentdetails.ApartmentActivity;
import com.projects.cactus.maskn.appartments.ApartmentRecyclerViewAdapter;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.utils.GridSpacingItemDecoration;
import com.projects.cactus.maskn.utils.RecyclerClickListener;
import com.projects.cactus.maskn.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import prod.Injection;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by bisho on 11/11/2017.
 */


/*
activity that contains all favourite apartments .. similar t allApartmentActivity
but I preferred to create another one  ... so lazy

 */
public class FavouriteActivity extends AppCompatActivity implements FavouriteContract.View {

    private static final String APARTMENT_ID_KEY = "apartment_key";

    @BindView(R.id.loadingView_block)
    LVBlock loadingView;
    @BindView(R.id.textViewError_allApartments)
    TextView errorTextView;
    @BindView(R.id.tryAgain_btn)
    Button refreshBtn;
    //recyclerView
    @BindView(R.id.recyclerView_result_allApartment_)
    RecyclerView apartmentRecyclerView;
    private List<Apartment> apartmentsList;
    private RecyclerView.LayoutManager layoutManager;
    private ApartmentRecyclerViewAdapter apartmentRecyclerAdapter;

    FavouriteContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ButterKnife.bind(this);
        initViews();
        presenter = new FavouritePresenter(this, Injection.provideApartmentRepository(this));
//        presenter.getAllFavouriteApartments();
    }


    void initViews() {
        //   setupToolBar();  dont need it
        prepareRecyclerView();

    }

    private void setupToolBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.favourite);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void setPresenter(FavouriteContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startAnim();
        //hide all views
        refreshBtn.setVisibility(View.GONE);
        apartmentRecyclerView.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        loadingView.setVisibility(View.GONE);
        apartmentRecyclerView.setVisibility(View.GONE);
        refreshBtn.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideError() {
        errorTextView.setVisibility(View.GONE);
        refreshBtn.setVisibility(View.GONE);
    }


    public void openApartmentDetails(String apartmentId) {

        Intent intent = new Intent(this, ApartmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(APARTMENT_ID_KEY, apartmentId);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void showNoResult() {
        Toast.makeText(this, getString(R.string.favourite_is_empty), Toast.LENGTH_SHORT).show();
    }


    private void prepareRecyclerView() {
        layoutManager = new GridLayoutManager(this, 2);
        apartmentRecyclerView.setLayoutManager(layoutManager);
        apartmentRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(this, 2, Util.dpToPx(this, 5), true));
        apartmentRecyclerView.setItemAnimator(new DefaultItemAnimator());

        apartmentRecyclerView.addOnItemTouchListener(
                new RecyclerClickListener(this, apartmentRecyclerView, new RecyclerClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Apartment apartment = apartmentsList.get(position);
                        openApartmentDetails(apartment);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // to be implement add to favourite
                    }
                })


        );
    }

    private void openApartmentDetails(Apartment apartment) {
        SemsarApplication.getRxBus().getBehaviorSubject().onNext(apartment);
        Intent intent = new Intent(this, ApartmentActivity.class);
        startActivity(intent);

    }

    //to apply default font og calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void ShowFavouriteApartments(List<Apartment> apartments) {
        apartmentsList = apartments;
        Timber.d("apartments from local database ---> " + apartments.toString());
        apartmentRecyclerView.setVisibility(View.VISIBLE);
        apartmentRecyclerAdapter = new ApartmentRecyclerViewAdapter(apartments, this);
        apartmentRecyclerView.setAdapter(apartmentRecyclerAdapter);

    }

    @Override
    public void showMessage(String s) {
        Snackbar.make(findViewById(R.id.content_favourite), s, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        //this is very important because whenver the user open a an apartment  and ark it as not favourite
        //then he goes back and the activity resumes  .. he will find it.. so here I update my adapter
        presenter.subscribe();
        super.onResume();
    }

    @Override
    protected void onPause() {
        presenter.unsubscribe();
        super.onPause();

    }
}
