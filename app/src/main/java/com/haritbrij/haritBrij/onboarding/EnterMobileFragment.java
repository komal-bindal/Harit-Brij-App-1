package com.haritbrij.haritBrij.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.haritbrij.haritBrij.R;

public class EnterMobileFragment extends Fragment {
    OnboardingViewModel viewModel;
    public EnterMobileFragment() {
        super(R.layout.fragment_enter_mobile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        EditText mobileNumberEditText = view.findViewById(R.id.mobile_num_edit_text);
        Button sendOtpButton = view.findViewById(R.id.send_otp_button);
        TextView goToUserRegistrationTextView = view.findViewById(R.id.go_to_user_registration_text_view);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredMobileNumber = mobileNumberEditText.getText().toString();
                Toast.makeText(getContext(), "OTP Sent", Toast.LENGTH_LONG);

                viewModel.addPhoneNumber(Long.valueOf(enteredMobileNumber));
                viewModel.sendOtp();

                //navigate to enterotpfragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, new EnterOtpFragment()).addToBackStack(null).commit();
            }
        });

        goToUserRegistrationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, new UserRegistrationDetailsFragment()).addToBackStack(null).commit();
            }
        });
    }
}
