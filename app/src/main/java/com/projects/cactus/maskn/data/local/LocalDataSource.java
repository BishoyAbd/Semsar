package com.projects.cactus.maskn.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.projects.cactus.maskn.data.apiservies.model.Apartment;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.projects.cactus.maskn.data.local.ApartmentSqlHelper.COLUMN_ID;
import static com.projects.cactus.maskn.data.local.ApartmentSqlHelper.TABLE_APARTMENTS;
import static com.projects.cactus.maskn.data.local.ApartmentSqlHelper.TABLE_APARTMENT_IMAGES;

/**
 * Created by el on 10/15/2017.
 */


/*
used to save and delete user's favourites in a local database
 */
public class LocalDataSource {


    private static LocalDataSource instance;
    private ApartmentSqlHelper apartmentSqlHelper;
    private SQLiteDatabase sqLiteDatabase;
    private SqlBrite sqlBrite;
    private BriteDatabase db;


    //make sure apartmentSqlHelper is the same object "singleton" all over your app ... just for thread safety

    private LocalDataSource(Context context) {
        apartmentSqlHelper = new ApartmentSqlHelper(context);
        sqlBrite = new SqlBrite.Builder().build();
        db = sqlBrite.wrapDatabaseHelper(apartmentSqlHelper, Schedulers.io());
    }

    public static LocalDataSource getInstance(Context context) {
        if (instance == null)
            return new LocalDataSource(context);

        return instance;
    }


    public static void destroyInstance() {
        instance = null;
    }

    public String getUserId(String key) {

        return null;
    }


    public long add(Apartment apartment) {
        long id = -1; //row id if success or -1 if failure

        ContentValues contentValues = new ContentValues();
        contentValues.put(ApartmentSqlHelper.COLUMN_ID, apartment.getAppartmentId());
        contentValues.put(ApartmentSqlHelper.COLUMN_CITY, apartment.getCity());
        contentValues.put(ApartmentSqlHelper.COLUMN_PHONE, apartment.getmPhoneNumber());
        contentValues.put(ApartmentSqlHelper.COLUMN_OWNER_NAME, apartment.getmOwnerName());
        contentValues.put(ApartmentSqlHelper.COLUMN_ADDRESS, apartment.getAddress());
        contentValues.put(ApartmentSqlHelper.COLUMN_DESCRIPTION, apartment.getDescription());
        contentValues.put(ApartmentSqlHelper.COLUMN_PRICE, apartment.getPrice());
        contentValues.put(ApartmentSqlHelper.COLUMN_FLOOR, apartment.getFloor());
        contentValues.put(ApartmentSqlHelper.COLUMN_VIEWS, apartment.getNumOfViews());
        contentValues.put(ApartmentSqlHelper.COLUMN_ROOMS, apartment.getRoomsNumber());
        contentValues.put(ApartmentSqlHelper.COLUMN_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        //values for image
        ContentValues images = new ContentValues();
        for (String url : apartment.getmApartmentImages()) {
            images.put(ApartmentSqlHelper.COLUMN_URL, url);
            images.put(ApartmentSqlHelper.COLUMN_APARTMENT_ID, apartment.getAppartmentId());
        }
        try {

            id = db.insert(TABLE_APARTMENTS, contentValues); //if successfully added apartment then add images
            if (id > 0)
                db.insert(TABLE_APARTMENT_IMAGES, images);

        } catch (SQLException e) {


            Timber.d("error inserting apartment -> " + e.getLocalizedMessage());
        }

        return id;
    }


    public Observable<SqlBrite.Query> getAllFavs() {
        return db.createQuery(TABLE_APARTMENTS, ApartmentSqlHelper.QUERY_GET_ALL_FAVOURITE);
    }

    @Deprecated
    public Observable<SqlBrite.Query> getImages(String apartmentId) {
        String[] args = {apartmentId};
        String QUERY_GET_IMAGES = "SELECT url FROM  apartment_images  WHERE  apartment_id = ? ";
        //String QUERY_GET_IMAGES = "SELECT * FROM  apartment_images  ";

        return db.createQuery(ApartmentSqlHelper.TABLE_APARTMENT_IMAGES, QUERY_GET_IMAGES, args);
    }


    public void close() {
        db.close();
    }

    public void deleteApartmentFromFavourite(String id) {
        db.delete(ApartmentSqlHelper.TABLE_APARTMENTS, COLUMN_ID + " = " + id);
    }

    public Observable<SqlBrite.Query> checkInFavouriteOrNot(Apartment apartment) {

        return db.createQuery(TABLE_APARTMENTS, ApartmentSqlHelper.QUERY_CHECk_APARTMENT_EXIST, apartment.getAppartmentId());

    }


}
