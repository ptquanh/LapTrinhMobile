package com.example.demostoragensharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edPrefKey, edPrefValue;
    private TextView tvPrefResult;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.writeIS).setOnClickListener(this);
        findViewById(R.id.readIS).setOnClickListener(this);
        findViewById(R.id.writeES).setOnClickListener(this);
        findViewById(R.id.readES).setOnClickListener(this);
        findViewById(R.id.writeCF).setOnClickListener(this);
        findViewById(R.id.readCF).setOnClickListener(this);

        edPrefKey = findViewById(R.id.edPrefKey);
        edPrefValue = findViewById(R.id.edPrefValue);
        tvPrefResult = findViewById(R.id.tvPrefResult);

        findViewById(R.id.btnSavePref).setOnClickListener(this);
        findViewById(R.id.btnReadPref).setOnClickListener(this);
        findViewById(R.id.btnClearPref).setOnClickListener(this);

        sharedPreferences = getSharedPreferences("MyStoragePrefs", MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.writeIS) {
            writeIS("myfile.txt", "Xin chào từ Internal Storage!");
        } else if (id == R.id.readIS) {
            String data = readIS("myfile.txt");
            Toast.makeText(this, "Nội dung Internal: " + data, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.writeES) {
            writeES("external_file.txt", "Dữ liệu từ External Storage!");
        } else if (id == R.id.readES) {
            String data = readES("external_file.txt");
            Toast.makeText(this, "Nội dung External: " + data, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.writeCF) {
            writeCF("cache_file.txt", "Dữ liệu từ Cache Files!");
        } else if (id == R.id.readCF) {
            String data = readCF("cache_file.txt");
            Toast.makeText(this, "Nội dung Cache: " + data, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnSavePref) {
            String key = edPrefKey.getText().toString().trim();
            String val = edPrefValue.getText().toString().trim();
            if (!key.isEmpty()) {
                sharedPreferences.edit().putString(key, val).apply();
                Toast.makeText(this, "Đã lưu vào SharedPreferences!", Toast.LENGTH_SHORT).show();
                tvPrefResult.setText("Đã lưu: [" + key + " = " + val + "]");
            }
        } else if (id == R.id.btnReadPref) {
            String key = edPrefKey.getText().toString().trim();
            if (!key.isEmpty()) {
                String val = sharedPreferences.getString(key, "Không tìm thấy");
                tvPrefResult.setText("Đọc [" + key + "]: " + val);
                Toast.makeText(this, "Giá trị: " + val, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btnClearPref) {
            sharedPreferences.edit().clear().apply();
            tvPrefResult.setText("Đã xóa toàn bộ SharedPreferences!");
            Toast.makeText(this, "Đã xóa toàn bộ SharedPreferences!", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeIS(String fileName, String content) {
        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Đã ghi file nội bộ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String readIS(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = openFileInput(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return sb.toString();
    }

    private void writeES(String fileName, String content) {
        File file = new File(getExternalFilesDir(null), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Đã ghi file bộ nhớ ngoài", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String readES(String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return sb.toString();
    }

    private void writeCF(String fileName, String content) {
        File file = new File(getCacheDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Đã ghi file cache", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String readCF(String fileName) {
        File file = new File(getCacheDir(), fileName);
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return sb.toString();
    }
}