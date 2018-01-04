package com.projects.cactus.maskn.authentication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bishoy on 2/28/2017.
 */

public class User {


    @SerializedName("full_name")
    private String name;

    private String email; //pluplu@el-eng.menoufia.edu.eg

    @SerializedName("user_id")
    private String unique_id;

    @SerializedName("phone_number")
    private String phone_number;  //optional

    private String password;

    private String confirmPassword;

    @SerializedName("profile_url")
    private String profile_url;

    @SerializedName("cover_url")
    private String cover_url;

    private String bio;


    public User() {


    }

    public User(String name, String email, String unique_id,
                String phone_number, String password, String profile_url, String cover_url) {

        this.setName(name);
        this.setEmail(email);
        this.setUnique_id(unique_id);
        this.setPhoneNumber(phone_number);
        this.setPassword(password);
        this.setProfile_url(profile_url);
        this.setCover_url(cover_url);

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public String getPhone_number() {
        return phone_number;
    }


    public String getPassword() {
        return password;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getBio() {
        return bio;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }
}