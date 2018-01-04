package com.projects.cactus.maskn.navdrawer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.projects.cactus.maskn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bisho on 11/18/2017.
 */

public class ContactUsFragment extends Fragment {


    public static final String TAG = "contact_us_fragment";
    @BindView(R.id.image_view_call)
    ImageView imageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_us_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
