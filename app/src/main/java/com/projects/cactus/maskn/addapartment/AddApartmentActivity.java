package com.projects.cactus.maskn.addapartment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.projects.cactus.maskn.BuildConfig;
import com.projects.cactus.maskn.R;
import com.projects.cactus.maskn.SemsarApplication;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.projects.cactus.maskn.profile.ProfileActivity;
import com.projects.cactus.maskn.search.SearchDialog;
import com.projects.cactus.maskn.utils.FileUtils;
import com.projects.cactus.maskn.utils.PermissionUtil;
import com.projects.cactus.maskn.utils.Util;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import prod.Injection;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


//the validator available anotation are in here
//https://github.com/ragunathjawahar/android-saripaar/tree/master/saripaar/src/main/java/com/mobsandgeeks/saripaar/annotation

/**
 * this activity is used to add  new apartments
 * contains basic edit texts for filling apartment information
 */
public class AddApartmentActivity extends AppCompatActivity implements AddApartmentContract.View, Validator.ValidationListener, SearchDialog.DialogCallback {

    static final int REQUEST_CODE_APARTMENT_LIST_IMAGES = 80;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 22;

    @BindView(R.id.city_tv_addApart_)
    TextView cityTv;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @NotEmpty(message = "")
    @DecimalMax(value = 10000, sequence = 1, message = "اغلي حاجه عشرةالاف")
    @DecimalMin(value = 100, sequence = 2, message = "اقل حاجه 100")
    @BindView(R.id.priceEt_addApartmentAc)
    EditText priceEt;
    @NotEmpty(sequence = 3, message = "")
    @BindView(R.id.descriptionEt_addApartmentAc)
    EditText descriptionEt;
    @NotEmpty(message = "")
    @Length(max = 20, message = "من فضلك ادخل عنوان صحيح", sequence = 4)
    @BindView(R.id.addressEt_addApartmentAc)
    EditText addressEt;
    @NotEmpty(message = "")
    @DecimalMax(value = 5, message = "اقصي عدد للغرف هو 5", sequence = 5)
    @DecimalMin(value = 1, message = "اقل عدد للغرف هو 1", sequence = 6)
    @BindView(R.id.numOfRoomsEt_addApartmentAc)
    EditText numOfRooms;
    @NotEmpty(message = "")
    @DecimalMax(value = 10, message = "اقصي عدد للادوار هو 8 ", sequence = 7)
    @BindView(R.id.floorEt_addApartmentAc)
    EditText floorEt;
    @BindView(R.id.parentView_addApartAct)
    ViewGroup parent;
    @BindView(R.id.loadingView_block)
    LVBlock loadingView;
    private DialogFragment citiesDialog;
    private SweetAlertDialog errorSweetAlertDialog, successSweetAlertDialog;
    //belongs to validator library
    private Validator validator;
    private AddApartmentContract.Presenter presenter;
    //properties
    private List<MultipartBody.Part> apartmentImages;
    private List<String> citiesList = Collections.emptyList();
    private String city;
    private int gender = 1;
    private String userId;
    private ArrayAdapter<String> arrayAdapterCities;
    private Apartment apartment;
    private File compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        validator = new Validator(this);
        validator.setValidationListener(this);
        presenter = new ApartmentPresenter(this, Injection.provideApartmentRepository(SemsarApplication.getContext()));

