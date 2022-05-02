package com.haritbrij.haritBrij;

import static android.provider.Settings.System.getString;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

public class OnboardingViewModel extends AndroidViewModel {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;

        public OnboardingViewModel(@NonNull Application application) {
                super(application);
                editor = sharedPreferences.edit();
        }

        public void setEnglishLanguage() {
                editor.putString(getApplication().getString(R.string.user_language), "english");
                editor.apply();

                Toast.makeText(getApplication(), "Set Language to English", Toast.LENGTH_SHORT).show();
        }

        public void setHindiLanguage() {
                editor.putString(getApplication().getString(R.string.user_language), "hindi");
                editor.apply();
        }
}
