package com.example.luyentap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.luyentap.MainActivity;
import com.example.luyentap.R;
import com.example.luyentap.adapters.BookGridAdapter;
import com.example.luyentap.models.Book;

import java.util.List;

public class BookGridFragment extends Fragment {

    private GridView gridViewBooks;
    private BookGridAdapter adapter;

    public BookGridFragment() {
    }

    public static BookGridFragment newInstance() {
        return new BookGridFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_grid, container, false);
        gridViewBooks = view.findViewById(R.id.gridViewBooks);

        setupGridView();

        return view;
    }

    private void setupGridView() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            List<Book> books = activity.getAllBooks();
            adapter = new BookGridAdapter(requireContext(), books);
            adapter.setOnBookClickListener((book, position) -> {
                activity.toggleBookInCart(book);
                String msg = book.isInCart()
                        ? "Đã thêm \"" + book.getTitle() + "\" vào giỏ hàng"
                        : "Đã xóa \"" + book.getTitle() + "\" khỏi giỏ hàng";
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            });
            gridViewBooks.setAdapter(adapter);
        }
    }

    public void refreshData() {
        if (adapter != null && getActivity() instanceof MainActivity) {
            adapter.updateData(((MainActivity) getActivity()).getAllBooks());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
}
