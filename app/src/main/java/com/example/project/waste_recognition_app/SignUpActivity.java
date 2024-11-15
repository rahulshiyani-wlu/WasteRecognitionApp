package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText userNameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // Set the activity layout

        initializeUIComponents();
        setSignUpButtonListener();
    }

    private void initializeUIComponents() {
        userNameInput = findViewById(R.id.user_name_sign_up);
        emailInput = findViewById(R.id.email_sign_up);
        passwordInput = findViewById(R.id.password_sign_up);
        confirmPasswordInput = findViewById(R.id.confirm_password_sign_up);
        signUpButton = findViewById(R.id.sign_up_btn);
    }

    private void setSignUpButtonListener() {
        signUpButton.setOnClickListener(v -> {
            if (validateInput()) {
                navigateToMainActivity();
            }
        });
    }

    // Verify the correctness of user inputs in the signup form
    private boolean validateInput() {
        String username = userNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (username.isEmpty()) {
            userNameInput.setError("Username is required");
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            return false;
        }

        if (!isValidPassword(password)) {
            passwordInput.setError("Password must be at least 8 characters, include a digit, and a special character");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}