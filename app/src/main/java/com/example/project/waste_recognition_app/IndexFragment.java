package com.example.project.waste_recognition_app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {

    private static final String TAG = "IndexFragment";

    private List<Item> itemList = new ArrayList<>();
    private Item.ItemListAdapter itemListAdapter;
    private ListView listView;
    private TextInputEditText searchInputEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        initializeViews(view);

        // Initialize adapter and set it to ListView
        setupAdapter();

        // Fetch data from Firestore
        fetchDataFromFirestore();

        // Setup search functionality
        setupSearchListener();
    }

    // Method to initialize views
    private void initializeViews(View view) {
        listView = view.findViewById(R.id.listView);
        searchInputEditText = view.findViewById(R.id.searchInputEditText);
    }

    // Method to initialize the adapter and set it to ListView
    private void setupAdapter() {
        itemListAdapter = new Item.ItemListAdapter(requireContext(), new ArrayList<>());
        listView.setAdapter(itemListAdapter);
    }

    // Fetch data from Firestore and update the item list
    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e(TAG, "User not logged in. Cannot fetch items.");
            return;
        }

        db.collection("users").document(userId).collection("scanned_items")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error fetching Firestore data", error);
                        return;
                    }

                    if (value != null) {
                        itemList.clear();
                        for (DocumentSnapshot document : value) {
                            String id = document.getId();
                            String type = document.getString("binType");
                            String img = document.getString("img");
                            String date = document.getString("date");
                            String time = document.getString("time");

                            if (id != null && type != null && img != null && date != null && time != null) {
                                itemList.add(new Item(id, type, img, date, time));
                            } else {
                                Log.e(TAG, "Invalid item data in Firestore: ID=" + id);
                            }
                        }

                        Log.d(TAG, "Items fetched successfully. Total items: " + itemList.size());
                        itemListAdapter.updateList(itemList);
                    }
                });
    }

    // Set up a text listener for search input to filter items in the list
    private void setupSearchListener() {
        searchInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItemList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    // Filter the item list based on user query and update the adapter
    private void filterItemList(String query) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : itemList) {
            // Match type (Recycle, Organic, etc.) with the search query
            if (item.getType().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update the adapter with the filtered data
        itemListAdapter.updateList(filteredList);
    }
}