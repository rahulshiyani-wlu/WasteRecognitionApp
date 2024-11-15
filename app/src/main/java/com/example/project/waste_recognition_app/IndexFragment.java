package com.example.project.waste_recognition_app;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

    private List<Item> originalItemList = new ArrayList<>();
    private Item.Item_List_Adapter itemListAdapter;
    private ListView listView;
    private ProgressDialog progressDialog;
    private TextInputEditText searchInputField;

    @Keep
    public IndexFragment() {
//        Default empty constructor required for fragment initialization
    }

    @Override
    @Keep
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Keep
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    @Keep
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);
        searchInputField = view.findViewById(R.id.searchInputEditText);

        itemListAdapter = new Item.Item_List_Adapter(getContext(), new ArrayList<>());
        listView.setAdapter(itemListAdapter);

        setupProgressDialog();

        fetchDataFromFirestore();

        setupSearchFunctionality();

        setupScrollListener();
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Loading Items...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("more_info")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        originalItemList.clear();
                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Item item = new Item(
                                    (String) document.get("img"),
                                    (String) document.get("name"),
                                    (String) document.get("type"),
                                    (String) document.get("info")
                            );

                            if (item.getName() != null) {
                                originalItemList.add(item);
                            }
                        }

//                        Sort the list of items in alphabetical order based on their names
                        Collections.sort(originalItemList, (o1, o2) -> o1.getName().compareTo(o2.getName()));

                        itemListAdapter.updateList(new ArrayList<>(originalItemList));
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                    dismissProgressDialog();
                });
    }

    private void setupSearchFunctionality() {
        searchInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action required for this method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action required for this method
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
            }
        });
    }

    private void filterList(String query) {
        List<Item> filteredList = new ArrayList<>();
        if (!query.isEmpty()) {
            for (Item item : originalItemList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        } else {
            filteredList.addAll(originalItemList);
        }
        itemListAdapter.updateList(filteredList);
    }

    private void setupScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Placeholder for future implementation if possible
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Placeholder for future implementation if possible
            }
        });
    }

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
        dismissProgressDialog();
        progressDialog = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}