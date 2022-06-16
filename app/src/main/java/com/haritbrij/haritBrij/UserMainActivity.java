package com.haritbrij.haritBrij;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.haritbrij.haritBrij.onboarding.UserRegistrationDetailsFragment;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

public class UserMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    UserMainViewModel viewModel;
    TextView treeTargetTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        treeTargetTextView = findViewById(R.id.treeTargetTextView);

        viewModel = new ViewModelProvider(this).get(UserMainViewModel.class);

        treeTargetTextView.setText(viewModel.sharedPreferences.getString(SharedPrefConstants.target, ""));
        bottomNavigationView = findViewById(R.id.main_user_bottom_navigation_view);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final TreeRegisterFragment treeRegisterFragment = new TreeRegisterFragment();
        final SearchTreeFragment searchTreeFragment = new SearchTreeFragment();
        final TreeMapFragment treeMapFragment = new TreeMapFragment();

        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        });

        bottomNavigationView.setSelectedItemId(R.id.action_tree_register);

        String baseUrl = VolleySingleton.getBaseUrl();
        long mobileNumber = viewModel.sharedPreferences.getLong("mobileNumber", 0);
        String myUrl = baseUrl + "login.php/" + "?mobile=" + mobileNumber;
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                },
                volleyError -> {
                    Toast.makeText(this, "Please register again " + mobileNumber, Toast.LENGTH_LONG).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(myRequest);

    }
}
