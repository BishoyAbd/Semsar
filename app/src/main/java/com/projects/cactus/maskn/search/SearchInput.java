package com.projects.cactus.maskn.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by el on 10/29/2017.
 */

public class SearchInput {


    @SerializedName("city")
    String city;
    @SerializedName("min_price")
    int from;

    @SerializedName("gender")
    int gender;

    @SerializedName("max_price")
    int to;
    @SerializedName("rooms_number")
    List<CharSequence> arrayNumOfRooms;
    List<CharSequence> arrayNumOfBeds;
    List<CharSequence> arrayNumOfBaths;

    public SearchInput(String city, int gender, int from, int to, List<CharSequence> arrayNumOfRooms, List<CharSequence> arrayNumOfBeds, List<CharSequence> arrayNumOfBaths) {
        this.setCity(city);
        this.setFrom(from);
        this.setTo(to);
        this.setArrayNumOfRooms(arrayNumOfRooms);
        this.setArrayNumOfBeds(arrayNumOfBeds);
        this.setArrayNumOfBaths(arrayNumOfBaths);
        this.setGender(gender);
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<CharSequence> getArrayNumOfRooms() {
        return arrayNumOfRooms;
    }

    public void setArrayNumOfRooms(List<CharSequence> arrayNumOfRooms) {
        this.arrayNumOfRooms = arrayNumOfRooms;
    }

    public List<CharSequence> getArrayNumOfBeds() {
        return arrayNumOfBeds;
    }

    public void setArrayNumOfBeds(List<CharSequence> arrayNumOfBeds) {
        this.arrayNumOfBeds = arrayNumOfBeds;
    }

    public List<CharSequence> getArrayNumOfBaths() {
        return arrayNumOfBaths;
    }

    public void setArrayNumOfBaths(List<CharSequence> arrayNumOfBaths) {
        this.arrayNumOfBaths = arrayNumOfBaths;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
