package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.onboarding.OnboardingViewModel;

public class UserTypeFragment extends Fragment {
    //TODO Add the usertype imageview and button to a single viewgroup and set onclick listener on the viewgroup.
    OnboardingViewModel viewModel;
    Button chooseAdminButton, chooseUserButton;
    private final String TAG = "UserTypeFragment";

    public UserTypeFragment() {
        super(R.layout.fragment_user_type);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        chooseAdminButton = view.findViewById(R.id.choose_admin_button);
        chooseUserButton = view.findViewById(R.id.choose_user_button);

        chooseAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, new EnterMobileFragment()).addToBackStack(null).commit();
                Toast.makeText(getContext(), "Admin Profile", Toast.LENGTH_LONG).show();
                viewModel.getSharedPreferenceEditor().putString("profile", "admin");
            }
        });

        chooseUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, new EnterMobileFragment()).addToBackStack(null).commit();
                Toast.makeText(getContext(), "User Profile", Toast.LENGTH_LONG).show();
                viewModel.getSharedPreferenceEditor().putString("profile", "user");
            }
        });
    }
}
