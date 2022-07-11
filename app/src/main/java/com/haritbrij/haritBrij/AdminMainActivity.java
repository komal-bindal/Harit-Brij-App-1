package com.haritbrij.haritBrij;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    BottomNavigationView bottomNavigationView;
    AdminViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final OrganisationListFragment organisationListFragment = new OrganisationListFragment();
        final AdminSearchTreeFragment adminSearchTreeFragment = new AdminSearchTreeFragment();
        final AdminFilterTreesFragment adminFilterTreesFragment = new AdminFilterTreesFragment();
        bottomNavigationView = findViewById(R.id.main_admin_bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            switch (item.getItemId()) {
                case R.id.admin_search_utid:
                    selectedFragment = adminSearchTreeFragment;
                    Log.d(TAG, "adminTreeData");
                    break;
                case R.id.admin_search:
                    selectedFragment = adminFilterTreesFragment;
                    Log.d(TAG, "admintreemap");
                    break;
                default:
                    selectedFragment = organisationListFragment;
                    Log.d(TAG, "orgList");
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.main_admin_fragment_container_view, selectedFragment).commit();
            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.admin_org_list);
    }
}
