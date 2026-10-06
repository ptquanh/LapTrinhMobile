package com.example.gridview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PictureActivity extends AppCompatActivity {

    private ImageView imgChiTiet;
    private TextView txtTenChiTiet;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_picture);

        imgChiTiet = findViewById(R.id.imgChiTiet);
        txtTenChiTiet = findViewById(R.id.txtTenChiTiet);
        btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        int hinh = intent.getIntExtra("hinhAnh", 0);
        String ten = intent.getStringExtra("tenHinh");

        if (hinh != 0) {
            imgChiTiet.setImageResource(hinh);
        }
        if (ten != null) {
            txtTenChiTiet.setText(ten);
        }

        btnBack.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}