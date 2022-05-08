package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.haritbrij.haritBrij.R;

public class UserRegistrationDetailsFragment extends Fragment {
    public UserRegistrationDetailsFragment() {
        super(R.layout.fragment_user_registration_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button submitRegistrationDetailsButton = view.findViewById(R.id.submit_registration_details_button);

        submitRegistrationDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Registration Completed Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
