package com.example.project.waste_recognition_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    // Flag to notify MainActivity about settings changes
    public static boolean isSettingsChanged = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load preferences from the XML resource
        setPreferencesFromResource(R.xml.settings, rootKey);

        configureDarkModeToggle();       // Handle dark mode changes
        configureSignOutPreference();   // Handle sign-out option
        configureProfileSettingsOption(); // Navigate to Profile Settings
        configureChangePasswordOption(); // Navigate to Change Password
        configureContributorsDialog();  // Display contributors information
        configureFeedbackOption();      // Handle feedback email option
    }

    private void configureDarkModeToggle() {
        SwitchPreferenceCompat darkModePreference = findPreference("night_mode");
        if (darkModePreference != null) {
            darkModePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnabled = (boolean) newValue;
                AppCompatDelegate.setDefaultNightMode(
                        isEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                );
                isSettingsChanged = true; // Notify MainActivity
                return true;
            });
        }
    }

    private void configureSignOutPreference() {
        Preference signOutPreference = findPreference("sign_out");
        if (signOutPreference != null) {
            signOutPreference.setOnPreferenceClickListener(preference -> {
                performSignOut();
                return true;
            });
        }
    }

    private void configureProfileSettingsOption() {
        Preference profileSettingsPreference = findPreference("profile_settings");
        if (profileSettingsPreference != null) {
            profileSettingsPreference.setOnPreferenceClickListener(preference -> {
                navigateToActivity(ProfileSettingsActivity.class);
                return true;
            });
        }
    }

    private void configureChangePasswordOption() {
        Preference changePasswordPreference = findPreference("change_password");
        if (changePasswordPreference != null) {
            changePasswordPreference.setOnPreferenceClickListener(preference -> {
                navigateToActivity(ChangePasswordActivity.class);
                return true;
            });
        }
    }

    private void configureContributorsDialog() {
        Preference contributorsPreference = findPreference("contributors");
        if (contributorsPreference != null) {
            contributorsPreference.setOnPreferenceClickListener(preference -> {
                showDialog("Contributors", "Main Developer: Group 3\nAssisting Developer: Group 3\nTester: Group 3");
                return true;
            });
        }
    }

    private void configureFeedbackOption() {
        Preference feedbackPreference = findPreference("feedback");
        if (feedbackPreference != null) {
            feedbackPreference.setOnPreferenceClickListener(preference -> {
                sendFeedbackEmail();
                return true;
            });
        }
    }

    private void performSignOut() {
        if (getActivity() != null) {
            getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(getActivity(), "Signed out successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToActivity(Class<?> activityClass) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), activityClass);
            startActivity(intent);
        }
    }

    private void showDialog(String title, String message) {
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                    .create()
                    .show();
        }
    }

    private void sendFeedbackEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:daarv@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for the Waste Recognition App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello!\n\nWhile using the Waste Recognition App, I found something to share. [Insert your feedback]");
        if (emailIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(getActivity(), "No email client available", Toast.LENGTH_SHORT).show();
        }
    }
}