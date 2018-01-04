package com.projects.cactus.maskn.navdrawer;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.data.apiservies.ApartmentsApi;
import com.projects.cactus.maskn.data.apiservies.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bisho on 11/18/2017.
 */

public class ReportProblemFragment extends Fragment implements BaseView {


    @BindView(R.id.content)
    RelativeLayout relativeLayout;
    @BindView(R.id.reportProblem_btn)
    Button reportProblem_btn;
    @BindView(R.id.reportProblem_et)
    EditText reportProblem_et;

    @BindView(R.id.loadingView_block)
    LVBlock lvBlock;
    public static final String TAG = "report_problem_fragment";
    private String problem;
    private Disposable disposable;

    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.report_problem_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.reportProblem_btn)
    void reportProblem(View view) {
        problem = reportProblem_et.getText().toString();
        if (null == problem || problem.trim().equals(""))
            return;

        showLoading();
        enableInput(false);
        disposable = ServiceGenerator.createService(ApartmentsApi.class)
                .reportProblem(problem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {


                    @Override
                    public void onComplete() {
                        hideLoading();
                        enableInput(true);
                        showMessage(getString(R.string.thanks_for_reporting_problem));
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        showError();
                        enableInput(true);

                    }


                });


    }

    private void showMessage(String m) {
//        Snackbar.make(relativeLayout, m, Snackbar.LENGTH_LONG).show();
        Toast.makeText(getActivity(), m, Toast.LENGTH_SHORT).show();

    }


    void enableInput(boolean enable) {
        reportProblem_btn.setEnabled(enable);
        reportProblem_et.setEnabled(enable);

    }

    //not needed
    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void showLoading() {
        lvBlock.setVisibility(View.VISIBLE);
        lvBlock.startAnim();
    }

    @Override
    public void hideLoading() {
        lvBlock.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        Snackbar.make(relativeLayout, "يوجد مشكلة ف الشبكة", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void hideError() {

    }

    @Override
    public void onPause() {
        if (disposable != null)
            if (!disposable.isDisposed())
                disposable.dispose();
        super.onPause();
    }


}
