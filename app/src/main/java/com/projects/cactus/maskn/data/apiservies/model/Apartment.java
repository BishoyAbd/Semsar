
package com.projects.cactus.maskn.data.apiservies.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Apartment {

//    public static final int GENDER_MAN=1;
//    public static final int GENDER_WOMAN=2;
//    public static final int GENDER_ALL=3;


    @Expose
    @SerializedName("address")
    private String mAddress;
    @SerializedName("apartment_id")
    private String mAppartmentId;
    @SerializedName("approved")
    private String mApproved;
    @SerializedName("available_rooms")
    private String mAvailableRooms;
    @SerializedName("city")
    private String mCity;
    @SerializedName("date")
    private String mDate;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("floor")
    private int mFloor;
    //owner info
    @SerializedName("full_name")
    private String mOwnerName;
    @SerializedName("profile_picture")
    private String mOwnerProfilePhoto;
    @SerializedName("phone_number")
    private String mPhoneNumber;
    @SerializedName("owner_id")
    private String mOwnerId;

    @SerializedName("price")
    private int mPrice;
    @SerializedName("rooms_number")
    private int mRoomsNumber;
    @SerializedName("images")
    private List<String> mApartmentImages;
    @SerializedName("gender")
    private int gender;
    @SerializedName("views")
    private int numOfViews;

    public Apartment() {
        mApartmentImages = new ArrayList<>();
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getAppartmentId() {
        return mAppartmentId;
    }

    public void setAppartmentId(String appartmentId) {
        mAppartmentId = appartmentId;
    }

    public String getApproved() {
        return mApproved;
    }

    public void setApproved(String approved) {
        mApproved = approved;
    }

    public String getAvailableRooms() {
        return mAvailableRooms;
    }

    public void setAvailableRooms(String availableRooms) {
        mAvailableRooms = availableRooms;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getFloor() {
        return mFloor;
    }

    public void setFloor(int floor) {
        mFloor = floor;
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }

    public int getPrice() {
        return getmPrice();
    }

    public void setPrice(int price) {
        setmPrice(price);
    }

    public int getRoomsNumber() {
        return mRoomsNumber;
    }

    public void setRoomsNumber(int roomsNumber) {
        mRoomsNumber = roomsNumber;
    }

    public List<String> getmApartmentImages() {
        return mApartmentImages;
    }

    public void setmApartmentImages(List<String> mApartmentImages) {
        this.mApartmentImages = mApartmentImages;
    }

    public void addImage(String url) {
        this.mApartmentImages.add(url);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getmOwnerName() {
        return mOwnerName;
    }

    public void setmOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getmOwnerProfilePhoto() {
        return mOwnerProfilePhoto;
    }

    public void setmOwnerProfilePhoto(String mOwnerProfilePhoto) {
        this.mOwnerProfilePhoto = mOwnerProfilePhoto;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "mAddress='" + mAddress + '\'' +
                ", mAppartmentId='" + mAppartmentId + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mFloor=" + mFloor +
                ", mOwnerName='" + mOwnerName + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mPrice=" + mPrice +
                ", mRoomsNumber=" + mRoomsNumber +
                ", gender=" + gender +
                ", numOfViews=" + numOfViews +
                '}';
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public int getNumOfViews() {
        return numOfViews;
    }

    public void setNumOfViews(int numOfViews) {
        this.numOfViews = numOfViews;
    }
}
