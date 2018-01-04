package com.projects.cactus.maskn.search;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.projects.cactus.maskn.BuildConfig;
import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.appartments.AllApartmentsActivity;
import com.projects.cactus.maskn.favourite.FavouriteActivity;
import com.projects.cactus.maskn.navdrawer.InfoActivity;
import com.projects.cactus.maskn.utils.Util;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity implements SearchDialog.DialogCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_KEY_CITY = "city";
//    private static final String EXTRA_NUM_OF_ROOMS = "num_of_rooms";
//    private static final String EXTRA_PRICE_FROM = "price_from";
//    private static final String EXTRA_PRICE_TO = "price_to";
//    private static final String EXTRA_NUM_OF_BEDS = "num_of_beds";
//    private static final String EXTRA_NUM_OF_BATHS = "num_of_baths";

    CharSequence[] arrFrom1To12 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigationView)
    NavigationView nvDrawer;

    @BindView(R.id.btn_search)
    Button searchBtn;
    @BindView(R.id.rangeSeekbar_price)
    CrystalRangeSeekbar rangeSeekbar;
    @BindView(R.id.city_tv_in_search)
    TextView cityTv;
    @BindView(R.id.ET_numOfBeds)
    EditText numOfBedsEt;
    @BindView(R.id.ET_numOfBathRooms)
    EditText numOfBathsEt;
    @BindView(R.id.textView_from)
    TextView tvMinSeakBar;
    @BindView(R.id.textView_to)
    TextView tvMaxSeakBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.numOfRooms_tv_in_search)
    TextView numOfRoomsTv;

    DialogFragment searchDialog;
    //ads
    @BindView(R.id.adViewSearch)
    AdView mAdView;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    private String city;
    private int from = 200;
    private int to = 10000;
    private List<CharSequence> arrayNumOfRooms = Arrays.asList(arrFrom1To12);
    private List<CharSequence> arrayNumOfBaths = Arrays.asList(arrFrom1To12);
    private List<CharSequence> arrayNumOfBeds = Arrays.asList(arrFrom1To12);
    private int gender = 1;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupAds();
        setupToolbar();
        initDrawer();
        setUpSeekbar();
        initSpinners();
        disableInput(numOfRoomsTv);
        disableInput(cityTv);


    }

    private void setupAds() {

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_search_footer));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (BuildConfig.DEBUG)
            mAdView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
//                    Toast.makeText(getApplicationContext(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClosed() {
//                    Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
//                    Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLeftApplication() {
//                    Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
//                    Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        nvDrawer.setNavigationItemSelectedListener(this);
    }

    private void initSpinners() {


        //I was planning to add cities inside a spinner after I get the city list once the activity starts
        // but I decided to use dialog with listview and progress instead

        ArrayAdapter<String> genderSpinnerAdapter=new ArrayAdapter<String>(this,R.layout.spinner_gender_item,getResources().getStringArray(R.array.genderArray));
        genderSpinner.setAdapter(genderSpinnerAdapter);
        gender = 1;
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Timber.d("selected gender --> "+i );
                gender = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Timber.d("nothing selected");
            }
        });

    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            toolbarTitle.setText(R.string.app_name);

        }
