package com.projects.cactus.maskn.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by el on 1/22/2017.
 */

public class Util {


    public static final String KEY_LOGGED_IN = "logged_in";
    public static final String KEY_USER_ID = "user_id";
    public static final String NO_USER_ID = "no_user";
    public static final String QUESTION_ID_EXRA ="question_id" ;
    public static final String ERROR_ID_INVALIDE = "0000";
    public static final String MESSAGE_APARTMENT_ADDED_SUCCESSFULLY ="uploaded_successfully";
    public static final String CONTACT_US ="contact_us" ;
    public static final String KEY_INFP_ACTIVITY = "info_activity";
    public static final String ADV_WITH_US = "adv_with_us";
    public static final String ABOUT_APP="about_app";
    public static final String REPORT_PROBLEM = "report_problem";

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void changeTabsFont(Context context,TabLayout tabLayout) {

        Typeface typeface= FontCache.getTypeFace(context,"arabict.otf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }



    //Email an Mobile Validation pattern
    //public static final String regEx = "\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}\b";
    public static final String regEx_mobile = "^01[0-2]{1}[0-9]{8}$";
    public static final String regEx_email =  "^[a-zA-Z0-9._-]+@el-eng.menoufia.edu.eg$";;
    public static final String regEx_positive_digits="^[0-9]";



    //Fragments Tags
    public static final String Login_Fragment = "Login_Fragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";


    //authentication constants
    public static final String REGISTER_OPERATION = "register";
    public static final String LOGIN_OPERATION = "login";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String IS_LOGGED_IN = "isLoggedIn";

    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String UNIQUE_ID = "unique_id";
    public static final String EXTRA_UNIQUE_ID ="extra_unique_id" ;


    public static final String TAG = "Authentication";



    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}