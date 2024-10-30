
package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Set click listener for Sign Up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // If validation passes, navigate to MainActivity
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close SignUpActivity if needed
                }
            }
        });
    }

    private boolean validateInput() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate username: not empty
        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            return false;
        }

        // Validate email format
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            return false;
        }

        // Validate password: at least 8 characters, 1 digit, 1 special character
        if (password.isEmpty() || !Pattern.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", password)) {
            etPassword.setError("Password must be at least 8 characters, include a digit and a special character");
            return false;
        }

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }

        // All validations passed
        return true;
    }
}

//package com.example.wastemanagementsystem;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.wastemanagementsystem.R;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//import java.util.regex.Pattern;
//
//public class SignUpActivity extends AppCompatActivity {
//
//    private TextInputLayout usernameLayout, emailLayout, passwordLayout, confirmPasswordLayout;
//    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
//    private MaterialButton btnSignUp;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        // Initialize fields and layouts
////        usernameLayout = findViewById(R.id.usernameLayout);
////        emailLayout = findViewById(R.id.emailLayout);
////        passwordLayout = findViewById(R.id.passwordLayout);
////        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
//        etUsername = findViewById(R.id.etUsername);
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        etConfirmPassword = findViewById(R.id.etConfirmPassword);
//        btnSignUp = findViewById(R.id.btnSignUp);
//
//        // Add listeners
//        addListeners();
//    }
//
//    private void addListeners() {
//        // Handle username change
//        etUsername.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                handleUsernameChange(s.toString());
//            }
//            // Not used but required for TextWatcher
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        // Handle password change
//        etPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                handlePasswordChange(s.toString());
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        // Handle form submit
//        btnSignUp.setOnClickListener(view -> handleFormSubmit());
//    }
//
//    // Methods for Form Handling & Validation
//    private void handleUsernameChange(String username) {
//        if (username.length() < 3 || username.length() > 15) {
//            usernameLayout.setError("Username must be 3-15 characters.");
//        } else {
//            usernameLayout.setError(null);
//        }
//    }
//
//    private void handlePasswordChange(String password) {
//        if (!isValidPassword(password)) {
//            passwordLayout.setError("Password must be at least 8 characters, include an uppercase, lowercase, digit, and special character.");
//        } else {
//            passwordLayout.setError(null);
//        }
//    }
//
//    private void handleFormSubmit() {
//        if (validateForm()) {
//            signupUser(etUsername.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());
//            clearFormFields();
//        }
//    }
//
//    private void clearFormFields() {
//        etUsername.setText("");
//        etEmail.setText("");
//        etPassword.setText("");
//        etConfirmPassword.setText("");
//    }
//
//    private void togglePasswordVisibility() {
//        // Toggle visibility for password fields
//        if (etPassword.getInputType() == 129) { // 129: TYPE_TEXT_VARIATION_PASSWORD
//            etPassword.setInputType(1); // 1: TYPE_CLASS_TEXT
//            etConfirmPassword.setInputType(1);
//        } else {
//            etPassword.setInputType(129);
//            etConfirmPassword.setInputType(129);
//        }
//    }
//
//    private void showFormError(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void showFormSuccess(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void handleInputBlur(View view) {
//        if (view == etUsername) {
//            handleUsernameChange(etUsername.getText().toString());
//        } else if (view == etPassword) {
//            handlePasswordChange(etPassword.getText().toString());
//        }
//    }
//
//    private boolean validateForm() {
//        return !TextUtils.isEmpty(etUsername.getText()) &&
//                Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches() &&
//                isValidPassword(etPassword.getText().toString()) &&
//                etPassword.getText().toString().equals(etConfirmPassword.getText().toString());
//    }
//
//    private boolean isValidPassword(String password) {
//        Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
//        return PASSWORD_PATTERN.matcher(password).matches();
//    }
//
//    // API Calls
//    private void loginUser(String email, String password) {
//        // Implement login API call logic here
//        showFormSuccess("Login successful for " + email);
//    }
//
//    private void signupUser(String username, String email, String password) {
//        // Implement signup API call logic here
//        showFormSuccess("Sign-Up successful for " + username);
//    }
//
//    private void checkUserExists(String username) {
//        // Implement user existence check here
//        boolean exists = false; // Replace with actual API check
//        if (exists) {
//            showFormError("Username already taken.");
//        } else {
//            showFormSuccess("Username is available.");
//        }
//    }
//}




//package com.example.wastemanagementsystem;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SignUpActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        Button user_sign_up_btn = findViewById(R.id.btnSignUp);
//
//        user_sign_up_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignUpActivity .this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}