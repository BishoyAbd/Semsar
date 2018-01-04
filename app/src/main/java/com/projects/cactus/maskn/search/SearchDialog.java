package com.projects.cactus.maskn.search;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.data.apiservies.ApartmentsApi;
import com.projects.cactus.maskn.data.apiservies.ServiceGenerator;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by el on 10/27/2017.
 */

public class SearchDialog extends DialogFragment {


    private static final String KEY_TITLE = "title";
    private static final String EXTRA_KEY_CITY = "city";
    public static String TAG = "cities_dialog";
    @BindView(R.id.btnCancelCitiesDialog)
    Button cancelBtn;
    @BindView(R.id.cityListViewDialog)
    ListView citiesListView;
    DialogCallback dialogCallback;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.textViewError)
    TextView textViewError;

    private Disposable disposable;


    private List<String> cities = Collections.emptyList();
    private String city;
    private ArrayAdapter<String> arrayAdapter;

    static public DialogFragment getInstance(String title) {

        DialogFragment dialog = new SearchDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        dialog.setArguments(bundle);
        return dialog;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cities, container, false);
        ButterKnife.bind(this, view);
        updateCitiesList();
        citiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                city = cities.get(i);
                dialogCallback.onClickList(city);

            }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCallback.OnClickCancel();
            }
        });
        return view;
    }


    @OnClick(R.id.textViewError)
    void onClick() {
        updateCitiesList();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialogCallback = (DialogCallback) getActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
        dialogCallback = null;
        disposable.dispose();
        Timber.d("onStop called");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialogFragment = super.onCreateDialog(savedInstanceState);
        dialogFragment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialogFragment;

    }

    private void showError(boolean show) {
        if (show)
            textViewError.setVisibility(View.VISIBLE);
        else
            textViewError.setVisibility(View.INVISIBLE);

    }


    private void updateCitiesList() {
        showLoading(true);
        showError(false);
        ApartmentsApi apartmentsApi = ServiceGenerator.createService(ApartmentsApi.class);
        disposable = apartmentsApi.getCities().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {


                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<String> citiesList) {
                        citiesListView.setVisibility(View.VISIBLE);
                        showLoading(false);
                        showError(false);
                        cities = citiesList;
                        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, citiesList);
                        citiesListView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        showLoading(false);
                        showError(true);
                        citiesListView.setVisibility(View.INVISIBLE);
                        Timber.d(e);

                    }
                });


    }


    private void showLoading(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.animate();
        } else
            mProgressBar.setVisibility(View.GONE);

    }


    public interface DialogCallback {

        void OnClickCancel();

        void onClickList(String city);

    }


}
