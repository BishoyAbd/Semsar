package com.projects.cactus.maskn.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.projects.cactus.maskn.BuildConfig;
import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.authentication.model.User;
import com.projects.cactus.maskn.authentication.presenter.SignUpPresenter;
import com.projects.cactus.maskn.authentication.view.LoginContract;
import com.projects.cactus.maskn.authentication.view.SignUpContract;
import com.projects.cactus.maskn.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import prod.Injection;

/**
 * Created by el on 10/22/2017.
 */

public class SignupFragment extends Fragment implements SignUpContract.SignUpView, Validator.ValidationListener {


    interface SignUPCommunicator {
        void openProfileActivity(String userId);

        void openLogInFragment();
    }


    public SignupFragment() {

    }


    @NotEmpty(messageResId = R.string.please_file_in_form)
    @BindView(R.id.et_signup_userName)
    EditText userNameEt;

    @Pattern(regex = Util.regEx_mobile, messageResId = R.string.enter_valid_phone)
    @BindView(R.id.et_signup_phone)
    EditText phoneEt;

    @Password(messageResId = R.string.password_min_max_lenght_error, min = 8)
    @Length(max = 20,message = "")
    @BindView(R.id.et_signup_password)
    EditText passwordEt;

    @ConfirmPassword(messageResId = R.string.please_confirmPassword)
    @BindView(R.id.et_signup_confirmPassword)
    EditText confirmPassEt;

    @BindView(R.id.signUp_btn)
    Button signUpButton;

    @BindView(R.id.parent_signup)
    ConstraintLayout constraitLayoutParent;
    @BindView(R.id.tv_alreadyUser)
    TextView alreadyUserTv;

    SignUpPresenter signUpPresenter;
    private Validator validator;

    @BindView(R.id.loadingView_block)
    LVBlock loadingView;
    private View view;
    private SweetAlertDialog errorSweetAlertDialog;
    private SweetAlertDialog successSweetAlertDialog;


    private SignUPCommunicator communicator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        initViews();
        validator = new Validator(this);
        validator.setValidationListener(this);

        if (BuildConfig.DEBUG) {

            userNameEt.setText("eeee");
            phoneEt.setText("01228790902");
            passwordEt.setText("123456789");
            confirmPassEt.setText("123456789");

        }

        return view;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }


    @OnClick({R.id.tv_alreadyUser, R.id.signUp_btn})
    void onClick(View v) {
        if (v.getId() == R.id.signUp_btn)
            validator.validate();
        if (R.id.tv_alreadyUser == v.getId())
            communicator.openLogInFragment();
    }


    @Override
    public void onValidationSucceeded() {

        User user = new User();
        user.setName(userNameEt.getText().toString());
        user.setPhoneNumber(phoneEt.getText().toString());
        user.setPassword(passwordEt.getText().toString());
        signUpPresenter.signUp(user);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        communicator = (SignUPCommunicator) getActivity();

//        if (signUpPresenter != null) {
//            signUpPresenter.unsubscribe();
//            signUpPresenter = null;
//        }
    }

    @Override
    public void onPause() {
        signUpPresenter.unsubscribe();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        communicator = (SignUPCommunicator) getActivity();
        signUpPresenter = new SignUpPresenter(this, Injection.provideAuthenticationManager(getActivity()));
    }


    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        signUpPresenter.subscribe();

    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startAnim();
        enableInput(false);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.INVISIBLE);
        enableInput(true);
    }

    void enableInput(boolean enabled) {
        userNameEt.setEnabled(enabled);
        phoneEt.setEnabled(enabled);
        passwordEt.setEnabled(enabled);
        confirmPassEt.setEnabled(enabled);
        signUpButton.setEnabled(enabled);
        signUpButton.setActivated(enabled);
        alreadyUserTv.setEnabled(enabled);


//        if (enabled){
//            constraitLayoutParent.setAlpha(1f);
//
//        }
//        else
//            constraitLayoutParent.setAlpha(.45f);

    }

    void initViews() {


        //for error dialog
        errorSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

        //for success dialog
        successSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setContentText(getResources().getString(R.string.success)).setTitleText("");
        successSweetAlertDialog.setCancelable(false);
        successSweetAlertDialog.setCanceledOnTouchOutside(false);
//        successSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                successSweetAlertDialog.dismiss();
//            }
//        });

    }


    @Override
    public void onSignUpSuccess(final User user) {


        if (!successSweetAlertDialog.isShowing())
            successSweetAlertDialog.show();
        successSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                // communicator.openProfileActivity(user.getUnique_id());
                communicator.openLogInFragment();

            }
        });
    }

    @Override
    public void showMessage(String message) {

        if (!errorSweetAlertDialog.isShowing() && errorSweetAlertDialog != null) {
            errorSweetAlertDialog.setContentText(message);
            errorSweetAlertDialog.show();
        }
    }

    @Override
    public void showError() {
        if (!errorSweetAlertDialog.isShowing() && null != errorSweetAlertDialog) {
            errorSweetAlertDialog.setContentText(getResources().getString(R.string.error_general));
            errorSweetAlertDialog.show();
        }

    }

    @Override
    public void hideError() {
        if (errorSweetAlertDialog.isShowing() && errorSweetAlertDialog != null)
            errorSweetAlertDialog.dismiss();

    }


}
