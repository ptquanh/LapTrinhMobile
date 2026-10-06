package com.example.demotoast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnMethod2, btnMethod3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMethod2 = findViewById(R.id.btnMethod2);
        btnMethod3 = findViewById(R.id.btnMethod3);

        // Method 2: implements View.OnClickListener
        btnMethod2.setOnClickListener(this);

        // Method 3: Anonymous Listener
        btnMethod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Toast: Method 3 (Anonymous Listener)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method 1: from file .xml
    public void showToastXml(View view) {
        Toast.makeText(this, "Toast: Method 1 (XML onClick)", Toast.LENGTH_SHORT).show();
    }

    // Method 2 callback
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnMethod2) {
            Toast.makeText(this, "Toast: Method 2 (implements OnClickListener)", Toast.LENGTH_SHORT).show();
        }
    }
}