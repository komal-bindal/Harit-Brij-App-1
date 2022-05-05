package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.haritbrij.haritBrij.R;

public class EnterMobileFragment extends Fragment {
    EnterMobileFragment() {
        super(R.layout.fragment_enter_mobile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText mobileNumberEditText = view.findViewById(R.id.mobile_num_edit_text);
        Button sendOtpButton = view.findViewById(R.id.send_otp_button);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredMobileNumber = mobileNumberEditText.getText().toString();

                //call the api passing the enteredMobileNumber.
            }
        });
    }
}
