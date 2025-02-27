package com.example.carsharing;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private RatingBar ratingBar;
    private Button btnSubmitRating, btnSearchRide;
    private TextView tvBookingConfirmation;
    private BookingDatabaseHelper dbHelper; // Database helper for storing ratings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);
        btnSearchRide = findViewById(R.id.btnSearchRide);
        tvBookingConfirmation = findViewById(R.id.tvBookingConfirmation); // New TextView for booking status

        // Initialize the database helper
        dbHelper = new BookingDatabaseHelper(this);

        // Welcome message
        tvWelcome.setText("Welcome to the Car Sharing Dashboard!");

        // Retrieve booking status from Intent (for confirmation message)
        Intent intent = getIntent();
        String bookingStatus = intent.getStringExtra("bookingStatus");

        // Show booking confirmation message based on status
        if (bookingStatus != null && bookingStatus.equals("success")) {
            tvBookingConfirmation.setText("Your booking has been confirmed!");
            Toast.makeText(UserDashboardActivity.this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
        } else {
            tvBookingConfirmation.setText("Booking failed. Please try again.");
        }

        // Handle the submit button click for ratings
        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the rating value from RatingBar
                float ratingValue = ratingBar.getRating();

                // Log values to check
                Log.d("RatingActivity", "User: " + "User123"); // Replace with actual dynamic user
                Log.d("RatingActivity", "Rating: " + ratingValue);

                // Check if the rating value is valid (greater than 0)
                if (ratingValue > 0) {
                    // Get writable database
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // Prepare values to insert into the database
                    ContentValues values = new ContentValues();
                    values.put(BookingDatabaseHelper.COLUMN_USER, "User123"); // Replace with actual user data
                    values.put(BookingDatabaseHelper.COLUMN_RATING, ratingValue);

                    // Perform the insert operation
                    long rowId = db.insert(BookingDatabaseHelper.TABLE_RATINGS, null, values);

                    // Check if insertion was successful
                    if (rowId != -1) {
                        Log.d("RatingActivity", "Rating submitted successfully.");
                        Toast.makeText(UserDashboardActivity.this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("RatingActivity", "Failed to submit rating.");
                        Toast.makeText(UserDashboardActivity.this, "Failed to submit rating. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    // Close the database connection
                    db.close();
                } else {
                    // Handle invalid rating value
                    Toast.makeText(UserDashboardActivity.this, "Please give a rating!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle the search ride button click
        btnSearchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log the click to check if it's being triggered
                Log.d("UserDashboardActivity", "Search Ride button clicked");

                // Navigate to the Search Ride Activity when the button is clicked
                Intent intent = new Intent(UserDashboardActivity.this, SearchRideActivity.class);
                startActivity(intent);
            }
        });
    }
}
