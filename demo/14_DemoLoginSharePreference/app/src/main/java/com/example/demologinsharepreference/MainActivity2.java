package com.example.demologinsharepreference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    Button logout, btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        logout = findViewById(R.id.logout_btn);
        btnThoat = findViewById(R.id.logout_exit);

        logout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("login_check", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnThoat.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}