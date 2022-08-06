package com.haritbrij.haritBrij.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haritbrij.haritBrij.AdminMainActivity;
import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.Arrays;

public class AdminLoginFragment extends Fragment {
    public AdminLoginFragment() {
        super(R.layout.fragment_admin_login);
    }
OnboardingViewModel viewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        EditText adminUsernameEditText = view.findViewById(R.id.admin_username_edit_text);
        EditText adminPasswordEditText = view.findViewById(R.id.admin_password_edit_text);
        Button adminSubmitButton = view.findViewById(R.id.admin_login_button);

        adminSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = adminUsernameEditText.getText().toString();
                String password = adminPasswordEditText.getText().toString();

                //Construct the Json object
                JSONObject object = new JSONObject();
                try {
                    object.put("user", userName);
                    object.put("pass", password);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
                System.out.println(object.getClass());

                String baseUrl = VolleySingleton.getBaseUrl();
                String myUrl = baseUrl + "adminlogin.php/";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, myUrl, object,
                        response -> {
                            Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                            viewModel.getSharedPreferenceEditor().putBoolean(SharedPrefConstants.isAdminSignedIn, true).apply();
                            Intent intent = new Intent(getActivity(), AdminMainActivity.class);
                            startActivity(intent);
//                            getActivity().finish();
                        },
                        error -> {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                );

                VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}
