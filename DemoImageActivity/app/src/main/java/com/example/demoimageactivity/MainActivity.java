package com.example.demoimageactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    ImageView imageView;
    private int[] listButtonID = {R.id.buttonCenter, R.id.buttonCenterCrop, R.id.buttonCenterInside, R.id.buttonFitCenter,R.id.buttonFitStart, R.id.buttonFitEnd, R.id.buttonFitXY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCenter:
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                textView.setText("Center Style");
                break;

            case R.id.buttonCenterCrop:

        }
    }

    public void init(){
        textView = findViewById(R.id.textCenterCropStyle);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.thoi_than_1);
        for(int id:listButtonID){
            Button btnTemp = (Button) findViewById(id);
            btnTemp.setOnClickListener(this);
        }
    }
}