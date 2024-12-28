package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import android.animation.Animator;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initialize Lottie Animation for intro screen
        LottieAnimationView lottieIntroAnimation = findViewById(R.id.lottie_intro);

        // Set the speed of the animation (default speed is 1.0)
        lottieIntroAnimation.setSpeed(3.0f);  // Adjust to speed up the animation

        // Add an animation listener to detect when the animation ends
        lottieIntroAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                // Optional: Handle actions when animation starts
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                // When animation ends, navigate to LoginActivity
                Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {
                // Handle actions when animation is canceled
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {
                // Handle actions when animation repeats
            }
        });
    }
}