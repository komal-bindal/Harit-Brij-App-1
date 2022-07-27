package com.haritbrij.haritBrij.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.UserMainActivity;
import com.haritbrij.haritBrij.utils.LocaleHelper;

public class OnboardingActivity extends AppCompatActivity {
    public final String langCode = "hi";

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LocaleHelper.changeLanguage(newBase, langCode);
        super.attachBaseContext(context);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
        //add the ChooseLanguageFragment to the stack
        if (savedInstanceState == null) {
            if (isSignedIn) {
                Intent intent = new Intent(OnboardingActivity.this
                        , UserMainActivity.class);
                startActivity(intent);
                this.finish();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, ChooseLanguageFragment.class, null)
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                        .commit();
            }
        }
    }
}
