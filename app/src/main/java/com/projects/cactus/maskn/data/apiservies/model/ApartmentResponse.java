package com.projects.cactus.maskn.data.apiservies.model;

import com.google.gson.annotations.SerializedName;
import com.projects.cactus.maskn.authentication.model.User;

/**
 * Created by el on 11/4/2017.
 */


//contains all information about apartment
public class ApartmentResponse {

    @SerializedName("user")
    private User user;
    @SerializedName("apartment")
    private Apartment apartment;

    public ApartmentResponse() {

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }
}
