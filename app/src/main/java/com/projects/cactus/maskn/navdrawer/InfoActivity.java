package com.projects.cactus.maskn.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.utils.Util;

/**
 * Created by bisho on 11/18/2017.
 */

public class InfoActivity extends AppCompatActivity {


    private String which = null;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");


        which = getIntent().getStringExtra(Util.KEY_INFP_ACTIVITY);
        switch (which) {

            case Util.CONTACT_US:
                startFragment(new ContactUsFragment(), ContactUsFragment.TAG);
                break;

            case Util.REPORT_PROBLEM:
                startFragment(new ReportProblemFragment(), ReportProblemFragment.TAG);
                break;

            case Util.ABOUT_APP:
                startFragment(new AboutAppFragment(), AboutAppFragment.TAG);
                break;

        }


    }


    void startFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.left_out)
                .replace(android.R.id.content, fragment, tag)
                .commit();

    }

    @Override
    public boolean onNavigateUp() {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

