package com.projects.cactus.maskn.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.authentication.presenter.AuthActivityPresenter;
import com.projects.cactus.maskn.authentication.view.AuthActivityContract;
import com.projects.cactus.maskn.profile.ProfileActivity;
import com.projects.cactus.maskn.utils.Util;

import prod.Injection;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.projects.cactus.maskn.utils.Util.KEY_USER_ID;

/**
 * Created by el on 10/22/2017.
 */

public class AuthActivity extends AppCompatActivity implements AuthActivityContract.View, SignupFragment.SignUPCommunicator, LoginFragment.LoginCommunicator {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private AuthActivityPresenter authActivityPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authActivityPresenter = new AuthActivityPresenter(this,Injection.provideAuthenticationManager(SemsarApplication.getContext()));
        authActivityPresenter.checkLogedIn();

    }


    //to apply default font og calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    // an option is to do nothing
    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment SignUp_Fragment = getSupportFragmentManager()
                .findFragmentByTag(Util.SignUp_Fragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task
        if (SignUp_Fragment != null) //if it's exist replace it
            replaceLoginFragment();

        else
            super.onBackPressed();

    }


    @Override
    protected void onStop() {
        super.onStop();
        authActivityPresenter.unsubscribe();
    }

    @Override
    public void replaceLoginFragment() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_enter, R.anim.right_out)
                .replace(R.id.frag_container, new LoginFragment(), Util.Login_Fragment);
        fragmentTransaction.commit();

    }


    void replaceSignUpFragment() {

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.left_enter, R.anim.left_out).
                replace(R.id.frag_container, new SignupFragment(), Util.SignUp_Fragment);

        fragmentTransaction.commit();
    }


    @Override
    public void openProfileActivity(String userId) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        startActivity(intent);
        finish();
    }

    @Override
    public void openSignUpFragment() {
        replaceSignUpFragment();
    }

    @Override
    public void openLogInFragment() {
        replaceLoginFragment();
    }

    @Override
    public void setPresenter(AuthActivityContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void hideError() {

    }
}
