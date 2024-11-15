package com.example.project.waste_recognition_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class Item {
    private String name;
    private String info;
    private String type;
    private String img;

    public Item(String img, String name, String type, String info) {
        this.name = capitailizeWord(name);
        this.info = info;
        this.img = img;
        this.type = type;
    }

    public String getName() {
        return capitailizeWord(name);
    }

    public String getInfo() {
        return info;
    }

    public String getImg() {
        return img;
    }

    private static String capitailizeWord(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder s = new StringBuilder();
        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {
            if (ch == ' ' && str.charAt(i) != ' ') {
                s.append(Character.toUpperCase(str.charAt(i)));
            } else {
                s.append(str.charAt(i));
            }
            ch = str.charAt(i);
        }
        return s.toString().trim();
    }

    // Nested adapter class to manage list items
    public static class Item_List_Adapter extends ArrayAdapter<Item> {
        private final Context context;
        private List<Item> items;

        public Item_List_Adapter(@NonNull Context context, @NonNull List<Item> items) {
            super(context, R.layout.list_item_layout, items);
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;

            // Create a new view or reuse an existing one for better performance
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_item_layout, parent, false);

                // Set up the ViewHolder to cache references to child views for efficient reuse
                viewHolder = new ViewHolder();
                viewHolder.itemImage = convertView.findViewById(R.id.itemImage);
                viewHolder.itemName = convertView.findViewById(R.id.textView_itemName);
                viewHolder.itemInfo = convertView.findViewById(R.id.textView_itemInfo);

                // Link the ViewHolder to the view for future efficient reuse
                convertView.setTag(viewHolder);
            } else {
                // Reuse the existing ViewHolder to improve scrolling performance
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Retrieve the current item to populate the view
            Item currentItem = items.get(position);

            // Populate the item's name and information into the corresponding views
            viewHolder.itemName.setText(currentItem.getName());
            viewHolder.itemInfo.setText(currentItem.getInfo());

            // Use Glide to load the item's image with a placeholder and fallback for errors
            if (currentItem.getImg() != null && !currentItem.getImg().isEmpty()) {
                Glide.with(context)
                        .load(currentItem.getImg())
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(viewHolder.itemImage);
            } else {
                // Display a default image when no image URL is available
                viewHolder.itemImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            // Add a slide-in animation to each list item as it becomes visible
            Animation slideInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            convertView.startAnimation(slideInAnimation);

            return convertView;
        }

        public void updateList(List<Item> newList) {
            items.clear();
            items.addAll(newList);
            notifyDataSetChanged();
        }

        private static class ViewHolder {
            ImageView itemImage;
            TextView itemName;
            TextView itemInfo;
        }
    }
}