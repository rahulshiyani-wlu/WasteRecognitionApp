package com.example.project.waste_recognition_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        // Retrieve old password from SharedPreferences for verification
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedPassword = prefs.getString("password", null); // Assume password is stored

        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();

        if (savedPassword != null && savedPassword.equals(oldPassword)) {
            // Change the password
            prefs.edit().putString("password", newPassword).apply();
            Toast.makeText(this, "Password successfully changed", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}