//

    }


    @OnClick({R.id.ET_numOfBathRooms, R.id.ET_numOfBeds, R.id.ET_numOfRooms, R.id.et_city_in_search})
    void fillForm(View viwe) {
        switch (viwe.getId()) {
            case R.id.ET_numOfBathRooms:
                openBathsDialog();
                break;
            case R.id.ET_numOfBeds:
                openBedsDialog();
                break;
            case R.id.ET_numOfRooms:
                openRoomsDialog();
                break;
            case R.id.et_city_in_search:
                openCitiesDialog();
                break;

        }


    }

    private void openCitiesDialog() {
        searchDialog = SearchDialog.getInstance("hojhv hgl]dkm");
        searchDialog.show(getSupportFragmentManager(), SearchDialog.TAG);
    }

    private void openBathsDialog() {

        new MaterialDialog.Builder(this)
                .title(R.string.title_dialog_beds)
                .items(new String[]{"1", "2", "3", "4", "5", "6"})
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        arrayNumOfBaths = Arrays.asList(text);
                        if (arrayNumOfBaths.size() > 0)
                            numOfBathsEt.setText(appendResultFromDialog(which, text));

                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
    }


    private String appendResultFromDialog(Integer[] which, CharSequence[] text) {

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < which.length; i++) {

            str.append(text[i] + " او ");

        }


        return str.substring(0, str.length() - 3);
    }

    private void openBedsDialog() {

        new MaterialDialog.Builder(this)
                .title(R.string.title_dialog_beds)
                .items(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        arrayNumOfBeds = Arrays.asList(text);
                        if (arrayNumOfRooms.size() > 0)
                            numOfBedsEt.setText(appendResultFromDialog(which, text));
                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
    }


    private void openRoomsDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_dialog_rooms)
                .items(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"})
                .positiveColor(getResources().getColor(R.color.bg_login))
                .negativeColor(getResources().getColor(R.color.bg_login))
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                        arrayNumOfRooms = Arrays.asList(text);
                        if (arrayNumOfRooms.size() > 0)

                            numOfRoomsTv.setText(appendResultFromDialog(which, text));


                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
    }


    @OnClick(R.id.btn_search)
    void sendDataToOtherActivity() {

        if (null != city) {
            from = rangeSeekbar.getSelectedMinValue().intValue();
            to = rangeSeekbar.getSelectedMaxValue().intValue();
            Timber.d("search prefs" + "\ncity : " + city + "\nprice: from -> " + from + " to -> " + to +
                    "\nrooms : " + arrayNumOfRooms + "\nbeds : " + arrayNumOfBeds + "\narrayNumOfBaths : " + arrayNumOfBaths);
            Intent intent = new Intent(this, AllApartmentsActivity.class);
            intent.putExtra(EXTRA_KEY_CITY, city);
            SearchInput searchInput = new SearchInput(city, gender, from, to, arrayNumOfRooms, arrayNumOfBeds, arrayNumOfBaths);
            SemsarApplication.getRxBus().getBehaviorSubject().onNext(searchInput);
            startActivity(intent);
        } else
            showError(getString(R.string.youMustChoseCity));
    }


    private void showError(String s) {
        //show toast
        Snackbar.make(findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT).show();
    }


    void disableInput(TextView textView) {
        textView.setInputType(InputType.TYPE_NULL);
        textView.setTextIsSelectable(false);

//        editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                return true;  // Blocks input from hardware keyboards. //also disable hardware back button
//            }
//        });
    }

    void setUpSeekbar() {

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMinSeakBar.setText(String.valueOf(minValue));
                tvMaxSeakBar.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.clearSearch:
                clearSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void clearSearch() {
        cityTv.setText("");
        city = null;
        numOfRoomsTv.setText("");
        arrayNumOfRooms = Arrays.asList(arrFrom1To12);
        rangeSeekbar.setMaxValue(to).setMinValue(from).apply();
        tvMaxSeakBar.setText(getResources().getInteger(R.integer.price_max_value) + "");
        tvMinSeakBar.setText(getResources().getInteger(R.integer.price_min_value)+ "");


    }

    //to apply default font of calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //two methods of dialog cities callback
    @Override
    public void OnClickCancel() {
        searchDialog.dismiss();
    }

    @Override
    public void onClickList(String city) {
        this.city = city;
        cityTv.setText(" " + city + " ");
        searchDialog.dismiss();
    }


    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    //on menu of navigation click listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(this, InfoActivity.class);


        switch (item.getItemId()) {
            case R.id.nav_item_favourites:
                startActivity(new Intent(this, FavouriteActivity.class));
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_item_contact_us:
                intent.putExtra(Util.KEY_INFP_ACTIVITY, Util.CONTACT_US);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_item_report_problem:
                intent.putExtra(Util.KEY_INFP_ACTIVITY, Util.REPORT_PROBLEM);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_item_about_app:
                intent.putExtra(Util.KEY_INFP_ACTIVITY, Util.ABOUT_APP);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                break;
        }

        return true;
    }


}
