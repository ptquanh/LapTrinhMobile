package com.example.demoactivity;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText edUserName, edPassword;
    Button btLogin;
    String userName="admin";
    String password = "123";
    String name = "Phan Tuan Quoc Anh";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edUserName = (EditText) findViewById(R.id.edUserName);
        edPassword = (EditText) findViewById(R.id.edPassword);

    }

    public void changeActivity(View view){
        if(userName.equals(edUserName.getText().toString()) && password.equals(edPassword.getText().toString())) {
            Intent i = new Intent(this,SecondActivity.class);
            i.putExtra("name",name);
            startActivity(i);
        }
        else{
            Toast.makeText(this,"Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }
}