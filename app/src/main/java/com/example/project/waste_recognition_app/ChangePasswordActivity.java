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
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    // UI Components
    private EditText newPasswordEditText;
    private Button changePasswordBtn, backToSettingsBtn;
    private TextView userInitialTextView;

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        initializeUI();
        displayUserInitial();
        setChangePasswordClickListener();
        setBackToSettingsClickListener();
    }

    // Method to initialize UI components such as EditText, Button, and TextView
    private void initializeUI() {
        newPasswordEditText = findViewById(R.id.user_new_password);
        changePasswordBtn = findViewById(R.id.user_change_password_btn);
        backToSettingsBtn = findViewById(R.id.back_setting_change_password_btn);
        userInitialTextView = findViewById(R.id.user_initial); // Toolbar user initial display
    }

    // Display the user's initial in the toolbar, using shared preferences to retrieve the username
    private void displayUserInitial() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "R"); // Default to "R" if no username is found
        String userInitial = username.isEmpty() ? "R" : String.valueOf(username.charAt(0)).toUpperCase();

        // Set the user initial in the TextView
        userInitialTextView.setText(userInitial);

        // Set OnClickListener for the user initial to show options like logout
        userInitialTextView.setOnClickListener(this::showUserOptionsPopup);
    }

    // Show a popup menu with user options, such as logging out
    private void showUserOptionsPopup(View anchorView) {
        PopupMenu userOptionsMenu = new PopupMenu(ChangePasswordActivity.this, anchorView);
        userOptionsMenu.getMenuInflater().inflate(R.menu.user_options_menu, userOptionsMenu.getMenu());
        userOptionsMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_logout) {
                performLogout();
                return true;
            }
            return false;
        });
        userOptionsMenu.show();
    }

    // Log out the user by clearing shared preferences and navigating to LoginActivity
    private void performLogout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    // Set up the change password button listener
    private void setChangePasswordClickListener() {
        changePasswordBtn.setOnClickListener(v -> changeUserPassword());
    }

    // Set up the back to settings button listener
    private void setBackToSettingsClickListener() {
        backToSettingsBtn.setOnClickListener(v -> navigateToSettingsFragment());
    }

    // Change the user's password using Firebase Authentication
    private void changeUserPassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();

        if (newPassword.isEmpty()) {
            displayToastMessage("New password cannot be empty");
            return;
        }

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            displayToastMessage("Password successfully changed");
                            finish();
                        } else {
                            displayToastMessage("Failed to change password: " + task.getException().getMessage());
                        }
                    });
        } else {
            displayToastMessage("User not logged in");
        }
    }

    // Navigate back to SettingsFragment
    private void navigateToSettingsFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("navigateTo", "settings");
        startActivity(intent);
        finish();
    }

    // Display a short message to the user
    private void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}