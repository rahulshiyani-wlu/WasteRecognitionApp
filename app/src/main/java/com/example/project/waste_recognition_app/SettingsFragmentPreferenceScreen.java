package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragmentPreferenceScreen extends PreferenceFragmentCompat {

    static Boolean openSettingsNotifier = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.settings, rootKey);

        // Dark Mode
        SwitchPreferenceCompat signaturePreference = findPreference("night_mode");

        if (signaturePreference != null){
            signaturePreference.setOnPreferenceClickListener(preference -> {
                if (signaturePreference.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                openSettingsNotifier = true;
                if (getActivity() != null) getActivity().recreate();

                return false;
            });
        }

        // contributors
        Preference contributorsPreference = findPreference("contributors");
        if (contributorsPreference != null) {
            contributorsPreference.setOnPreferenceClickListener(preference -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

                builder.setMessage("Main Developer: Group 3\nAssisting Developer: Group 3\nTester: Group 3")
                        .setTitle("Contributors");

                builder.setPositiveButton("OK", (dialog, id) -> {
                    // User clicked OK button
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            });
        }

        // legend
        Preference legendPreference = findPreference("index_legend");
        if (legendPreference != null) {
            legendPreference.setOnPreferenceClickListener(preference -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

                builder.setTitle("Legend");

                builder.setPositiveButton("OK", (dialog, id) -> {
                    // User clicked OK button
                    dialog.dismiss();
                });

                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_legend, null);
                builder.setView(view);

                AlertDialog dialog = builder.create();

                dialog.show();

                return false;
            });
        }

        // feedback
        Preference feedbackPreference = findPreference("feedback");
        if (feedbackPreference != null) {
            feedbackPreference.setOnPreferenceClickListener(preference -> {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

                String mailto = "mailto:daarv@gmail.com" +
                        "?subject=" + Uri.encode("Feedback for the Waste Recognition App") +
                        "&body=" + Uri.encode("Hello!\n\nWhile using the Waste Recognition App, I found something / a few things that I would like to notify you of. [Please insert your feedback here]");

                emailIntent.setData(Uri.parse(mailto));
                startActivity(emailIntent);

                return false;
            });
        }

    }
}