        userId = getIntent().getStringExtra(Util.KEY_USER_ID);
        Timber.d("user id to be used by addApratmentAct----> " + userId);
        if ("no_user".equals(userId) || null == userId)
            //goto login or crach the app
            if (BuildConfig.DEBUG) {
                addressEt.setText("address");
                numOfRooms.setText("1");
                floorEt.setText("1");
                descriptionEt.setText("des");
                priceEt.setText("200");


            }

    }


    private void initViews() {
        setContentView(R.layout.activity_add_apartment);
        ButterKnife.bind(this);
        initSpinners();

        //for error dialog
        errorSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
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
        successSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setContentText(getResources().getString(R.string.success)).setTitleText("هااااح");
        successSweetAlertDialog.setCancelable(false);
        successSweetAlertDialog.setCanceledOnTouchOutside(false);


    }


    private void initSpinners() {


        //I was planning to add cities inside a spinner after I get the city list once the activity starts
        // but I decided to use dialog with listview and progress instead

        ArrayAdapter<String> genderSpinnerAdapter=new ArrayAdapter<String>(this,R.layout.spinner_gender_item,getResources().getStringArray(R.array.genderArray));
        genderSpinner.setAdapter(genderSpinnerAdapter);
        gender = 1;
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Timber.d("selected gender --> "+i );
                gender = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Timber.d("nothing selected");
            }
        });

    }

    @OnClick(R.id.addApartment_btn_actAdd)
    void uploadApartment() {
        presenter.validate(validator);
    }

    //decided to use the same name as spinner ---- so lazy
    @OnClick(R.id.city_tv_addApart_)
    void openCitiesDialog() {
        citiesDialog = SearchDialog.getInstance("اختار المدينة");
        citiesDialog.show(getSupportFragmentManager(), SearchDialog.TAG);
    }


    @OnClick(R.id.Ib_openGallery)
    void getImages() {
        openGallery();
    }


    //to apply default font og calligrapgy
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void setPresenter(AddApartmentContract.Presenter presenter) {
//        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        loadingView.startAnim();


    }

    //during loading
    @Override
    public void setWindowEnabled(boolean enabled) {
        if (enabled) {
            parent.setBackgroundColor(getResources().getColor(R.color.white));
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            parent.setBackgroundColor(getResources().getColor(R.color.grayLight));

        }
    }

    @Override
    public void onGettingCities(List<String> cities) {
//        citiesList = cities;
//        arrayAdapterCities = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citiesList);
//        citySpinner.setAdapter(arrayAdapterCities);
    }

    @Override
    public void hideLoading() {
        loadingView.stopAnim();
        loadingView.setVisibility(View.GONE);
    }


    @Override
    public void showError() {
        if (!errorSweetAlertDialog.isShowing() && null != errorSweetAlertDialog)
            errorSweetAlertDialog.show();


    }

    @Override
    public void hideError() {
        if (errorSweetAlertDialog.isShowing() && errorSweetAlertDialog != null)
            errorSweetAlertDialog.dismiss();

    }

    @Override
    public void showApartmentAddedSuccessfully() {
        if (!successSweetAlertDialog.isShowing() && successSweetAlertDialog != null) {
            successSweetAlertDialog.show();
            successSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    //open profile activity
                    successSweetAlertDialog.dismiss();
                    openProfileActivity(userId);

                }


            });

        }
    }

    private void openProfileActivity(String userId) {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent.putExtra(Util.KEY_USER_ID, userId));
        finish();
    }


    //belongs to validator library
    @Override
    public void onValidationSucceeded() {

        if (null != city) {
            //upload apartment
            apartment = new Apartment();
            apartment.setAddress(addressEt.getText().toString());
            apartment.setGender(gender);
            apartment.setPrice(Integer.parseInt(priceEt.getText().toString()));
            apartment.setFloor(Integer.parseInt(floorEt.getText().toString()));
            apartment.setCity(city);
            apartment.setDescription(descriptionEt.getText().toString());
            apartment.setRoomsNumber(Integer.parseInt(numOfRooms.getText().toString()));
            apartment.setOwnerId(userId);
            presenter.addApartment(apartmentImages, apartment);
        } else
            showError(getString(R.string.pleaseChoose_city));
    }

    private void showError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    //tobe added inside the util
    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void openGallery() {


        PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onNeedPermission() {
                        ActivityCompat.requestPermissions(
                                AddApartmentActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        );
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //  show a dialog explaining permission and then request permission
                        ActivityCompat.requestPermissions(
                                AddApartmentActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        );


                    }

                    @Override
                    public void onPermissionDisabled() {
                        Toast.makeText(AddApartmentActivity.this, "Permission Disabled.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionGranted() {
                        openMAtessi();
                    }
                });


    }


    void openMAtessi() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(5)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .theme(R.style.Matisse_Zhihu)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_APARTMENT_LIST_IMAGES);
    }

    @Override
    public void showErrorNoImagesAdded() {
        Toast.makeText(this, "يجب اختيار صور للشقة", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            apartmentImages = prepareImages(Matisse.obtainResult(data));
    }


    public List<MultipartBody.Part> prepareImages(List<Uri> paths) {
        List<MultipartBody.Part> list = new ArrayList<>();
        int i = 0;
        for (Uri uri : paths) {
            //very important files[]
            MultipartBody.Part imageRequest = prepareFilePart("apartment_images[]", uri);
            list.add(imageRequest);
        }

        return list;
        //manipulating data came fro gallery
    }


    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // I use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        try {
            compressedImageFile = new Compressor(this).compressToFile(file);
            Timber.d("size of image after compression --> " + compressedImageFile.getTotalSpace());

        } catch (IOException e) {
            e.printStackTrace();
            Timber.d(e.getLocalizedMessage());
            showError(getString(R.string.error));
        }


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(this.getContentResolver().getType(fileUri)), compressedImageFile);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    //two dialog callback method
    @Override
    public void OnClickCancel() {
        citiesDialog.dismiss();
    }

    @Override
    public void onClickList(String city) {
        this.city = city;
        cityTv.setText(" " + city + " ");
        citiesDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    openMAtessi();
                } else {
                    // permission denied, boo! Disable the
                    Snackbar.make(findViewById(android.R.id.content), "لازم توافق علي ال permission ", Snackbar.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}