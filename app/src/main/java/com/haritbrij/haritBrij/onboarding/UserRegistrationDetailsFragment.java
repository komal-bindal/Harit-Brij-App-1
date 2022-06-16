package com.haritbrij.haritBrij.onboarding;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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
    final int REQUEST_IMAGE_CAPTURE = 1;
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
        Button uploadUserImageButton = view.findViewById(R.id.upload_user_image_button);
        userNameEditText = view.findViewById(R.id.user_name_edit_text);
        userMobileNumberEditText = view.findViewById(R.id.user_mobile_edit_text);
        userTreeTargetEditText = view.findViewById(R.id.user_tree_target_edit_text);

        submitRegistrationDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getDataAndCallApi();


                Intent intent = new Intent(getActivity(), UserMainActivity.class);
                startActivity(intent);
            }
        });

        uploadUserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(getActivity(), "Sorry Camera not found", Toast.LENGTH_LONG).show();
                }
                }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            viewModel.setUserImage(imageBitmap);
        }
    }

    private void getDataAndCallApi() {
                //get all the data from the edit texts
                String userName = userNameEditText.getText().toString();
                long userMobileNumber = Long.parseLong(userMobileNumberEditText.getText().toString());
                String userTreeTarget = userTreeTargetEditText.getText().toString();

                viewModel.setUserDetails(userName, userMobileNumber, userTreeTarget);
                viewModel.sendUserDetails();
    }
}
