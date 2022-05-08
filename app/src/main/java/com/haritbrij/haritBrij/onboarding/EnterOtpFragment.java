package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.haritbrij.haritBrij.R;

public class EnterOtpFragment extends Fragment {
    public EnterOtpFragment() {
        super(R.layout.fragment_enter_otp);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button submitOtpButton = view.findViewById(R.id.submit_otp_button);

        submitOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Otp Submitted", Toast.LENGTH_LONG).show();

                //navigate to userRegistrationDetailsFragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, new UserRegistrationDetailsFragment()).addToBackStack(null).commit();
            }
        });
    }
}
