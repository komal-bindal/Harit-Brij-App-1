package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.onboarding.OnboardingViewModel;

public class UserTypeFragment extends Fragment {
    OnboardingViewModel viewModel;
    UserTypeFragment() {
        super(R.layout.fragment_user_type);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);
    }
}
