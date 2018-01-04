package com.projects.cactus.maskn.appartmentdetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.schedulers.Schedulers;
import prod.Injection;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ApartmentActivity extends AppCompatActivity implements ApartmentContract.View {

    @BindView(R.id.toolbar_inside_collapsing)
    Toolbar toolbar;
    @BindView(R.id.profile_photo)
    CircleImageView profilePhoto;
    @BindView(R.id.profileUserName_tv_)
    TextView profileUserName;
    @BindView(R.id.profileUserPhone_tv_)
    TextView profileUserPhone;
    @BindView(R.id.tv_price_ApartAct)
    TextView priceTv;
    @BindView(R.id.tv_city_ApartAct)
    TextView cityTv;
    @BindView(R.id.tv_numRooms_ApartAct)
    TextView numOfRoomsTv;
    @BindView(R.id.tv_address_ApartAct)
    TextView addressTv;
    @BindView(R.id.tv_desc_ApartAct)
    TextView descTv;
    @BindView(R.id.tv_date_apartAct)
    TextView dateTv;
    @BindView(R.id.progressIndicator_inApartAct)
    ProgressBar progressBar;
    @BindView(R.id.fab_call)
    FloatingActionButton fabCall;
    @BindView(R.id.slider_layout_id)
    SliderLayout mSlider;
    //cards to animate
    @BindViews({R.id.priceCardView, R.id.cityCardView, R.id.addressCardView, R.id.roomsCardView, R.id.descCardView, R.id.dateCardView})
    CardView[] detailsCardViews;

    @BindView(R.id.cardView_profileSectionInApartment)
    CardView cardView_profileSectionInApartment;

    private Menu menu;
    private ApartmentContract.Presenter presenter;
    private Apartment apartment;
    private SweetAlertDialog errorSweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartmet);
        ButterKnife.bind(this);
        init();
        presenter = new ApartmentPresenter(this, Injection.provideAuthenticationManager(getApplicationContext()), Injection.provideApartmentRepository(SemsarApplication.getContext()));
        //listen for incoming apartment from the sender activity
        presenter.listenToBus();
        //if it belongs to user then hide user info cardView
    }

    @OnClick(R.id.fab_call)
    void call(View view) {
        String phone = null;
        if (apartment != null)
            phone = apartment.getmPhoneNumber();
        if (phone != null)
            dialPhoneNumber(phone);
    }


    private void init() {
        setToolbar();
        initializeCollapsingToolBar();
        initDialogs();
    }

    private void initDrawer() {


    }

    private void initDialogs() {
        //for error dialog
        errorSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText(getResources().getString(R.string.error_general))
                .setTitleText(getString(R.string.error));
        errorSweetAlertDialog.setCancelable(false);
        errorSweetAlertDialog.setCanceledOnTouchOutside(false);
        errorSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                errorSweetAlertDialog.dismiss();
            }
        });
    }

    private void initializeCollapsingToolBar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("");
        AppBarLayout appBarLayout = findViewById(R.id.appbarLayoutWithSliding);
        appBarLayout.setExpanded(true);
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        //set main picture
        try {
            if (apartment != null)
                initializeSlider(apartment.getmApartmentImages());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        }
    }

    @Override
    public void initializeSlider(List<String> urls) {


//        Map<String, String> file_maps = new HashMap<>();

        for (String url : urls) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .image(url)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            mSlider.addSlider(textSliderView);
        }
        mSlider.setPresetTransformer(SliderLayout.Transformer.Tablet);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);

    }

    @Override
    public void showAds(String url) {
        ImageView imageView = findViewById(R.id.imageViewAdsInApartment);
        Picasso.with(this).load(url).
                into((imageView));
    }


    @Override
    public void setPresenter(ApartmentContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate();
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void showError() {
        if (!errorSweetAlertDialog.isShowing() && null != errorSweetAlertDialog)
            errorSweetAlertDialog.show();


    }

    @Override
    public void hideError() {
        if (errorSweetAlertDialog.isShowing() && errorSweetAlertDialog != null)
            errorSweetAlertDialog.dismiss();

    }

    @Override
    public void displayApartment(Apartment apartment) {

        this.apartment = apartment;
        //apartment details
        priceTv.setText(apartment.getPrice() + "");
        cityTv.setText(apartment.getCity());
        addressTv.setText(apartment.getAddress());
        numOfRoomsTv.setText(apartment.getRoomsNumber() + "");
        descTv.setText(apartment.getDescription());
        dateTv.setText(apartment.getDate().toString());

        animateDetails(detailsCardViews);


    }

    @Override
    public void displayApartmentOwner(boolean display) {

        if (display) {
            cardView_profileSectionInApartment.setVisibility(View.VISIBLE);
            fabCall.setVisibility(View.VISIBLE);
            //user section
            Picasso.with(this).load(apartment.getmOwnerProfilePhoto()).
                    fit().
                    into(profilePhoto);
            profileUserName.setText(apartment.getmOwnerName());
            profileUserPhone.setText(apartment.getmPhoneNumber());
        } else {
            cardView_profileSectionInApartment.setVisibility(View.GONE);
            fabCall.setVisibility(View.GONE);

        }
    }

    @Override
    public void openUserProfile() {

    }

    void animateDetails(CardView[] cards) {


        for (CardView card1 : cards) {
            card1.setVisibility(View.INVISIBLE);

        }

        for (CardView card : cards) {

            YoYo.with(Techniques.RollIn)
                    .duration(1200)
                    .playOn(card);
            card.setVisibility(View.VISIBLE);

        }

    }

    //to apply default font og calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void markFavourite(boolean b) {
        if (null != menu) {
            if (b)
                menu.findItem(R.id.action_add_toFavourite).setIcon(R.drawable.star_gold_24dp);
            else
                menu.findItem(R.id.action_add_toFavourite).setIcon(R.drawable.ic_star_border_24dp);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.single_apartment_menu, menu);

        toolbar.inflateMenu(R.menu.single_apartment_menu);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_toFavourite) {
            if (apartment != null)
                presenter.addToFavourite(apartment);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //unsubscribe to bus
        SemsarApplication.getRxBus().getBehaviorSubject().unsubscribeOn(Schedulers.newThread());

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // presenter.unsubscribe();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }
}
