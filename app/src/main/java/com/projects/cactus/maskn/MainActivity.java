package com.projects.cactus.maskn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.projects.cactus.maskn.addapartment.AddApartmentActivity;
import com.projects.cactus.maskn.auth.AuthActivity;
import com.projects.cactus.maskn.search.SearchActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/*
contains two button to navigate user either to addNewApartmentActivity or searchActivity
if the user is not logged in he will be navigated to authACtivity to decide to sign up or log in
 */
public class MainActivity extends AppCompatActivity {

    private boolean isLogedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Timber.d("isLogedin --> " + isLogedIn);
    }


    @OnClick(R.id.openSearchDialog)
    void openSearch() {
        startActivity(new Intent(this, SearchActivity.class));

    }

    @OnClick(R.id.addApartment_mainActBtn)
    void process() {
        if (!isLogedIn)
            startActivity(new Intent(this, AuthActivity.class)); //will display signup fragment first
        else
            startActivity(new Intent(this, AddApartmentActivity.class));


    }

    //to apply default font og calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
