package com.example.internalexternalstoragenew;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.writeIS).setOnClickListener(this);
        findViewById(R.id.readIS).setOnClickListener(this);
        findViewById(R.id.writeES).setOnClickListener(this);
        findViewById(R.id.readES).setOnClickListener(this);
        findViewById(R.id.writeCF).setOnClickListener(this);
        findViewById(R.id.readCF).setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.writeIS) {
            writeIS("myfile.txt", "Xin chào các bạn!");
        } else if (id == R.id.readIS) {
           String readDataIS = readIS("myfile.txt");
            Toast.makeText(this,"Nội dung file:" + readDataIS, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.writeES) {
            writeES("external_file.txt", "Dữ liệu từ bộ nhớ ngoài!");
        } else if (id == R.id.readES) {
            String data = readES("external_file.txt");
            Toast.makeText(this, "External: " + data, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.writeCF) {
            writeCF("cache_file.txt", "Dữ liệu từ cache!");
        } else if (id == R.id.readCF){
            String data = readCF("cache_file.txt");
            Toast.makeText(this, "Cache: " + data, Toast.LENGTH_SHORT).show();
        }
    }

    // Internal Storage
    private void writeIS(String fileName, String content) {
        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Đã ghi file nội bộ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
            Toast.makeText(this,"Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return sb.toString();
    }

    // External Storage (Scoped Storage / App-specific directory)
    private void writeES(String fileName, String content) {
        File file = new File(getExternalFilesDir(null), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Đã ghi file bộ nhớ ngoài", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
            Toast.makeText(this,"Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return sb.toString();
    }

    // Cache Files
    private void writeCF(String fileName, String content) {
        File file = new File(getCacheDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            Toast.makeText(this, "Đã ghi file cache", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
            Toast.makeText(this,"Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return sb.toString();
    }
}