package com.example.carsharing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvRideDetails, tvAmount;
    private EditText etCardNumber, etCardExpiry, etCardCVV;
    private Button btnProcessPayment;

    // Notification channel ID
    private static final String CHANNEL_ID = "payment_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize views
        tvRideDetails = findViewById(R.id.tvRideDetails);
        tvAmount = findViewById(R.id.tvAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardExpiry = findViewById(R.id.etCardExpiry);
        etCardCVV = findViewById(R.id.etCardCVV);
        btnProcessPayment = findViewById(R.id.btnProcessPayment);

        // Retrieve the ride details and amount passed from SearchRideActivity
        String rideDetails = getIntent().getStringExtra("rideDetails");
        String amount = getIntent().getStringExtra("amount");

        // Set the data to the TextViews
        tvRideDetails.setText("Ride Details: " + rideDetails);
        tvAmount.setText("Amount: " + amount);

        btnProcessPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simulate the payment process
                simulatePayment();
            }
        });
    }

    private void simulatePayment() {
        String cardNumber = etCardNumber.getText().toString();
        String cardExpiry = etCardExpiry.getText().toString();
        String cardCVV = etCardCVV.getText().toString();

        // Basic validation for input fields
        if (cardNumber.isEmpty() || cardExpiry.isEmpty() || cardCVV.isEmpty()) {
            Toast.makeText(PaymentActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate payment processing logic here
        boolean paymentSuccess = processPayment(cardNumber, cardExpiry, cardCVV);

        if (paymentSuccess) {
            Toast.makeText(PaymentActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
            // Show notification
            showPaymentNotification();
            // Optionally, update booking/payment status in the database
            updatePaymentStatusInDatabase();

            // Pass booking status to dashboard activity
            navigateToDashboard();
        } else {
            Toast.makeText(PaymentActivity.this, "Payment failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean processPayment(String cardNumber, String cardExpiry, String cardCVV) {
        // Simulate payment validation and approval
        if (cardNumber.startsWith("4")) {
            return true; // Simulate success
        } else {
            return false; // Simulate failure
        }
    }

    private void updatePaymentStatusInDatabase() {
        // Here we could update payment status in your database (Booking/Payment tables)
        // Use your existing database helper methods
        BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Update the database with payment status (e.g., for a booking)
        db.close();
    }

    private void showPaymentNotification() {
        // Create NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Payment Notifications";
            String description = "Notifications related to payment status";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Create the notification
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Payment Success")
                .setContentText("Your payment has been processed successfully.")
                .setSmallIcon(R.drawable.ic_payment_success) // Replace with your own icon
                .build();

        // Issue the notification
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }

    private void navigateToDashboard() {
        // Pass booking status to UserDashboardActivity
        Intent dashboardIntent = new Intent(PaymentActivity.this, UserDashboardActivity.class);
        dashboardIntent.putExtra("bookingStatus", "success"); // Pass booking status
        startActivity(dashboardIntent);
    }
}
