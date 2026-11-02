package com.example.luyentap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luyentap.R;
import com.example.luyentap.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookGridAdapter extends BaseAdapter {

    public interface OnBookClickListener {
        void onBookClick(Book book, int position);
    }

    private final Context context;
    private List<Book> bookList;
    private final LayoutInflater inflater;
    private OnBookClickListener onBookClickListener;

    public BookGridAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList != null ? bookList : new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.onBookClickListener = listener;
    }

    public void updateData(List<Book> newBooks) {
        this.bookList = newBooks != null ? newBooks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Book getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_book_grid, parent, false);
            holder = new ViewHolder();
            holder.imgCover = convertView.findViewById(R.id.imgBookCover);
            holder.tvTitle = convertView.findViewById(R.id.tvBookTitle);
            holder.tvAuthor = convertView.findViewById(R.id.tvBookAuthor);
            holder.tvPrice = convertView.findViewById(R.id.tvBookPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);
        holder.imgCover.setImageResource(book.getImageResId());
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvPrice.setText(book.getFormattedPrice());

        convertView.setOnClickListener(v -> {
            if (onBookClickListener != null) {
                onBookClickListener.onBookClick(book, position);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgCover;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvPrice;
    }
}
