package com.haritbrij.haritBrij;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        //add the ChooseLanguageFragment to the stack
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ChooseLanguageFragment.class, null)
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                    .commit();
        }
    }
}
