package com.example.project.waste_recognition_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class Item {
    private final String name;
    private final String info;
    private final String type;
    private final String img;

    // Constructor
    public Item(String img, String name, String type, String info) {
        this.name = capitalizeWord(name);
        this.info = info;
        this.img = img;
        this.type = type;
    }

    // Getters for the Item properties
    public String getName() {
        return capitalizeWord(name);
    }

    public String getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }

    public String getImg() {
        return img;
    }

    // Utility method for capitalizing words
    private static String capitalizeWord(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder capitalized = new StringBuilder();
        char prevChar = ' ';

        for (char currentChar : str.toCharArray()) {
            if (prevChar == ' ' && currentChar != ' ') {
                capitalized.append(Character.toUpperCase(currentChar));
            } else {
                capitalized.append(currentChar);
            }
            prevChar = currentChar;
        }

        return capitalized.toString().trim();
    }

    // Adapter Class for displaying Item objects in a ListView
    public static class ItemListAdapter extends ArrayAdapter<Item> {

        private final Context context;
        private final List<Item> items;

        // Constructor
        public ItemListAdapter(@NonNull Context context, @NonNull List<Item> items) {
            super(context, R.layout.list_item_layout, items);
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;

            // Inflate layout and set up ViewHolder if convertView is null
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_item_layout, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.itemImage = convertView.findViewById(R.id.itemImage);
                viewHolder.itemName = convertView.findViewById(R.id.textView_itemName);
                viewHolder.itemInfo = convertView.findViewById(R.id.textView_itemInfo);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Bind data to the ViewHolder
            Item currentItem = items.get(position);
            viewHolder.itemName.setText(currentItem.getName());
            viewHolder.itemInfo.setText(currentItem.getInfo());

            // Load image with Glide or set default placeholder
            if (currentItem.getImg() != null && !currentItem.getImg().isEmpty()) {
                Glide.with(context)
                        .load(currentItem.getImg())
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(viewHolder.itemImage);
            } else {
                viewHolder.itemImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            return convertView;
        }

        // Update the adapter's dataset
        public void updateList(List<Item> newList) {
            items.clear();
            items.addAll(newList);
            notifyDataSetChanged();
        }

        // ViewHolder class for performance optimization
        private static class ViewHolder {
            ImageView itemImage;
            TextView itemName;
            TextView itemInfo;
        }
    }
}