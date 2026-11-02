package com.example.luyentap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.luyentap.adapters.ViewPagerAdapter;
import com.example.luyentap.fragments.BookGridFragment;
import com.example.luyentap.fragments.CartFragment;
import com.example.luyentap.fragments.ProfileFragment;
import com.example.luyentap.models.Book;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;

    private BookGridFragment bookGridFragment;
    private CartFragment cartFragment;
    private ProfileFragment profileFragment;

    private final List<Book> bookList = new ArrayList<>();
    private boolean isSyncing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSampleData();
        initViews();
        setupViewPager();
        setupNavigationSync();
    }

    private void initSampleData() {
        bookList.clear();
        bookList.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 12.99, R.drawable.book_gatsby, true, false));
        bookList.add(new Book(2, "To Kill a Mockingbird", "Harper Lee", "Fiction", 14.50, R.drawable.book_mockingbird, false, false));
        bookList.add(new Book(3, "1984", "George Orwell", "Dystopian", 11.99, R.drawable.book_1984, true, true));
        bookList.add(new Book(4, "Pride and Prejudice", "Jane Austen", "Romance", 9.99, R.drawable.book_pride, false, false));
        bookList.add(new Book(5, "The Hobbit", "J.R.R. Tolkien", "Fantasy", 15.99, R.drawable.book_hobbit, true, true));
        bookList.add(new Book(6, "Harry Potter", "J.K. Rowling", "Fantasy", 18.50, R.drawable.book_harry_potter, true, false));
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        bookGridFragment = BookGridFragment.newInstance();
        cartFragment = CartFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();

        viewPagerAdapter.addFragment(bookGridFragment, getString(R.string.tab_books));
        viewPagerAdapter.addFragment(cartFragment, getString(R.string.tab_cart));
        viewPagerAdapter.addFragment(profileFragment, getString(R.string.tab_profile));

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private void setupNavigationSync() {
        // BottomNavigationView -> ViewPager
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (isSyncing) return true;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_books) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_cart) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(2, true);
                return true;
            }
            return false;
        });

        // ViewPager -> BottomNavigationView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                isSyncing = true;
                if (position == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_books);
                    if (bookGridFragment != null) {
                        bookGridFragment.refreshData();
                    }
                } else if (position == 1) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_cart);
                    if (cartFragment != null) {
                        cartFragment.refreshData();
                    }
                } else if (position == 2) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                }
                isSyncing = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void navigateToTab(int position) {
        if (viewPager != null && position >= 0 && position < 3) {
            viewPager.setCurrentItem(position, true);
        }
    }

    public List<Book> getAllBooks() {
        return bookList;
    }

    public List<Book> getCartBooks() {
        List<Book> cart = new ArrayList<>();
        for (Book b : bookList) {
            if (b.isInCart()) {
                cart.add(b);
            }
        }
        return cart;
    }

    public void toggleBookInCart(Book book) {
        book.setInCart(!book.isInCart());
        if (cartFragment != null) {
            cartFragment.refreshData();
        }
    }

    public void onCartItemSelectionChanged(Book book, boolean isChecked) {
        book.setSelected(isChecked);
    }
}