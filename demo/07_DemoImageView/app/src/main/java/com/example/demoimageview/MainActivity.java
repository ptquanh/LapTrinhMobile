package com.example.demoimageview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    ImageView imageView;
    private int[] listButtonID = {
        R.id.buttonCenter, R.id.buttonCenterCrop, R.id.buttonCenterInside,
        R.id.buttonFitCenter, R.id.buttonFitStart, R.id.buttonFitEnd, R.id.buttonFitXY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textScaleType);
        imageView = findViewById(R.id.imageView);

        for (int id : listButtonID) {
            Button btn = findViewById(id);
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonCenter) {
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            textView.setText("ScaleType: CENTER");
        } else if (id == R.id.buttonCenterCrop) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textView.setText("ScaleType: CENTER CROP");
        } else if (id == R.id.buttonCenterInside) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            textView.setText("ScaleType: CENTER INSIDE");
        } else if (id == R.id.buttonFitCenter) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            textView.setText("ScaleType: FIT CENTER");
        } else if (id == R.id.buttonFitStart) {
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
            textView.setText("ScaleType: FIT START");
        } else if (id == R.id.buttonFitEnd) {
            imageView.setScaleType(ImageView.ScaleType.FIT_END);
            textView.setText("ScaleType: FIT END");
        } else if (id == R.id.buttonFitXY) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            textView.setText("ScaleType: FIT XY");
        }
    }
}