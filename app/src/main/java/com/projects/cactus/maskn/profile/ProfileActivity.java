package com.projects.cactus.maskn.profile;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.addapartment.AddApartmentActivity;
import com.projects.cactus.maskn.appartmentdetails.ApartmentActivity;
import com.projects.cactus.maskn.appartments.ApartmentRecyclerViewAdapter;
import com.projects.cactus.maskn.authentication.model.User;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.utils.GridSpacingItemDecoration;
import com.projects.cactus.maskn.utils.RecyclerClickListener;
import com.projects.cactus.maskn.utils.Util;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import prod.Injection;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.projects.cactus.maskn.SemsarApplication.getContext;


public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {


//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.coordiantorProfile)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.profileUserName_tv_)
    TextView textViewUserName;
    @BindView(R.id.profileUserPhone_tv_)
    TextView textViewUserPhone;
    @BindView(R.id.recyclerview_inProfile)
    RecyclerView apartmentRecyclerView;

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.textViewError)
    TextView textViewError;

    private List<Apartment> apartmentsList;
    private ProfilePresenter profilePresenter;
    private ApartmentRecyclerViewAdapter adapter;

    private String userId;
    private Animator spruceAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        prepareRecyclerView();

        userId = getIntent().getStringExtra(Util.KEY_USER_ID);
        profilePresenter = new ProfilePresenter(this, Injection.provideApartmentRepository(getContext()), Injection.provideAuthenticationManager(getContext()));

        Timber.d("user id received from authAct ---> " + userId);
        if (null != userId && Util.NO_USER_ID != userId) {
            profilePresenter.getUserInfo(userId);
            profilePresenter.getApartmentsList(userId);

        }

        setupSwipeRefresh();
    }


    void setupSwipeRefresh() {
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                profilePresenter.getApartmentsList(userId);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

    }

    @OnClick(R.id.textViewError)
    public void onClick() {

    }

    //to apply default font og calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void prepareRecyclerView() {
        apartmentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
//                initSpruce();
            }
        };

        apartmentRecyclerView.setLayoutManager(linearLayoutManager);
        apartmentRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(this, 2, Util.dpToPx(this, 5), true));
        apartmentRecyclerView.setItemAnimator(new DefaultItemAnimator());

        apartmentRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this,
                apartmentRecyclerView, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Apartment apartment = apartmentsList.get(position);
                openApartmentActivity(apartment);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                // to be implement add to favourite
            }
        }));


    }


    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void showError() {

        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_general), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.YELLOW)
                .setAction(getString(R.string.load), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (!swipeRefreshLayout.isRefreshing())
                        profilePresenter.getUserInfo(userId);
                        profilePresenter.getApartmentsList(userId);
                    }
                }).show();


    }

    @Override
    public void hideError() {
    }

    @Override
    public void showUserInfo(User user) {
        textViewUserName.setText(user.getName());
        textViewUserPhone.setText(user.getPhone_number());

    }

    @Override
    public void EditProfile(boolean isAuthorized) {

    }

    @Override
    public void NavigateToAddApartmentActivity() {

    }

    @Override
    public void showUserApartmentList(List<Apartment> apartments) {
        apartmentsList = apartments;
        adapter = new ApartmentRecyclerViewAdapter(apartments, this);
        apartmentRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openApartmentActivity(String apartmentId) {

    }

    @Override
    public void openApartmentActivity(Apartment apartment) {
        SemsarApplication.getRxBus().getBehaviorSubject().onNext(apartment);
        startActivity(new Intent(this, ApartmentActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                profilePresenter.logOut();
                finish();
                return true;
            case R.id.action_add_apartment:
                openAddApartmentActivity();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onPause() {
        profilePresenter.unsubscribe();
        super.onPause();
    }

    private void openAddApartmentActivity() {
        startActivity(new Intent(this, AddApartmentActivity.class).putExtra(Util.KEY_USER_ID, userId));
    }

    private void initSpruce() {
        spruceAnimator = new Spruce.SpruceBuilder(apartmentRecyclerView)
                .sortWith(new DefaultSort(100))
                .animateWith(DefaultAnimations.shrinkAnimator(apartmentRecyclerView, 800),
                        ObjectAnimator.ofFloat(apartmentRecyclerView, "translationX", -apartmentRecyclerView.getWidth(), 0f).setDuration(800))
                .start();
    }

}
