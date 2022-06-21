package com.haritbrij.haritBrij;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.onboarding.UserRegistrationDetailsFragment;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    UserMainViewModel viewModel;
    TextView treeTargetTextView;
    TextView userNameTextView;
    TextView treesPlantedTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        treeTargetTextView = findViewById(R.id.treeTargetTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        treesPlantedTextView = findViewById(R.id.registeredTreesTextView);

        viewModel = new ViewModelProvider(this).get(UserMainViewModel.class);

        treeTargetTextView.setText(viewModel.sharedPreferences.getString(SharedPrefConstants.target, ""));
        userNameTextView.setText(viewModel.sharedPreferences.getString(SharedPrefConstants.name, ""));
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

        //set the number of registered trees.
        String url = baseUrl + "getalltree.php";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try{
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        JSONArray jsonArray = myJsonObject.getJSONArray("body");

                        treesPlantedTextView.setText(String.valueOf(jsonArray.length()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
        );
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
