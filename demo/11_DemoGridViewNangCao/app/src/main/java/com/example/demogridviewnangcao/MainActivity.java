package com.example.demogridviewnangcao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demogridviewnangcao.model.HinhAnh;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<HinhAnh> arrayAnh;
    private HinhAnhAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gvTen);

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

        adapter = new HinhAnhAdapter(this, R.layout.hinh_anh, arrayAnh);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            HinhAnh hinhAnh = arrayAnh.get(position);
            Intent intent = new Intent(MainActivity.this, PictureActivity.class);
            intent.putExtra("hinhAnh", hinhAnh.getHinh());
            intent.putExtra("tenHinh", hinhAnh.getTen());
            startActivity(intent);
        });
    }
}