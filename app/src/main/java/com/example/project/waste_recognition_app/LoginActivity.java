package com.example.project.waste_recognition_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_INFO = "Login Activity";
    private static final String RGS_MAIL = "RahulFile";
    private static final String DOMAIN_MAIL = "LoginNullEmail";

    private TextInputEditText userAuthLoginEmail;
    private TextInputEditText userAuthLoginPassword;
    private Button userLoginBtn;
    private SharedPreferences sharedPreferences;

    public void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGIN_INFO, "onCreate called");
        setContentView(R.layout.activity_login);

        initializeViews();
        setupBackgroundImage();
        loadSavedEmail();

        // Set the OnClickListener for the Sign Up button
        TextView user_sign_up_btn = findViewById(R.id.btnsignupredirect);
        user_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Set the OnClickListener for the Login button
        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    saveEmail();
                    navigateToMainActivity();
                }
            }
        });
    }

    public void open_SignUp_Page(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @SuppressLint("WrongViewCast")
    private void initializeViews() {
        userAuthLoginEmail = findViewById(R.id.email_login);
        userAuthLoginPassword = findViewById(R.id.password_login);
        userLoginBtn = findViewById(R.id.btnLogin);
    }

    private void setupBackgroundImage() {
        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        backgroundImage.setImageResource(R.drawable.garbage);
    }

    private void loadSavedEmail() {
        sharedPreferences = getSharedPreferences(RGS_MAIL, MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(DOMAIN_MAIL, "");
        if (!savedEmail.isEmpty()) {
            userAuthLoginEmail.setText(savedEmail);
        }
    }

    private void setupLoginButton() {
        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    saveEmail();
                    navigateToMainActivity();
                }
            }
        });
    }

    private boolean validateInput() {
        String email = userAuthLoginEmail.getText().toString().trim();
        String password = userAuthLoginPassword.getText().toString().trim();

        // Validate email
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userAuthLoginEmail.setError("Enter a valid email address");
            return false;
        }

        // Validate password
        if (password.isEmpty()) {
            userAuthLoginPassword.setError("Password cannot be empty");
            return false;
        }

        // Check password requirements
        if (!Pattern.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", password)) {
            userAuthLoginPassword.setError("Password must be at least 8 characters, include a digit and a special character");
            return false;
        }

        return true; // All validations passed
    }

    private void saveEmail() {
        String email = userAuthLoginEmail.getText().toString().trim();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DOMAIN_MAIL, email);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Lifecycle methods
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOGIN_INFO, "onResume called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOGIN_INFO, "onStart called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOGIN_INFO, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOGIN_INFO, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOGIN_INFO, "onDestroy called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOGIN_INFO, "onSaveInstanceState called");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(LOGIN_INFO, "onRestoreInstanceState called");
    }

}