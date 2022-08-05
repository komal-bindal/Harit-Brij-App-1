package com.haritbrij.haritBrij;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.haritbrij.haritBrij.onboarding.OnboardingActivity;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;

public class AdminMainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    BottomNavigationView bottomNavigationView;
    AdminViewModel viewModel;
    ImageView logoutButtonView;
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
        logoutButtonView = findViewById(R.id.logoutButtonAdmin);

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


        logoutButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getEditor().putBoolean(SharedPrefConstants.isAdminSignedIn, false).apply();
                Intent intent = new Intent(AdminMainActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.admin_org_list);
    }
}
