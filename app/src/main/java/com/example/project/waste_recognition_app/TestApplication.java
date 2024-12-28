package com.example.project.waste_recognition_app;
import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Glide.init(this, new GlideBuilder());
    }
}