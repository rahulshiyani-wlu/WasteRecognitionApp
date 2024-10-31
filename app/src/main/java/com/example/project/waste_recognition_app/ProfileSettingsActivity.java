package com.example.project.waste_recognition_app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    private EditText UserNameUpdate, UserEmailUpdate;
    private Button UserBtnUpdate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileSettingsActivity", "onCreate() called");

        setContentView(R.layout.activity_profile_settings);

        UserNameUpdate = findViewById(R.id.user_name_update);
        UserEmailUpdate = findViewById(R.id.user_email_update);
        UserBtnUpdate = findViewById(R.id.update_profile_btn);

        // Load existing user data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        UserNameUpdate.setText(prefs.getString("username", ""));
        UserEmailUpdate.setText(prefs.getString("email", ""));

        UserBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        // Save updated profile data
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putString("username", UserNameUpdate.getText().toString())
                .putString("email", UserEmailUpdate.getText().toString())
                .apply();

        Toast.makeText(this, "Profile successfully updated", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity
    }
}
