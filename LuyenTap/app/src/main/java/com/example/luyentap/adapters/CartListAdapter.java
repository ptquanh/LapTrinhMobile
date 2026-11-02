package com.example.luyentap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luyentap.R;
import com.example.luyentap.models.Book;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends BaseAdapter {

    public interface OnCartItemChangeListener {
        void onCheckChanged(Book book, boolean isChecked, int position);
    }

    private final Context context;
    private List<Book> cartList;
    private final LayoutInflater inflater;
    private OnCartItemChangeListener onCartItemChangeListener;

    public CartListAdapter(Context context, List<Book> cartList) {
        this.context = context;
        this.cartList = cartList != null ? cartList : new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnCartItemChangeListener(OnCartItemChangeListener listener) {
        this.onCartItemChangeListener = listener;
    }

    public void updateData(List<Book> newCartList) {
        this.cartList = newCartList != null ? newCartList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Book getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cartList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cart_list, parent, false);
            holder = new ViewHolder();
            holder.imgThumb = convertView.findViewById(R.id.imgCartThumb);
            holder.tvTitle = convertView.findViewById(R.id.tvCartTitle);
            holder.tvGenre = convertView.findViewById(R.id.tvCartGenre);
            holder.tvPrice = convertView.findViewById(R.id.tvCartPrice);
            holder.cbSelect = convertView.findViewById(R.id.cbCartSelect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);
        holder.imgThumb.setImageResource(book.getImageResId());
        holder.tvTitle.setText(book.getTitle());
        holder.tvGenre.setText(book.getGenre());
        holder.tvPrice.setText(book.getFormattedPrice());

        // Temporarily clear listener to prevent recycling bug
        holder.cbSelect.setOnCheckedChangeListener(null);
        holder.cbSelect.setChecked(book.isSelected());

        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            book.setSelected(isChecked);
            if (onCartItemChangeListener != null) {
                onCartItemChangeListener.onCheckChanged(book, isChecked, position);
            }
        });

        convertView.setOnClickListener(v -> {
            boolean newState = !holder.cbSelect.isChecked();
            holder.cbSelect.setChecked(newState);
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgThumb;
        TextView tvTitle;
        TextView tvGenre;
        TextView tvPrice;
        CheckBox cbSelect;
    }
}
