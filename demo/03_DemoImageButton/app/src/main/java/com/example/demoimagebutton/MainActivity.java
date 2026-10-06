package com.example.demoimagebutton;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }

    public void showFacebook(View view) {
        imageView.setImageResource(R.drawable.facebook);
    }

    public void showTwitter(View view) {
        imageView.setImageResource(R.drawable.twitter);
    }
}