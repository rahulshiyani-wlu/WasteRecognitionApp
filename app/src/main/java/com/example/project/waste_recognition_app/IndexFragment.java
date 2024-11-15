package com.example.project.waste_recognition_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Keep
public class IndexFragment extends Fragment {

    private static final String TAG = "IndexFragment";

    // Variables for item list, adapter, and UI elements
    private List<Item> itemList;
    private Item.ItemListAdapter itemAdapter;
    private ListView listView;
    private ProgressDialog progressDialog;
    private TextInputEditText searchField;

    @Keep
    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    @Keep
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Keep
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    @Keep
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firestore instance and UI elements
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        listView = view.findViewById(R.id.listView);
        searchField = view.findViewById(R.id.searchInputEditText);

        // Initialize item list and adapter
        itemList = new ArrayList<>();
        itemAdapter = new Item.ItemListAdapter(requireContext(), new ArrayList<>(itemList));
        listView.setAdapter(itemAdapter);

        // Show loading dialog
        setupProgressDialog();

        // Fetch data from Firestore
        fetchDataFromFirestore(db);

        // Setup search functionality (only triggers when user interacts with the search field)
        setupSearchFunctionality();
    }

    // Show and configure the progress dialog
    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Fetching items...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    // Fetch data from Firestore and Update the List
    private void fetchDataFromFirestore(FirebaseFirestore db) {
        db.collection("more_info").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                itemList.clear();
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Item item = new Item(
                            (String) document.get("img"),
                            (String) document.get("name"),
                            (String) document.get("type"),
                            (String) document.get("info")
                    );
                    if (item.getName() != null) {
                        itemList.add(item);
                    }
                }
                // Sort items alphabetically
                Collections.sort(itemList, (o1, o2) -> o1.getName().compareTo(o2.getName()));

                // Update adapter with new data
                itemAdapter.updateList(new ArrayList<>(itemList));
            } else {
                Log.w(TAG, "Error fetching documents", task.getException());
            }
            dismissProgressDialog();
        });
    }

    // Setup search functionality for filtering the list (only triggered on user interaction)
    private void setupSearchFunctionality() {
        searchField.setOnClickListener(v -> {
            searchField.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No action required here
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // No action required here
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    List<Item> filteredList = new ArrayList<>();
                    if (!s.toString().isEmpty()) {
                        for (Item item : itemList) {
                            if (item.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                                filteredList.add(item);
                            }
                        }
                    } else {
                        filteredList.addAll(itemList);
                    }
                    itemAdapter.updateList(filteredList);
                }
            });
        });
    }

    // Dismiss progress dialog
    @Keep
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    @Keep
    public void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    @Override
    @Keep
    public void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }

}