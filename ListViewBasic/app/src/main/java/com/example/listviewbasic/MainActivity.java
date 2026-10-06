package com.example.listviewbasic;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> arrayMonHoc;
    ArrayAdapter adapter;
    ListView lsView;
    EditText editText;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        lsView = findViewById(R.id.lsView);
        editText = findViewById(R.id.edText);

        arrayMonHoc = new ArrayList<String>();
        arrayMonHoc.add("Android");
        arrayMonHoc.add("Java");
        arrayMonHoc.add("OOP");
        arrayMonHoc.add("Web");

        adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,arrayMonHoc);
        lsView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                editText.setText(arrayMonHoc.get(position));
                pos = position;
            }
        });

        lsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,arrayMonHoc.get(position),Toast.LENGTH_LONG).show();
                arrayMonHoc.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    public void onAdd(View view) {
        arrayMonHoc.add(editText.getText().toString());
        adapter.notifyDataSetChanged();
        editText.setText("");
    }

    public void onUpdate(View view) {

        if(pos != -1){
            arrayMonHoc.set(pos, editText.getText().toString());
            adapter.notifyDataSetChanged();
        }
        pos = -1;
        editText.setText("");

    }
}