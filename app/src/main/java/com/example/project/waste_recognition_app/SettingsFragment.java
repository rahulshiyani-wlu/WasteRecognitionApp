package com.example.project.waste_recognition_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey); // Link to your preferences XML file

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


//    // Method to handle Sign Out
//    private void signOut() {
//        // Clear user session data, e.g., SharedPreferences
//        getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//                .edit()
//                .clear() // Or remove specific keys e.g. edit().remove("your_key").apply();
//                .apply();
//
//        // Redirect to login activity
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        getActivity().finish(); // Optional: Close the settings activity
//    }

    // Method to handle Profile Settings
    private void openProfileSettings() {
        Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
        startActivity(intent);
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



//package com.example.project.waste_recognition_app;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//
//import androidx.preference.Preference;
//import androidx.preference.PreferenceFragmentCompat;
//
//
//public class SettingsFragment extends PreferenceFragmentCompat {
//
//    private OnFragmentInteractionListener mListener;
//
//    public SettingsFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        setPreferencesFromResource(R.xml.settings, rootKey); // Link to your preferences XML file
//
//        // Set click listeners for the preferences
//        Preference signOutPreference = findPreference("sign_out");
//        if (signOutPreference != null) {
//            signOutPreference.setOnPreferenceClickListener(preference -> {
//                signOut();
//                return true;
//            });
//        }
//
//        Preference profileSettingsPreference = findPreference("profile_settings");
//        if (profileSettingsPreference != null) {
//            profileSettingsPreference.setOnPreferenceClickListener(preference -> {
//                openProfileSettings();
//                return true;
//            });
//        }
//
//        Preference changePasswordPreference = findPreference("change_password");
//        if (changePasswordPreference != null) {
//            changePasswordPreference.setOnPreferenceClickListener(preference -> {
//                changePassword();
//                return true;
//            });
//        }
//    }
//
//    // Method to handle Sign Out
//    private void signOut() {
//        // Implement sign-out logic here
//        // For example, clear user data or session and redirect to login screen
//    }
//
//    // Method to handle Profile Settings
//    private void openProfileSettings() {
//        Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
//        startActivity(intent);
//    }
//
//    // Method to handle Change Password
//    private void changePassword() {
//        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
//        startActivity(intent);
//    }
//
//    // TODO: This may not be needed unless you plan to interact with the activity
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//}


//package com.example.project.waste_recognition_app;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//
////TODO: Add this link: http://peelregion.ca/waste/calendar/ to people wanting to know their pickup schedule.
//// Also turn the settings fragment into something similar to Dark Mode's setting activity (https://play.google.com/store/apps/details?id=systems.maju.darkmode&hl=en)
//// Don't forget to add contributors as well!
//
//public class SettingsFragment extends Fragment {
//
//    private OnFragmentInteractionListener mListener;
//
//    public SettingsFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_settings, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
