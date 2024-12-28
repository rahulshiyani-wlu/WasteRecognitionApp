package com.example.project.waste_recognition_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import android.animation.Animator;

public class UnlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        // Initialize Lottie animation view for unlocking feature
        LottieAnimationView lottieUnlock = findViewById(R.id.lottie_unlock);

        // Set the speed of the unlock animation
        lottieUnlock.setSpeed(1.5f);  // Optional: Adjust the speed to fit desired experience

        // Add listener to handle actions when the animation starts, ends, cancels, or repeats
        lottieUnlock.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // Optional: Action to perform when the animation starts
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // When the animation ends, navigate to MainActivity
                Intent intent = new Intent(UnlockActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Close UnlockActivity so users can't navigate back to it
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Handle animation cancellation, if needed
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Handle animation repeat, if needed
            }
        });
    }
}