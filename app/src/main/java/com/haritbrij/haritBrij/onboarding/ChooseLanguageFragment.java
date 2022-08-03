package com.haritbrij.haritBrij.onboarding;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.util.Locale;

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
                setAppLocale("en");
            }
        });

        chooseHindiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setHindiLanguage();
                setAppLocale("hi");
                navigateToChooseUser();
            }
        });
    }

    private void navigateToChooseUser() {
        Fragment fragment = new UserTypeFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment).addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out);
        fragmentTransaction.commit();
    }

    private void setAppLocale(String localeCode) {
        Resources res = getResources();
        DisplayMetrics de = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf, de);
    }
}
