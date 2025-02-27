package com.example.carsharing;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchRideActivity extends AppCompatActivity {

    private EditText etSource, etDestination, etSeats;
    private Button btnFindRides, btnBookRide;
    private TextView tvRideResults;
    private List<String> availableRides;
    private String selectedRide = "";
    private BookingDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride); // Make sure you have a corresponding XML layout file

        // Initialize views
        etSource = findViewById(R.id.etSource);
        etDestination = findViewById(R.id.etDestination);
        etSeats = findViewById(R.id.etSeats); // Input for the number of seats
        btnFindRides = findViewById(R.id.btnFindRides);
        btnBookRide = findViewById(R.id.btnBookRide);
        tvRideResults = findViewById(R.id.tvRideResults);

        // Initialize database helper
        dbHelper = new BookingDatabaseHelper(this);

        // Predefined routes for Pondicherry
        availableRides = new ArrayList<>();
        availableRides.add("Pondicherry to Auroville");
        availableRides.add("Puducherry to Cuddalore");
        availableRides.add("Pondicherry to Villianur");
        availableRides.add("Auroville to Pondicherry");
        availableRides.add("Cuddalore to Pondicherry");

        // Handle find rides button click
        btnFindRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = etSource.getText().toString().trim();
                String destination = etDestination.getText().toString().trim();

                if (source.isEmpty() || destination.isEmpty()) {
                    tvRideResults.setText("Please enter both source and destination!");
                } else {
                    searchRides(source, destination);
                }
            }
        });

        // Handle book ride button click
        btnBookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedRide.isEmpty()) {
                    String seatInput = etSeats.getText().toString();
                    if (seatInput.isEmpty()) {
                        Toast.makeText(SearchRideActivity.this, "Please enter the number of seats to book", Toast.LENGTH_SHORT).show();
                    } else {
                        int numSeats = Integer.parseInt(seatInput);
                        if (numSeats > 0) {
                            // Save booking details to database
                            bookRide(selectedRide, etSource.getText().toString(), etDestination.getText().toString(), numSeats);
                        } else {
                            Toast.makeText(SearchRideActivity.this, "Please enter a valid number of seats", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(SearchRideActivity.this, "Please select a ride to book", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Search for rides matching source and destination
    private void searchRides(String source, String destination) {
        StringBuilder searchResults = new StringBuilder();
        boolean rideFound = false;

        for (String ride : availableRides) {
            if (ride.toLowerCase().contains(source.toLowerCase()) &&
                    ride.toLowerCase().contains(destination.toLowerCase())) {
                searchResults.append(ride).append("\n");
                rideFound = true;
            }
        }

        if (rideFound) {
            tvRideResults.setText("Available Rides:\n" + searchResults.toString());
            selectedRide = searchResults.toString().split("\n")[0]; // Select the first ride from the search results
        } else {
            tvRideResults.setText("No rides found for the specified route.");
        }
    }

    // Book a ride and store booking details in the database
    private void bookRide(String ride, String source, String destination, int seats) {
        // Show a confirmation dialog for payment
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage("Proceed with payment for booking " + seats + " seat(s) on " + ride + "?")
                .setPositiveButton("Pay", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(BookingDatabaseHelper.COLUMN_USER, "User123"); // Placeholder user, replace with actual user data
                    values.put(BookingDatabaseHelper.COLUMN_SOURCE, source);
                    values.put(BookingDatabaseHelper.COLUMN_DESTINATION, destination);
                    values.put(BookingDatabaseHelper.COLUMN_RIDE, ride);
                    values.put(BookingDatabaseHelper.COLUMN_SEATS, seats); // Add number of seats
                    values.put(BookingDatabaseHelper.COLUMN_PAYMENT_STATUS, "Paid"); // Payment status
                    values.put(BookingDatabaseHelper.COLUMN_AMOUNT_PAID, seats * 100); // Calculate amount (e.g., ₹100 per seat)

                    long rowId = db.insert(BookingDatabaseHelper.TABLE_BOOKINGS, null, values);
                    db.close();

                    if (rowId != -1) {
                        Toast.makeText(SearchRideActivity.this, "Payment successful! Booking confirmed for " + seats + " seat(s)!", Toast.LENGTH_SHORT).show();

                        // Navigate to the dashboard with booking status
                        Intent intent = new Intent(SearchRideActivity.this, PaymentActivity.class);
                        intent.putExtra("bookingStatus", "success");
                        intent.putExtra("rideDetails", ride + " | Seats: " + seats);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SearchRideActivity.this, "Payment failed! Booking could not be completed.", Toast.LENGTH_SHORT).show();

                        // Navigate to the dashboard with failure status
                        Intent intent = new Intent(SearchRideActivity.this, PaymentActivity.class);
                        intent.putExtra("bookingStatus", "failure");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(SearchRideActivity.this, "Payment cancelled. Booking not completed.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
