package com.example.demoviewpagernnavigation;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(0);

        getSupportActionBar().setTitle("Person");

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.person).setChecked(true);
                        getSupportActionBar().setTitle("Person");
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        getSupportActionBar().setTitle("Home");
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.settings).setChecked(true);
                        getSupportActionBar().setTitle("Setting");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.person) {
                    mViewPager.setCurrentItem(0);
                    getSupportActionBar().setTitle("Person");
                } else if (itemId == R.id.home) {
                    mViewPager.setCurrentItem(1);
                    getSupportActionBar().setTitle("Home");
                } else if (itemId == R.id.settings) {
                    mViewPager.setCurrentItem(2);
                    getSupportActionBar().setTitle("Setting");
                }
                return true;
            }
        });
    }
}
