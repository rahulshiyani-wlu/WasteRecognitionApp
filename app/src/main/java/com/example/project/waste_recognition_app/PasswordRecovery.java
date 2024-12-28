package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecovery extends AppCompatActivity {

    private EditText resetPasswordEmail;
    private Button sendResetLinkButton;
    private TextView backToLogin;
    private TextView userInitialTextView; // For user's initial in the blue circle
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_recovery);

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance();
        initializeUIComponents();
        setupUserInitial();
        setupListeners();
    }

    // Method to initialize UI components
    private void initializeUIComponents() {
        resetPasswordEmail = findViewById(R.id.reset_password_email);
        sendResetLinkButton = findViewById(R.id.send_reset_link_button);
        backToLogin = findViewById(R.id.back_to_login);
        userInitialTextView = findViewById(R.id.user_initial);
    }

    // Set up the user initial displayed in the toolbar, using shared preferences to retrieve the username
    private void setupUserInitial() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", ""); // Default to empty if no username
        String initial = username.isEmpty() ? "R" : String.valueOf(username.charAt(0)).toUpperCase();

        // Set the user initial in the TextView
        userInitialTextView.setText(initial);

        // Set OnClickListener for the user initial to show a logout menu
        userInitialTextView.setOnClickListener(this::showUserOptionsMenu);
    }

    // Show a popup menu with user options, such as logging out
    private void showUserOptionsMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(PasswordRecovery.this, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.user_options_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_logout) {
                logoutUser();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    // Log out the user by clearing shared preferences and navigating back to LoginActivity
    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Set up listeners for various UI elements
    private void setupListeners() {
        sendResetLinkButton.setOnClickListener(v -> sendResetLink());
        backToLogin.setOnClickListener(v -> finish()); // Navigate back to login
    }

    // Send a password reset link to the entered email address
    private void sendResetLink() {
        String email = resetPasswordEmail.getText().toString().trim();

        if (email.isEmpty()) {
            showToast("Please enter your email address");
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Password reset email sent to " + email);
                    } else {
                        showToast("Failed to send reset email: " + task.getException().getMessage());
                    }
                });
    }

    // Show a toast message to the user
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}