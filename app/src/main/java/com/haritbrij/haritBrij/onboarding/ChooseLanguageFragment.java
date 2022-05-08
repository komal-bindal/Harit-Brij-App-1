package com.haritbrij.haritBrij.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.haritbrij.haritBrij.R;

public class ChooseLanguageFragment extends Fragment {
    OnboardingViewModel viewModel;
    Button chooseEnglishButton;
    Button chooseHindiButton;

    public ChooseLanguageFragment() {
        super(R.layout.fragment_choose_language);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        chooseEnglishButton = view.findViewById(R.id.choose_english_button);
        chooseHindiButton = view.findViewById(R.id.choose_hindi_button);

        chooseEnglishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setEnglishLanguage();
                navigateToChooseUser();
            }
        });

        chooseHindiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setHindiLanguage();
                navigateToChooseUser();
            }
        });
    }

    private void navigateToChooseUser() {
        Fragment fragment = new UserTypeFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out);
        fragmentTransaction.commit();
    }
}
