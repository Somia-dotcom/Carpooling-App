package com.example.carsharing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookingDatabaseHelper extends SQLiteOpenHelper {

    // Database version and name
    private static final String DATABASE_NAME = "carpooling_db";
    private static final int DATABASE_VERSION = 2; // Increment version to 2

    // Table names
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String TABLE_RATINGS = "ratings";  // New table for ratings

    // Column names for BOOKINGS table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_SOURCE = "source";
    public static final String COLUMN_DESTINATION = "destination";
    public static final String COLUMN_RIDE = "ride";
    public static final String COLUMN_SEATS = "seats";
    public static final String COLUMN_PAYMENT_STATUS = "payment_status";
    public static final String COLUMN_AMOUNT_PAID = "amount_paid";

    // Column names for RATINGS table
    public static final String COLUMN_RATING_ID = "id";
    public static final String COLUMN_RATING_USER = "user";
    public static final String COLUMN_RATING = "rating"; // This is the rating column

    // Create table SQL statements
    private static final String CREATE_TABLE_BOOKINGS =
            "CREATE TABLE " + TABLE_BOOKINGS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USER + " TEXT, "
                    + COLUMN_SOURCE + " TEXT, "
                    + COLUMN_DESTINATION + " TEXT, "
                    + COLUMN_RIDE + " TEXT, "
                    + COLUMN_SEATS + " INTEGER, "
                    + COLUMN_PAYMENT_STATUS + " TEXT, "
                    + COLUMN_AMOUNT_PAID + " INTEGER"
                    + ");";

    private static final String CREATE_TABLE_RATINGS =
            "CREATE TABLE " + TABLE_RATINGS + " ("
                    + COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_RATING_USER + " TEXT, "
                    + COLUMN_RATING + " REAL" // Rating stored as REAL
                    + ");";

    // Constructor
    public BookingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOKINGS);
        db.execSQL(CREATE_TABLE_RATINGS);  // Create ratings table
    }

    // Upgrade the database (if needed)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RATINGS + " ("
                    + COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_RATING_USER + " TEXT, "
                    + COLUMN_RATING + " REAL" // Rating stored as REAL
                    + ");");
        }
    }
}
