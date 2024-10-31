
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

    private TextInputEditText UserNameSignUp;
    private TextInputEditText EmailSignUP;
    private TextInputEditText PasswordSignUp;
    private TextInputEditText ConfirmPasswordSignUp;
    private Button SignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        UserNameSignUp = findViewById(R.id.user_name_sign_up);
        EmailSignUP = findViewById(R.id.email_sign_up);
        PasswordSignUp = findViewById(R.id.password_sign_up);
        ConfirmPasswordSignUp = findViewById(R.id.confirm_password_sign_up);
        SignUpBtn = findViewById(R.id.sign_up_btn);

        // Set click listener for Sign Up button
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
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
        String username = UserNameSignUp.getText().toString().trim();
        String email = EmailSignUP.getText().toString().trim();
        String password = PasswordSignUp.getText().toString().trim();
        String confirmPassword = ConfirmPasswordSignUp.getText().toString().trim();

        // Validate username: not empty
        if (username.isEmpty()) {
            UserNameSignUp.setError("Username is required");
            return false;
        }

        // Validate email format
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailSignUP.setError("Enter a valid email address");
            return false;
        }

        // Validate password: at least 8 characters, 1 digit, 1 special character
        if (password.isEmpty() || !Pattern.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", password)) {
            PasswordSignUp.setError("Password must be at least 8 characters, include a digit and a special character");
            return false;
        }

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            ConfirmPasswordSignUp.setError("Passwords do not match");
            return false;
        }

        // All validations passed
        return true;
    }
}
