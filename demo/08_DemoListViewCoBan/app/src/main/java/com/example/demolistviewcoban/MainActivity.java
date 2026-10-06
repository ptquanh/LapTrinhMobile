package com.example.demolistviewcoban;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> arrayMonHoc;
    ArrayAdapter<String> adapter;
    ListView lsView;
    EditText editText;
    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lsView = findViewById(R.id.lsView);
        editText = findViewById(R.id.edText);

        arrayMonHoc = new ArrayList<>();
        arrayMonHoc.add("Android");
        arrayMonHoc.add("Java");
        arrayMonHoc.add("OOP");
        arrayMonHoc.add("Web");

        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayMonHoc);
        lsView.setAdapter(adapter);

        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                editText.setText(arrayMonHoc.get(position));
                pos = position;
            }
        });

        lsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Đã xóa: " + arrayMonHoc.get(position), Toast.LENGTH_SHORT).show();
                arrayMonHoc.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void onAdd(View view) {
        String text = editText.getText().toString().trim();
        if (!text.isEmpty()) {
            arrayMonHoc.add(text);
            adapter.notifyDataSetChanged();
            editText.setText("");
        }
    }

    public void onUpdate(View view) {
        String text = editText.getText().toString().trim();
        if (pos != -1 && !text.isEmpty()) {
            arrayMonHoc.set(pos, text);
            adapter.notifyDataSetChanged();
            pos = -1;
            editText.setText("");
        }
    }
}