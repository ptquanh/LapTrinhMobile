package com.example.luyentap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.luyentap.MainActivity;
import com.example.luyentap.R;
import com.example.luyentap.adapters.CartListAdapter;
import com.example.luyentap.models.Book;

import java.util.List;

public class CartFragment extends Fragment {

    private ListView listViewCart;
    private CartListAdapter adapter;

    public CartFragment() {
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        listViewCart = view.findViewById(R.id.listViewCart);

        setupListView();

        return view;
    }

    private void setupListView() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            List<Book> cartBooks = activity.getCartBooks();
            adapter = new CartListAdapter(requireContext(), cartBooks);
            adapter.setOnCartItemChangeListener((book, isChecked, position) -> {
                activity.onCartItemSelectionChanged(book, isChecked);
            });
            listViewCart.setAdapter(adapter);
        }
    }

    public void refreshData() {
        if (adapter != null && getActivity() instanceof MainActivity) {
            adapter.updateData(((MainActivity) getActivity()).getCartBooks());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
}
