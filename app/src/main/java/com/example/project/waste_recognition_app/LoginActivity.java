package com.example.project.waste_recognition_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_ACTIVITY = "LoginActivity";
    private static final String PREFS_FILE = "UserPreferences";
    private static final String PREF_KEY_EMAIL = "SavedEmail";

    private TextInputEditText inputEmail, inputPassword;
    private Button loginButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGIN_ACTIVITY, "onCreate called");
        setContentView(R.layout.activity_login);

        initializeUIComponents();
        setBackgroundImage();
        restoreSavedEmail();
        setupSignUpButton();
        setupLoginButton();

    }

    @SuppressLint("WrongViewCast")
    private void initializeUIComponents() {
        inputEmail = findViewById(R.id.email_login);
        inputPassword = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.btnLogin);
    }

    // Assign a fixed background image to the activity
    private void setBackgroundImage() {
        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        backgroundImage.setImageResource(R.drawable.garbage);
    }

    // Fetch the stored email from SharedPreferences
    private void restoreSavedEmail() {
        sharedPreferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(PREF_KEY_EMAIL, "");
        if (!savedEmail.isEmpty()) {
            inputEmail.setText(savedEmail);
        }
    }

    // Configure the Sign-Up button to navigate SignUpActivity
    private void setupSignUpButton() {
        TextView signUpButton = findViewById(R.id.btnsignupredirect);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // Configure the Login button to validate inputs and navigate to MainActivity
    private void setupLoginButton() {
        loginButton.setOnClickListener(v -> {
            // Verify the correctness of user-provided inputs
            if (isInputValid()) {
                // Store the email in SharedPreferences for future use
                saveEmailToPreferences();
                // Redirect the user to MainActivity
                navigateToMainActivity();
            }
        });
    }

    private boolean isInputValid() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check if the email is in a valid format
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Enter a valid email address");
            return false;
        }

        // Check if the password fulfills the specified criteria
        if (password.isEmpty()) {
            inputPassword.setError("Password cannot be empty");
            return false;
        }

        // Confirm the password adheres to security requirements
        if (!Pattern.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", password)) {
            inputPassword.setError("Password must be at least 8 characters, include a digit and a special character");
            return false;
        }

        return true;
        // Indicate success if all validation checks are passed
    }

    // Save the entered email to SharedPreferences
    private void saveEmailToPreferences() {
        String email = inputEmail.getText().toString().trim();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY_EMAIL, email);
        editor.apply(); // Apply changes to SharedPreferences
    }

    // Redirect to MainActivity upon successful login
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        // Terminate LoginActivity
    }

    // Lifecycle Activities

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOGIN_ACTIVITY, "onResume called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOGIN_ACTIVITY, "onStart called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOGIN_ACTIVITY, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOGIN_ACTIVITY, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOGIN_ACTIVITY, "onDestroy called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOGIN_ACTIVITY, "onSaveInstanceState called");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(LOGIN_ACTIVITY, "onRestoreInstanceState called");
    }
}