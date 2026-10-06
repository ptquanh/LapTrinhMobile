package com.example.gridview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gridview.model.HinhAnh;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<HinhAnh> arrayAnh;
    private HinhAnhAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gvTen);

        // Tạo 9 đối tượng HinhAnh với hình trong drawable và tên từ "Biển 1" -> "Biển 9"
        arrayAnh = new ArrayList<>();
        arrayAnh.add(new HinhAnh(R.drawable.hinh1, "Biển 1"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh2, "Biển 2"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh3, "Biển 3"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh4, "Biển 4"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh5, "Biển 5"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh6, "Biển 6"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh7, "Biển 7"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh8, "Biển 8"));
        arrayAnh.add(new HinhAnh(R.drawable.hinh9, "Biển 9"));

        // Khởi tạo Adapter và gán vào GridView
        adapter = new HinhAnhAdapter(this, R.layout.hinh_anh, arrayAnh);
        gridView.setAdapter(adapter);

        // Bắt sự kiện khi nhấn vào 1 item trên GridView
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            HinhAnh hinhAnh = arrayAnh.get(position);
            Intent intent = new Intent(MainActivity.this, PictureActivity.class);
            intent.putExtra("hinhAnh", hinhAnh.getHinh());
            intent.putExtra("tenHinh", hinhAnh.getTen());
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}