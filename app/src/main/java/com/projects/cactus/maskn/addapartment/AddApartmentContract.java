package com.projects.cactus.maskn.addapartment;

import android.content.Intent;

import com.mobsandgeeks.saripaar.Validator;
import com.projects.cactus.maskn.BasePresenter;
import com.projects.cactus.maskn.BaseView;
import com.projects.cactus.maskn.data.apiservies.model.Apartment;

import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by el on 10/17/2017.
 */
public interface AddApartmentContract {


     interface View extends BaseView<Presenter>{

         void showApartmentAddedSuccessfully();
         void openGallery();
         void showErrorNoImagesAdded();
         void setWindowEnabled(boolean enabled);

         void onGettingCities(List<String> cities);
     }

   interface Presenter extends BasePresenter{

       void addApartment(Apartment apartment);
       void validate(Validator validator);
       void onResult(int RESULT_CODE, Intent data);

       void addApartment(List<MultipartBody.Part> apartmentImages, Apartment apartment);

       void getCitiesList();
   }


}
