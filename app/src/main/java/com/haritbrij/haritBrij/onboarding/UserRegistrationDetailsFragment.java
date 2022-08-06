package com.haritbrij.haritBrij.onboarding;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.utils.VolleySingleton;

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
        viewModel.setLogin(false);
        Button submitRegistrationDetailsButton = view.findViewById(R.id.submit_registration_details_button);
        Button uploadUserImageButton = view.findViewById(R.id.upload_user_image_button);
        userNameEditText = view.findViewById(R.id.user_name_edit_text);
        userMobileNumberEditText = view.findViewById(R.id.user_mobile_edit_text);
        userTreeTargetEditText = view.findViewById(R.id.user_tree_target_edit_text);

        userNameEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString().length() == 20) {
                            Toast.makeText(getContext(), "Maximum limit is 20", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );


        submitRegistrationDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseUrl = VolleySingleton.getBaseUrl();
                String myUrl = baseUrl + "login.php/" + "?mobile=" + userMobileNumberEditText.getText().toString();
                final boolean[] doesExist = {false};
                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                            Toast.makeText(getContext(), "Mobile number already exist", Toast.LENGTH_SHORT).show();
                        },
                        volleyError -> {
                            getData();
                            Toast.makeText(getContext(), "OTP Sent", Toast.LENGTH_LONG).show();
                            viewModel.addMobileNumber(Long.parseLong(userMobileNumberEditText.getText().toString()));
                            viewModel.sendOtp();
//                Toast.makeText(getActivity(), "Registration successful. Kindly Signin", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container_view, new EnterOtpFragment()).addToBackStack(null).commit();
                        }
                );
                VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);


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
//            upperUserView.setImageBitmap(imageBitmap);
            viewModel.setUserImage(imageBitmap);
        }
    }


    private boolean isUserExist(String enteredMobileNumber) {
        String baseUrl = VolleySingleton.getBaseUrl();
        String myUrl = baseUrl + "login.php/" + "?mobile=" + enteredMobileNumber;
        final boolean[] doesExist = {false};
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    doesExist[0] = true;
                },
                volleyError -> {
                    doesExist[0] = false;
                }
        );

        return doesExist[0];
    }


    private void getData() {
        //get all the data from the edit texts
        String userName = userNameEditText.getText().toString();
        long userMobileNumber = Long.parseLong(userMobileNumberEditText.getText().toString());
        String userTreeTarget = userTreeTargetEditText.getText().toString();
        viewModel.setUserDetails(userName, userMobileNumber, userTreeTarget);
    }
}
