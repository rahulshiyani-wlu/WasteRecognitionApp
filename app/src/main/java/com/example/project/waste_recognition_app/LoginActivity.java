package com.example.project.waste_recognition_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    // Define constants for logging and shared preferences keys
    private static final String LOGIN_ACTIVITY = "LoginActivity";
    private static final String PREFS_FILE = "UserPreferences";
    private static final String PREF_KEY_EMAIL = "SavedEmail";

    // Declare UI components
    private TextInputEditText inputEmail, inputPassword;
    private Button loginButton;

    // SharedPreferences instance for saving/retrieving data
    private SharedPreferences sharedPreferences;

    // Utility method to display a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGIN_ACTIVITY, "onCreate called"); // Log lifecycle event
        setContentView(R.layout.activity_login); // Set the layout for the activity

        initializeUIComponents(); // Initialize UI components
        setBackgroundImage(); // Set the background image
        restoreSavedEmail(); // Restore saved email from SharedPreferences

        setupSignUpButton(); // Setup Sign Up button functionality
        setupLoginButton(); // Setup Login button functionality
    }

    // Initialize UI components by finding their respective IDs
    @SuppressLint("WrongViewCast")
    private void initializeUIComponents() {
        inputEmail = findViewById(R.id.email_login);
        inputPassword = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.btnLogin);
    }

    // Set a static background image for the activity
    private void setBackgroundImage() {
        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        backgroundImage.setImageResource(R.drawable.garbage);
    }

    // Retrieve the saved email from SharedPreferences
    private void restoreSavedEmail() {
        sharedPreferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(PREF_KEY_EMAIL, "");
        if (!savedEmail.isEmpty()) {
            inputEmail.setText(savedEmail);
        }
    }

    // Set up the Sign Up button to navigate to the SignUpActivity
    private void setupSignUpButton() {
        TextView signUpButton = findViewById(R.id.btnsignupredirect);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // Set up the Login button to validate inputs and proceed to MainActivity
    private void setupLoginButton() {
        loginButton.setOnClickListener(v -> {
            if (isInputValid()) { // Validate user inputs
                saveEmailToPreferences(); // Save email to SharedPreferences
                navigateToMainActivity(); // Navigate to MainActivity
            }
        });
    }

    // Validate email and password inputs
    private boolean isInputValid() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Validate email format
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Enter a valid email address");
            return false;
        }

        // Validate password requirements
        if (password.isEmpty()) {
            inputPassword.setError("Password cannot be empty");
            return false;
        }

        // Ensure password meets security criteria
        if (!Pattern.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", password)) {
            inputPassword.setError("Password must be at least 8 characters, include a digit and a special character");
            return false;
        }

        return true; // Return true if all validations pass
    }

    // Save the entered email to SharedPreferences
    private void saveEmailToPreferences() {
        String email = inputEmail.getText().toString().trim();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY_EMAIL, email);
        editor.apply(); // Apply changes to SharedPreferences
    }

    // Navigate to MainActivity after successful login
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity
    }

    // Log lifecycle event: onResume
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOGIN_ACTIVITY, "onResume called");
    }

    // Log lifecycle event: onStart
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOGIN_ACTIVITY, "onStart called");
    }

    // Log lifecycle event: onPause
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOGIN_ACTIVITY, "onPause called");
    }

    // Log lifecycle event: onStop
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOGIN_ACTIVITY, "onStop called");
    }

    // Log lifecycle event: onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOGIN_ACTIVITY, "onDestroy called");
    }

    // Save the activity state before it gets destroyed
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOGIN_ACTIVITY, "onSaveInstanceState called");
    }

    // Restore the activity state after it has been recreated
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(LOGIN_ACTIVITY, "onRestoreInstanceState called");
    }
}