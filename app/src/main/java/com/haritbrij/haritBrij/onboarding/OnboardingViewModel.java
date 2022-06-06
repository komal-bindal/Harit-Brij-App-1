package com.haritbrij.haritBrij.onboarding;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haritbrij.haritBrij.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class OnboardingViewModel extends AndroidViewModel {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        private long phoneNumber;

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

        public SharedPreferences.Editor getSharedPreferenceEditor() {
                return editor;
        }

        public void addPhoneNumber(long phoneNumber) {
                this.phoneNumber = phoneNumber;
        }

        private int generateRandomNumber() {
                Random rnd = new Random();
                return rnd.nextInt(9999);
        }

        public void sendOtp() {
                int otp = generateRandomNumber();

                String myUrl = "http://sms.vrinfosoft.co.in/unified.php?usr=27280&pwd=8126253636&ph=+91" + phoneNumber + "&text=" + "Your Harit-Brij OTP is - " + otp + ". Please do not share with anyone";
                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                                Log.d(getClass().getSimpleName(), response);
                        },
                        volleyError -> Log.e(getClass().getSimpleName(), volleyError.getMessage())
                );

                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(myRequest);
        }

        public void sendUserDetails(String userName, long userMobileNumber, int userTreeTarget) {
                String url = "http://172.16.78.53/api/signup.php/";

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("name", userName);
                params.put("mobile", String.valueOf(userMobileNumber));
                params.put("target", String.valueOf(userTreeTarget));

                JsonObjectRequest myRequest = new JsonObjectRequest(url, new JSONObject(params), response -> {

                }, error -> {

                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(myRequest);
        }

}
