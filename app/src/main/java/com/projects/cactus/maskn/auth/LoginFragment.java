package com.projects.cactus.maskn.auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.projects.cactus.maskn.BuildConfig;
import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.authentication.model.AuthenticationDataManager;
import com.projects.cactus.maskn.authentication.model.User;
import com.projects.cactus.maskn.authentication.presenter.LoginPresenter;
import com.projects.cactus.maskn.authentication.view.LoginContract;
import com.projects.cactus.maskn.utils.CalligraphyDefaultTextInputLayout;
import com.projects.cactus.maskn.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import prod.Injection;

import static android.view.View.VISIBLE;
import static com.projects.cactus.maskn.utils.Util.KEY_USER_ID;

/**
 * Created by el on 10/22/2017.
 */

public class LoginFragment extends Fragment implements LoginContract.LoginView, Validator.ValidationListener {

    @BindView(R.id.parent_login)
    RelativeLayout relativeLayoutParent;
    @BindView(R.id.loginBtnFram)
    FrameLayout button;

    @BindView(R.id.tv_newMember)
    TextView tv_newMember;
    @BindView(R.id.textViewLoginBtn)
    TextView textViewLogin;
    @BindView(R.id.progressInLoginBtn)
    ProgressBar progressBar;
    @BindView(R.id.reveal)
    View viewReveal;
    @Pattern(regex = Util.regEx_mobile, messageResId = R.string.enter_valid_phone, sequence = 1)
    @BindView(R.id.et_login_phone)
    EditText phoneEt;

    @Password(min = 8, messageResId = R.string.error_wrong_password, sequence = 2)
    @BindView(R.id.et_login_password)
    EditText passwordEt;
    //    @BindView(R.id.login_btn)
    //    Button loginButton;
    @BindView(R.id.loadingView_block)
    LVBlock loadingView;

    @Pattern(regex = Util.regEx_mobile, messageResId = R.string.enter_valid_phone, sequence = 1)
    @BindView(R.id.till_phone)
    CalligraphyDefaultTextInputLayout tillPhone;

