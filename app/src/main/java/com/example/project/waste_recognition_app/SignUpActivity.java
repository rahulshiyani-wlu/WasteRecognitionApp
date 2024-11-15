package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    // Declare UI components
    private TextInputEditText userNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // Set the activity layout

        initializeUIComponents(); // Initialize UI components
        setSignUpButtonListener(); // Set functionality for the Sign Up button
    }

    // Initialize UI components by linking them to layout IDs
    private void initializeUIComponents() {
        userNameInput = findViewById(R.id.user_name_sign_up);
        emailInput = findViewById(R.id.email_sign_up);
        passwordInput = findViewById(R.id.password_sign_up);
        confirmPasswordInput = findViewById(R.id.confirm_password_sign_up);
        signUpButton = findViewById(R.id.sign_up_btn);
    }

    // Set up the Sign Up button to validate input and navigate to MainActivity
    private void setSignUpButtonListener() {
        signUpButton.setOnClickListener(v -> {
            if (validateInput()) { // Validate inputs
                navigateToMainActivity(); // Navigate to the main activity
            }
        });
    }

    // Validate user input for signup form
    private boolean validateInput() {
        String username = userNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Check if username is not empty
        if (username.isEmpty()) {
            userNameInput.setError("Username is required");
            return false;
        }

        // Validate email using Android's built-in email pattern
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            return false;
        }

        // Ensure password meets criteria: 8 characters, 1 digit, 1 special character
        if (!isValidPassword(password)) {
            passwordInput.setError("Password must be at least 8 characters, include a digit, and a special character");
            return false;
        }

        // Check if confirm password matches the password
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        }

        return true;
        // All validations passed
    }

    // Helper method to validate password criteria
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    // Navigate to MainActivity after successful signup
    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        // Close SignUpActivity
    }
}