package com.projects.cactus.maskn.appartments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.search.SearchActivity;
import com.projects.cactus.maskn.utils.GridSpacingItemDecoration;
import com.projects.cactus.maskn.utils.RecyclerClickListener;
import com.projects.cactus.maskn.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import prod.Injection;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class AllApartmentsActivity extends AppCompatActivity implements SearchContract.View {

    //
//    private static final String EXTRA_KEY_CITY = "city";
//    private static final String EXTRA_NUM_OF_ROOMS = "num_of_rooms";
//    private static final String EXTRA_PRICE_FROM = "price_from";
//    private static final String EXTRA_PRICE_TO = "price_to";
    private static final String APARTMENT_ID_KEY = "apartment_key";
//    private static final String EXTRA_SEARCH_INPUT = "search_input";


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
    private ApartmentRecyclerViewAdapter apartmentRecyclerAdapter;

    private SearchContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apartments);
        ButterKnife.bind(this);
        initViews();

        presenter = new SearchManager(this, Injection.provideApartmentRepository(getApplicationContext()));
        presenter.listenToBus(); //listen for incoming apartment

    }


    private void initViews() {
        prepareRecyclerView();
        setupLoadingView();
        setupToolBar();
    }

    private void setupToolBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    @OnClick(R.id.tryAgain_btn)
    void refresh() {
        presenter.onClickRefresh();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = presenter;
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

    @Override
    public void showAllApartmnets(List<Apartment> apartments) {
        apartmentsList = apartments;
        Timber.d("apartments from database ---> " + apartments.toString());
        apartmentRecyclerView.setVisibility(View.VISIBLE);
        apartmentRecyclerAdapter = new ApartmentRecyclerViewAdapter(apartments, this);
        apartmentRecyclerView.setAdapter(apartmentRecyclerAdapter);


    }

    @Override
    public void navigateToSearchActivity(String city, int numOfRooms) {
        startActivity(new Intent(this, SearchActivity.class));

    }

    @Override
    public void openApartmentDetails(String apartmentId) {

        Intent intent = new Intent(AllApartmentsActivity.this, ApartmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(APARTMENT_ID_KEY, apartmentId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showAddApartment() {


    }

    public void setTitle(String title){
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    @Override
    public void showUserIsNotLoggedInDialog() {
        Toast.makeText(this, "please login first", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoResult() {

        Snackbar.make(findViewById(android.R.id.content), R.string.noResultMatches, Snackbar.LENGTH_SHORT).show();
    }


    private void prepareRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        apartmentRecyclerView.setLayoutManager(layoutManager);
        apartmentRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(AllApartmentsActivity.this, 2, Util.dpToPx(AllApartmentsActivity.this, 5), true));
        apartmentRecyclerView.setItemAnimator(new DefaultItemAnimator());

        apartmentRecyclerView.addOnItemTouchListener(
                new RecyclerClickListener(AllApartmentsActivity.this, apartmentRecyclerView, new RecyclerClickListener.OnItemClickListener() {
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
        Intent intent = new Intent(AllApartmentsActivity.this, ApartmentActivity.class);
        startActivity(intent);

    }

    //to apply default font og calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        presenter.unsubscribe();
        super.onPause();

    }


    private void setupLoadingView() {
//       loadingView.setViewColor(int color)
//        isShadow(boolean boolean show)
//        setShadowColor(int color)
//        startAnim(int time)
//        stopAnim()

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
