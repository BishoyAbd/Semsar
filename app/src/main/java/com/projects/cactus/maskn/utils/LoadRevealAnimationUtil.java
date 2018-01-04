package com.projects.cactus.maskn.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.projects.cactus.maskn.R;

import static android.view.View.VISIBLE;

/**
 * Created by el on 11/6/2017.
 */

public class LoadRevealAnimationUtil {

    private Button button;
    private Context context;
    private View viewReveal;
    private TextView textView;
    private ProgressBar progressBar;



    private void fadeOutTextAndShowProgressDialog() {

        getTextView().animate().alpha(0f)
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
        ValueAnimator anim = ValueAnimator.ofInt(getButton().getMeasuredWidth(), getFabWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = getButton().getLayoutParams();
                layoutParams.width = val;
                getButton().requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private int getFinalWidth(float dimen) {
        return (int) dimen;
    }

    private void revealButton() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getButton().setElevation(0f);


            getViewReveal().setVisibility(VISIBLE);

            int cx = getViewReveal().getWidth();
            int cy = getViewReveal().getHeight();


            int x = (int) (getFabWidth() / 2 + getButton().getX());
            int y = (int) (getFabWidth() / 2 + getButton().getY());

            float finalRadius = Math.max(cx, cy) * 1.2f;

            Animator reveal = ViewAnimationUtils
                    .createCircularReveal(getViewReveal(), x, y, getFabWidth(), finalRadius);

            reveal.setDuration(350);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    reset(animation);
//                finish();
                }

                private void reset(Animator animation) {
                    super.onAnimationEnd(animation);
                    getViewReveal().setVisibility(View.INVISIBLE);
                    getTextView().setVisibility(VISIBLE);
                    getTextView().setAlpha(1f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        getButton().setElevation(4f);
                    ViewGroup.LayoutParams layoutParams = getButton().getLayoutParams();
                    layoutParams.width = (int) (getContext().getResources().getDisplayMetrics().density * 300);
                    getButton().requestLayout();
                }
            });

            reveal.start();
        }
    }


    private void resetButtonToFirstState() {

        getViewReveal().setVisibility(View.INVISIBLE);
        getTextView().setVisibility(VISIBLE);
        getTextView().setAlpha(1f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getButton().setElevation(4f);
        ViewGroup.LayoutParams layoutParams = getButton().getLayoutParams();
        layoutParams.width = (int) (getContext().getResources().getDisplayMetrics().density * 300);
        getButton().requestLayout();
        fadeOutProgressDialog();
    }

    private int getFabWidth() {
        return (int) getContext().getResources().getDimension(R.dimen.fab_size);
    }

    private void fadeOutProgressDialog() {
        getProgressBar().animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        }).start();
    }


    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getViewReveal() {
        return viewReveal;
    }

    public void setViewReveal(View viewReveal) {
        this.viewReveal = viewReveal;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
