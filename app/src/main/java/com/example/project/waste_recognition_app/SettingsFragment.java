package com.example.project.waste_recognition_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    private void openProfileSettings() {
        try {
            Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("SettingsFragment", "Error opening ProfileSettingsActivity: " + e.getMessage());
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        try {
            Log.d("SettingsFragment", "onCreatePreferences() called");
            setPreferencesFromResource(R.xml.settings, rootKey);

            // Set click listeners for the preferences
            Preference signOutPreference = findPreference("sign_out");
            if (signOutPreference != null) {
                signOutPreference.setOnPreferenceClickListener(preference -> {
                    signOut();
                    return true;
                });
            }

            Preference profileSettingsPreference = findPreference("profile_settings");
            if (profileSettingsPreference != null) {
                profileSettingsPreference.setOnPreferenceClickListener(preference -> {
                    openProfileSettings();
                    return true;
                });
            }

            Preference changePasswordPreference = findPreference("change_password");
            if (changePasswordPreference != null) {
                changePasswordPreference.setOnPreferenceClickListener(preference -> {
                    changePassword();
                    return true;
                });
            }
        } catch (Exception e) {
            Log.e("SettingsFragment", "Error in onCreatePreferences(): " + e.getMessage());
        }

        Preference changePasswordPreference = findPreference("change_password");
        if (changePasswordPreference != null) {
            changePasswordPreference.setOnPreferenceClickListener(preference -> {
                changePassword();
                return true;
            });
        }
    }

    private void signOut() {
        // For debugging: Check if this method is called
        Toast.makeText(getActivity(), "Signing out...", Toast.LENGTH_SHORT).show();

        // Clear user session data
        getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Redirect to login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish(); // Optional: Close the settings/activity
    }

    // Method to handle Change Password
    private void changePassword() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    // This may not be needed unless you plan to interact with the activity
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