    @Password(min = 8, messageResId = R.string.error_wrong_password, sequence = 2)
    @BindView(R.id.till_pass)
    CalligraphyDefaultTextInputLayout tillPass;
    private View view;
    private Validator validator;
    private LoginPresenter loginPresenter;
    private SweetAlertDialog errorSweetAlertDialog;
    private SweetAlertDialog successSweetAlertDialog;
    private LoginCommunicator communicator;
    private boolean alreadyReset = false; //to keep track of animated button width

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValidator();

    }


    private void initValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(CalligraphyDefaultTextInputLayout.class, new TextInputLayoutAdapter());
        validator.setViewValidatedAction(new Validator.ViewValidatedAction() {
            @Override
            public void onAllRulesPassed(View view) {
                if (view instanceof TextInputLayout) {
                    ((TextInputLayout) view).setError("");
                    ((TextInputLayout) view).setErrorEnabled(false);

                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_revealed, container, false);
        ButterKnife.bind(this, view);
        initViews();


        if (BuildConfig.DEBUG) {
            passwordEt.setText("123456789");
            phoneEt.setText("01228790902");

        }
        return view;
    }

    @OnClick({R.id.tv_newMember, R.id.loginBtnFram})
    void onClick(View v) {
        if (v.getId() == R.id.loginBtnFram)
            validator.validate();
        if (R.id.tv_newMember == v.getId())
            communicator.openSignUpFragment();
    }

    @Override
    public void onValidationSucceeded() {
        loginPresenter.login(phoneEt.getText().toString(), passwordEt.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof TextInputLayout) {
//                ((EditText) view).setError(message);
                ((TextInputLayout) view).setError(message);
                ((TextInputLayout) view).setErrorEnabled(true);

//                Snackbar.make(this.view.findViewById(R.id.parent_login),message,Snackbar.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        loginPresenter.unsubscribe();
    }

    @Override
    public void onStart() {
        super.onStart();
        communicator = (LoginCommunicator) getActivity();
        loginPresenter = new LoginPresenter(this, Injection.provideAuthenticationManager(getActivity()));
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        loginPresenter.subscribe();
    }

    @Override
    public void showLoading() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateButtonWidth();
            fadeOutTextAndShowProgressDialog();
            alreadyReset = false; //button is not in it's original state
        } else {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.startAnim();
        }

    }

    @Override
    public void hideLoading() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resetButtonToFirstState();
        } else {
            loadingView.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void enableInput(boolean enabled) {
        phoneEt.setEnabled(enabled);
        passwordEt.setEnabled(enabled);
        button.setEnabled(enabled);
        tv_newMember.setEnabled(enabled);
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
                .setContentText(getResources().getString(R.string.success)).setTitleText(getString(R.string.success_title));
        successSweetAlertDialog.setCancelable(false);
        successSweetAlertDialog.setCanceledOnTouchOutside(false);


    }

    @Override
    public void onLoginSuccess(final User user) {

        loginPresenter.keepMeLoggedIn(AuthenticationDataManager.KEY_LOG_SATE
                , KEY_USER_ID, user.getUnique_id());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealButton();
            fadeOutProgressDialog();
            delayedStartNextActivity(user.getUnique_id());
        } else {

            //for pre lolipop
            if (!successSweetAlertDialog.isShowing()) {
                successSweetAlertDialog.show();

                successSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        communicator.openProfileActivity(user.getUnique_id());
                    }
                });
            }
        }


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

    private void fadeOutTextAndShowProgressDialog() {

        textViewLogin.animate().alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showProgressDialog();
                    }
                })
                .start();
    }

    private void showProgressDialog() {
        progressBar.setAlpha(1f);
        progressBar
                .getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(VISIBLE);
    }


    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(button.getMeasuredWidth(), getFabWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.width = val;
                button.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private int getFinalWidth() {
        return (int) getResources().getDimension(R.dimen.fabSmall);
    }

    private void revealButton() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setElevation(0f);
            viewReveal.setVisibility(VISIBLE);

            int cx = viewReveal.getWidth();
            int cy = viewReveal.getHeight();


            int x = (int) (getFabWidth() / 2 + button.getX());
            int y = (int) (getFabWidth() / 2 + button.getY());

            float finalRadius = Math.max(cx, cy) * 1.2f;

            Animator reveal = ViewAnimationUtils
                    .createCircularReveal(viewReveal, x, y, getFabWidth(), finalRadius);

            reveal.setDuration(350);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    reset(animation);
//                finish();
                }

                private void reset(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (alreadyReset)
                        return;
                    resetButtonToFirstState();

                }
            });

            reveal.start();
        }
    }


    private void resetButtonToFirstState() {
        if (alreadyReset)
            return;
        alreadyReset = true;
        viewReveal.setVisibility(View.INVISIBLE);
        textViewLogin.setVisibility(VISIBLE);
        textViewLogin.setAlpha(1f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setElevation(4f);
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
            layoutParams.width = (int) (getResources().getDisplayMetrics().density * 300);
            button.requestLayout();
            fadeOutProgressDialog();
        }
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }

    private void fadeOutProgressDialog() {
        progressBar.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        }).start();
    }


    private void delayedStartNextActivity(final String userId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                communicator.openProfileActivity(userId);
            }
        }, 20);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    interface LoginCommunicator {
        void openProfileActivity(String userId);

        void openSignUpFragment();
    }


    //custom adapter used by validator
    public class TextInputLayoutAdapter implements ViewDataAdapter<CalligraphyDefaultTextInputLayout, String> {

        @Override
        public String getData(final CalligraphyDefaultTextInputLayout til) {
            return getText(til);
        }

        private String getText(CalligraphyDefaultTextInputLayout til) {
            return til.getEditText().getText().toString();
        }
    }

}
