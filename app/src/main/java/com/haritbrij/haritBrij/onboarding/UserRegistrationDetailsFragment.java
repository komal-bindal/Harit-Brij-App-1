package com.haritbrij.haritBrij.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.UserMainActivity;

public class UserRegistrationDetailsFragment extends Fragment {
    OnboardingViewModel viewModel;
    EditText userNameEditText;
    EditText userMobileNumberEditText;
    EditText userTreeTargetEditText;

    public UserRegistrationDetailsFragment() {
        super(R.layout.fragment_user_registration_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        Button submitRegistrationDetailsButton = view.findViewById(R.id.submit_registration_details_button);
        userNameEditText = view.findViewById(R.id.user_name_edit_text);
        userMobileNumberEditText = view.findViewById(R.id.user_mobile_edit_text);
        userTreeTargetEditText = view.findViewById(R.id.user_tree_target_edit_text);

        submitRegistrationDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Registration Completed Successfully", Toast.LENGTH_SHORT).show();

                //get all the data from the edit texts
//                String userName = userNameEditText.getText().toString();
//                long userMobileNumber = Long.parseLong(userMobileNumberEditText.getText().toString());
//                int userTreeTarget = Integer.parseInt(userTreeTargetEditText.getText().toString());
//
//                //save the phone number
//                viewModel.addPhoneNumber(userMobileNumber);
//
//                //send the user details to api
//                viewModel.sendUserDetails(userName, userMobileNumber, userTreeTarget);

                Intent intent = new Intent(getActivity(), UserMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
