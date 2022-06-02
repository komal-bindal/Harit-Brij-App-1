package com.haritbrij.haritBrij;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        bottomNavigationView = findViewById(R.id.main_user_bottom_navigation_view);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final TreeRegisterFragment treeRegisterFragment = new TreeRegisterFragment();
        final SearchTreeFragment searchTreeFragment = new SearchTreeFragment();
        final TreeMapFragment treeMapFragment = new TreeMapFragment();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment;
                switch (item.getItemId()) {
                    case R.id.action_tree_data:
                        selectedFragment = searchTreeFragment;
                        break;
                    case R.id.action_tree_map:
                        selectedFragment = treeMapFragment;
                        break;
                    default:
                        selectedFragment = treeRegisterFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.main_user_fragment_container_view, selectedFragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_tree_register);
    }

    public void getFirst() {

    }
}
