package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import com.haritbrij.haritBrij.R;

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
                int submittedOtp = Integer.parseInt(otpTextView.getOTP());
                if(submittedOtp == viewModel.getOtp()) {
                    Toast.makeText(getActivity(), "Otp Matched", Toast.LENGTH_LONG).show();

                    //navigate to userRegistrationDetailsFragment
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, new UserRegistrationDetailsFragment()).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), "Not Matched. Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
