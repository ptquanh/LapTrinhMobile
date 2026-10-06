package com.example.demogridviewnangcao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demogridviewnangcao.model.HinhAnh;

import java.util.List;

public class HinhAnhAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<HinhAnh> list;

    public HinhAnhAdapter(Context context, int layout, List<HinhAnh> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }

        ImageView imgHinh = convertView.findViewById(R.id.imgHinh);
        TextView txtTen = convertView.findViewById(R.id.txtTen);

        HinhAnh hinhAnh = list.get(position);
        imgHinh.setImageResource(hinhAnh.getHinh());
        txtTen.setText(hinhAnh.getTen());

        return convertView;
    }
}