package com.haritbrij.haritBrij.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.TreeListAdapter;
import com.haritbrij.haritBrij.UserMainActivity;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.aabhasjindal.otptextview.OtpTextView;

public class EnterOtpFragment extends Fragment {
    OnboardingViewModel viewModel;
    public EnterOtpFragment() {
        super(R.layout.fragment_enter_otp);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        Button submitOtpButton = view.findViewById(R.id.submit_otp_button);
        OtpTextView otpTextView = view.findViewById(R.id.otp_view);

        submitOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String submittedOtp = otpTextView.getOTP();
                if(submittedOtp.equals(viewModel.getOtp().trim())) {
                    Toast.makeText(getActivity(), "Otp Matched", Toast.LENGTH_LONG).show();

                    viewModel.getSharedPreferenceEditor().putLong("mobileNumber", viewModel.getMobileNumber()).commit();

                    //check if the user already exists
                    String baseUrl = VolleySingleton.getBaseUrl();
                    String myUrl = baseUrl + "login.php/" + "?mobile=" + viewModel.getMobileNumber();
                    StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                            response -> {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String uid = jsonObject.getString("uid");
                                    viewModel.getSharedPreferenceEditor().putString(SharedPrefConstants.uid, uid).apply();
                                    String name = jsonObject.getString("name");
                                    viewModel.getSharedPreferenceEditor().putString(SharedPrefConstants.name, name).apply();
                                    String target = jsonObject.getString("target");
                                    viewModel.getSharedPreferenceEditor().putString(SharedPrefConstants.target, target).apply();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(getActivity(), UserMainActivity.class);
                                startActivity(intent);
                            },
                            volleyError -> {
                        //The api return 404 error. This means the user does not exist.
//                                Toast.makeText(getActivity(), "Sign in failed", Toast.LENGTH_SHORT).show();
//                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction.replace(R.id.fragment_container_view, new UserRegistrationDetailsFragment()).addToBackStack(null).commit();
                       }
                    );

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);

                } else {
                    Toast.makeText(getActivity(), "Not Matched. Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
