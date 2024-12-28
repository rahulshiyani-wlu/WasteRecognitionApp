package com.example.project.waste_recognition_app;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class Item {

    private String id;
    private String type;
    private String img;
    private String date;
    private String time;

    public Item(String id, String type, String img, String date, String time) {
        this.id = id;
        this.type = type;
        this.img = img;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getImg() {
        return img;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public static class ItemListAdapter extends ArrayAdapter<Item> {

        private final Context context;
        private final List<Item> items;

        public ItemListAdapter(@NonNull Context context, @NonNull List<Item> items) {
            super(context, R.layout.list_item_layout, items);
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.itemImage = convertView.findViewById(R.id.itemImage);
                viewHolder.itemType = convertView.findViewById(R.id.textView_itemType);
                viewHolder.deleteIcon = convertView.findViewById(R.id.deleteIcon);
                viewHolder.infoIcon = convertView.findViewById(R.id.infoIcon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Item currentItem = items.get(position);
            viewHolder.itemType.setText(currentItem.getType());

            // Load the item's image using Glide
            Glide.with(context)
                    .load(currentItem.getImg())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(viewHolder.itemImage);

            // Load the delete icon GIF using Glide
            Glide.with(context)
                    .asGif()
                    .load(R.raw.delete_icon)
                    .into(viewHolder.deleteIcon);

            // Load the info icon GIF using Glide
            Glide.with(context)
                    .asGif()
                    .load(R.raw.info_animation)
                    .into(viewHolder.infoIcon);

            // Set click listener for delete icon
            viewHolder.deleteIcon.setOnClickListener(v -> {
                if (!isNetworkAvailable()) {
                    Toast.makeText(context, "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                        FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

                if (userId == null) {
                    Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show();
                    Log.e("DeleteItem", "User not logged in. Cannot delete item.");
                    return;
                }

                String itemId = currentItem.getId();
                if (itemId == null || itemId.isEmpty()) {
                    Toast.makeText(context, "Invalid item ID. Cannot delete.", Toast.LENGTH_SHORT).show();
                    Log.e("DeleteItem", "Invalid item ID: " + itemId);
                    return;
                }

                Log.d("DeleteItem", "Attempting to delete item with ID: " + itemId);

                db.collection("users").document(userId).collection("scanned_items")
                        .document(itemId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Item deleted successfully.", Toast.LENGTH_SHORT).show();
                            Log.d("DeleteItem", "Item deleted successfully: " + itemId);
                            // No need to manually remove the item here; Firestore snapshot listener will handle it.
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Failed to delete item. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("DeleteItem", "Error deleting item: " + itemId, e);
                        });
            });

            // Set click listener for info icon
            viewHolder.infoIcon.setOnClickListener(v -> showInfoDialog(currentItem));

            return convertView;
        }

        // Method to update the list of items in the adapter
        public void updateList(List<Item> updatedItems) {
            items.clear();
            items.addAll(updatedItems);
            notifyDataSetChanged();
        }

        // Show a dialog with item details (date and time)
        private void showInfoDialog(Item item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_info, null);

            TextView dateTextView = dialogView.findViewById(R.id.textView_date);
            TextView timeTextView = dialogView.findViewById(R.id.textView_time);

            dateTextView.setText("Date: " + item.getDate());
            timeTextView.setText("Time: " + item.getTime());

            builder.setView(dialogView)
                    .setTitle("Item Information")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }

        // Check if the network is available
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        // ViewHolder pattern to improve ListView performance
        private static class ViewHolder {
            ImageView itemImage;
            TextView itemType;
            ImageView deleteIcon;
            ImageView infoIcon;
        }
    }
}