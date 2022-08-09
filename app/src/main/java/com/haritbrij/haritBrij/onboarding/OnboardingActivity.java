package com.haritbrij.haritBrij.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.haritbrij.haritBrij.AdminMainActivity;
import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.UserMainActivity;
import com.haritbrij.haritBrij.utils.LocaleHelper;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;

import java.util.Locale;

public class OnboardingActivity extends AppCompatActivity {
    public  String langCode = "hi";

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LocaleHelper.changeLanguage(newBase, langCode);
        super.attachBaseContext(context);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        OnboardingViewModel viewModel = new ViewModelProvider(OnboardingActivity.this).get(OnboardingViewModel.class);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean isUserSignedIn = sharedPreferences.getBoolean(SharedPrefConstants.isUserSignedIn, false);
        boolean isAdminSignedIn = sharedPreferences.getBoolean(SharedPrefConstants.isAdminSignedIn, false);
        langCode = viewModel.sharedPreferences.getString(getApplication().getString(R.string.user_language), null);
        if(langCode != null){
            setAppLocale(langCode);
        }
        //add the ChooseLanguageFragment to the stack
        if (savedInstanceState == null) {
            if (isUserSignedIn) {
                Intent intent = new Intent(OnboardingActivity.this
                        , UserMainActivity.class);
                startActivity(intent);
                this.finish();
            } else if (isAdminSignedIn) {
                Intent intent = new Intent(OnboardingActivity.this
                        , AdminMainActivity.class);
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
