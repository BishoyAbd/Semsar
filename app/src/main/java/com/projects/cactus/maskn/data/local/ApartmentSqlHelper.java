package com.projects.cactus.maskn.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bisho on 11/9/2017.
 */

public class ApartmentSqlHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Semsar.db";
    private static final int DATABASE_VERSION = 3;


    //table1 apartment details
    public static final String TABLE_APARTMENTS = "apartments";
    public static final String COLUMN_ID = "apartment_id";
    public static final String COLUMN_OWNER_NAME = "owner_name";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_FLOOR = "floor";
    public static final String COLUMN_ROOMS = "rooms";
    public static final String COLUMN_VIEWS = "views";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";


    private static final String TABLE_APARTMENT_CREATE =
            "CREATE TABLE " + TABLE_APARTMENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY UNIQUE, " +
                    COLUMN_OWNER_NAME + " VARCHAR2, " +
                    COLUMN_CITY + " VARCHAR2, " +
                    COLUMN_PRICE + " INTEGER, " +
                    COLUMN_ADDRESS + " VARCHAR2, " +
                    COLUMN_PHONE + " VARCHAR2, " +
                    COLUMN_VIEWS + " INTEGER, " +
                    COLUMN_FLOOR + " INTEGER, " +
                    COLUMN_ROOMS + " INTEGER, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_DATE + " DATE " +
                    ")";


    //apartment images
    public static final String TABLE_APARTMENT_IMAGES = "apartment_images";
    public static final String COLUMN_IMAGE_ID = "image_id";
    public static final String COLUMN_APARTMENT_ID = "apartment_id";
    public static final String COLUMN_URL = "url";


    private static final String TABLE_IMAGES_CREATE =
            "CREATE TABLE " + TABLE_APARTMENT_IMAGES + " (" +
                    COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    COLUMN_APARTMENT_ID + " INTEGER NOT NULL , " +
                    COLUMN_URL + " TEXT " +
                    ")";


    public static final String QUERY_GET_ALL_FAVOURITE = "SELECT apartments.* , GROUP_CONCAT(apartment_images.url ) as url" +
            " FROM apartments LEFT JOIN apartment_images ON apartments.apartment_id = apartment_images.apartment_id " +
            " GROUP BY apartments.apartment_id ORDER BY apartments.date desc ";


//    private static final String TABLE_IMAGES_CREATE =
//            "CREATE TABLE " + TABLE_APARTMENT_IMAGES + " (" +
//                    COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    COLUMN_APARTMENT_ID + " VARCHAR2 , " +
//                    COLUMN_URL + " TEXT, " +
//                    "FOREIGN KEY ("+COLUMN_APARTMENT_ID + ") "+
//                    "REFERENCES "+TABLE_APARTMENTS+"("+COLUMN_ID+") "+
//                    "ON DELETE CASCADE "+
//                    ")";


    public static final String QUERY_GET_ALL_APARTMENTS = "SELECT* FROM " + TABLE_APARTMENTS;

    public static final String QUERY_CHECk_APARTMENT_EXIST = "SELECT * FROM  apartments WHERE "+COLUMN_APARTMENT_ID+"  = ? ";

    public ApartmentSqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_APARTMENT_CREATE);
        sqLiteDatabase.execSQL(TABLE_IMAGES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_APARTMENTS);
        sqLiteDatabase.execSQL(TABLE_APARTMENT_CREATE);
        //images
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_APARTMENT_IMAGES);
        sqLiteDatabase.execSQL(TABLE_IMAGES_CREATE);
    }
}
