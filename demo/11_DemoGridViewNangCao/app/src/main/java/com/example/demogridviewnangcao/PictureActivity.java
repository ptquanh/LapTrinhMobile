package com.example.demogridviewnangcao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PictureActivity extends AppCompatActivity {
    ImageView imgChiTiet;
    TextView txtChiTiet;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imgChiTiet = findViewById(R.id.imgChiTiet);
        txtChiTiet = findViewById(R.id.txtChiTiet);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        int hinh = intent.getIntExtra("hinhAnh", 0);
        String ten = intent.getStringExtra("tenHinh");

        if (hinh != 0) {
            imgChiTiet.setImageResource(hinh);
        }
        if (ten != null) {
            txtChiTiet.setText(ten);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}