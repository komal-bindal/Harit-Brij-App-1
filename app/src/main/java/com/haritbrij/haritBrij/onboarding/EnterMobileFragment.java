package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.utils.VolleySingleton;

public class EnterMobileFragment extends Fragment {
    OnboardingViewModel viewModel;

    public EnterMobileFragment() {
        super(R.layout.fragment_enter_mobile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);
        viewModel.setLogin(true);
        EditText mobileNumberEditText = view.findViewById(R.id.mobile_num_edit_text);
        Button sendOtpButton = view.findViewById(R.id.send_otp_button);
        Button registerButton = view.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, new UserRegistrationDetailsFragment()).addToBackStack(null).commit();

            }
        });

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredMobileNumber = mobileNumberEditText.getText().toString();
                String baseUrl = VolleySingleton.getBaseUrl();
                String myUrl = baseUrl + "login.php/" + "?mobile=" + enteredMobileNumber;
                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                            Toast.makeText(getContext(), "OTP Sent", Toast.LENGTH_LONG).show();
                            viewModel.addMobileNumber(Long.parseLong(enteredMobileNumber));
                            viewModel.sendOtp();

                            //navigate to enterotpfragment
                            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container_view, new EnterOtpFragment()).addToBackStack(null).commit();

                        },
                        volleyError -> {
                            String message = null;
                            if (volleyError instanceof NetworkError) {
                                message = "Network Error!! Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ServerError) {
                                message = "User not found. Please register!";
                            } else if (volleyError instanceof AuthFailureError) {
                                message = "AuthFailure Error!! Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (volleyError instanceof NoConnectionError) {
                                message = "No Connection Error!! Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }
                            //The api return 404 error. This means the user does not exist.
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction.replace(R.id.fragment_container_view, new UserRegistrationDetailsFragment()).addToBackStack(null).commit();
                        }
                );

                VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);


            }
        });
    }
}
