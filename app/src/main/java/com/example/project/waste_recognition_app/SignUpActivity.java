package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText userNameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signUpButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth and Firestore instances
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeUIComponents();
        setSignUpButtonListener();
    }

    // Register a new user with the provided details
    private void registerUser() {
        String username = userNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validate if passwords match
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return;
        }

        // Create a new user with Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserToFirestore(username, email);
                    } else {
                        showToast("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    // Save the user information to Firestore
    private void saveUserToFirestore(String username, String email) {
        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Save user credentials to SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putString("username", username)
                            .putString("email", email)
                            .apply();

                    showToast("Registration successful");
                    navigateToMainActivity();
                })
                .addOnFailureListener(e -> showToast("Failed to save user: " + e.getMessage()));
    }

    // Show a toast message to the user
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Initialize UI components such as input fields and buttons
    private void initializeUIComponents() {
        userNameInput = findViewById(R.id.user_name_sign_up);
        emailInput = findViewById(R.id.email_sign_up);
        passwordInput = findViewById(R.id.password_sign_up);
        confirmPasswordInput = findViewById(R.id.confirm_password_sign_up);
        signUpButton = findViewById(R.id.sign_up_btn);
    }

    // Set up the listener for the sign-up button to validate and register a user
    private void setSignUpButtonListener() {
        signUpButton.setOnClickListener(v -> {
            if (validateInput()) {
                registerUser();
            }
        });
    }

    // Validate user input for username, email, and password
    private boolean validateInput() {
        String username = userNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (!isValidUsername(username)) {
            userNameInput.setError(
                    "Invalid username. Please follow these rules:\n" +
                            "1. Must be 3-20 characters long.\n" +
                            "2. Start and end with a letter or number.\n" +
                            "3. Allowed characters: A-Z, a-z, 0-9, _, ., -\n" +
                            "4. No consecutive special characters (e.g., .., __, --).\n" +
                            "5. No spaces allowed.\n" +
                            "6. Avoid reserved or offensive words."
            );
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

    // Validate the provided username based on custom rules
    private boolean isValidUsername(String username) {
        // Check for length
        if (username.length() < 3 || username.length() > 20) {
            return false;
        }

        // Ensure no spaces
        if (username.contains(" ")) {
            return false;
        }

        // Match against allowed pattern
        String usernamePattern = "^(?=.{3,20}$)(?!.*[._-]{2})[a-zA-Z0-9][a-zA-Z0-9._-]*[a-zA-Z0-9]$";
        if (!username.matches(usernamePattern)) {
            return false;
        }

        // Add any reserved or offensive word checks here if needed
        String[] reservedWords = {"admin", "root", "test"};
        for (String word : reservedWords) {
            if (username.equalsIgnoreCase(word)) {
                return false;
            }
        }

        return true;
    }

    // Validate if the provided password meets security requirements
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    // Navigate to the main activity after successful registration
    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}