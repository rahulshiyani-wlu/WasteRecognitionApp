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
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_ACTIVITY = "LoginActivity";

    private TextInputEditText inputEmailOrUsername, inputPassword;
    private Button loginButton;
    private TextView forgotPasswordTextView;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGIN_ACTIVITY, "onCreate called");
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        initializeUIComponents();
        setBackgroundImage();
        setupSignUpButton();
        setupForgotPasswordButton();
        setupLoginButton();
    }

    // Method to log in the user
    private void loginUser() {
        String input = inputEmailOrUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (input.isEmpty() || password.isEmpty()) {
            showToast("Username/Email and password must not be empty");
            return;
        }

        // Check if input is an email or username
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            // Input is an email
            auth.signInWithEmailAndPassword(input, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            fetchUserDataAndProceed();
                        } else {
                            showToast("Login failed: " + task.getException().getMessage());
                        }
                    });
        } else {
            // Input is a username
            db.collection("users")
                    .whereEqualTo("username", input)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String email = queryDocumentSnapshots.getDocuments().get(0).getString("email");
                            if (email != null) {
                                auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                fetchUserDataAndProceed();
                                            } else {
                                                showToast("Login failed: " + task.getException().getMessage());
                                            }
                                        });
                            }
                        } else {
                            showToast("No user found with the given username");
                        }
                    })
                    .addOnFailureListener(e -> showToast("Failed to fetch user: " + e.getMessage()));
        }
    }

    // Method to initialize UI components
    @SuppressLint("WrongViewCast")
    private void initializeUIComponents() {
        inputEmailOrUsername = findViewById(R.id.email_login);
        inputPassword = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.btnLogin);
        forgotPasswordTextView = findViewById(R.id.forgot_password);
    }

    // Method to set the background image for the activity
    private void setBackgroundImage() {
        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        backgroundImage.setImageResource(R.drawable.garbage);
    }

    // Method to set up the sign-up button
    private void setupSignUpButton() {
        TextView signUpButton = findViewById(R.id.btnsignupredirect);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // Method to set up the forgot password button
    private void setupForgotPasswordButton() {
        forgotPasswordTextView.setOnClickListener(v -> {
            showToast("Password Recovery Page");
            Intent intent = new Intent(LoginActivity.this, PasswordRecovery.class);
            startActivity(intent);
        });
    }

    // Method to show a toast message
    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to set up the login button
    private void setupLoginButton() {
        loginButton.setOnClickListener(v -> loginUser());
    }

    // Method to navigate to the main activity
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Fetch user data from Firestore and proceed to main activity
    private void fetchUserDataAndProceed() {
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");

                        // Save user data to SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        prefs.edit()
                                .putString("username", username)
                                .putString("email", email)
                                .apply();

                        showToast("Login successful");
                        navigateToMainActivity();
                    }
                })
                .addOnFailureListener(e -> showToast("Failed to fetch user data: " + e.getMessage()));
    }
}