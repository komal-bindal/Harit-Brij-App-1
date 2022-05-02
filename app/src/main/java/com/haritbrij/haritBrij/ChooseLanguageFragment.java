package com.haritbrij.haritBrij;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
            }
        });

        chooseHindiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setHindiLanguage();
            }
        });
    }
}
