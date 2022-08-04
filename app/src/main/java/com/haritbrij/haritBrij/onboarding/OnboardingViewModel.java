package com.haritbrij.haritBrij.onboarding;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.utils.ImageHelper;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class OnboardingViewModel extends AndroidViewModel {
    private final SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    private long userMobileNumber;
    private String userName;
    private String userTreeTarget;
    private Bitmap userImage;
    private String otp;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    private boolean isLogin = false;

    public OnboardingViewModel(@NonNull Application application) {
        super(application);
        editor = sharedPreferences.edit();
    }

    public void setEnglishLanguage() {
        editor.putString(getApplication().getString(R.string.user_language), "english");
        editor.apply();

        //Set language to hindi
//                Locale locale = new Locale("hi");
//                Locale.setDefault(locale);
//                Resources resources = getApplication().getResources();
//                Configuration config = resources.getConfiguration();
//                config.setLocale(locale);
//                resources.updateConfiguration(config, resources.getDisplayMetrics());
//
//                Toast.makeText(getApplication(), "Set Language to English", Toast.LENGTH_SHORT).show();
    }

    public void setHindiLanguage() {
        editor.putString(getApplication().getString(R.string.user_language), "hindi");
        editor.apply();
    }

    public SharedPreferences.Editor getSharedPreferenceEditor() {
        return editor;
    }

    public void addMobileNumber(long mobileNum) {
        userMobileNumber = mobileNum;
    }

    private String generateRandomNumber() {
        Random rnd = new Random();
        int randomOtp = rnd.nextInt(10000);
        @SuppressLint("DefaultLocale") String otpString = String.format("%04d%n", randomOtp);
        return otpString;
    }

    public void sendOtp() {
        otp = generateRandomNumber();

                /* Sends generated otp to given number. Replaced it with Toast for now.
                String myUrl = "http://sms.vrinfosoft.co.in/unified.php?usr=27280&pwd=8126253636&ph=+91" + phoneNumber + "&text=" + "Your Harit-Brij OTP is - " + otp + ". Please do not share with anyone";
                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                                Log.d(getClass().getSimpleName(), response);
                        },
                        volleyError -> Log.e(getClass().getSimpleName(), volleyError.getMessage())
                );

                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(myRequest);
                 */

        Toast.makeText(getApplication(), String.valueOf(otp), Toast.LENGTH_LONG).show();
    }

    public String getOtp() {
        return otp;
    }

    public void sendUserDetails() {
        String baseUrl = VolleySingleton.getBaseUrl();
        String url = baseUrl + "signup.php/";

        JSONObject object = new JSONObject();
        try {
            object.put("name", userName);
            object.put("mobile", userMobileNumber);
            object.put("target", userTreeTarget);
            if (userImage != null) {
                object.put("display", ImageHelper.encodeImage(userImage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                response -> {
                    Toast.makeText(getApplication(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), response.toString());
                },
                error -> {
                    Toast.makeText(getApplication(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), error.toString());
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(jsonObjectRequest);
    }

    public void setUserDetails(String userName, long userMobileNumber, String userTreeTarget) {
        this.userName = userName;
        this.userMobileNumber = userMobileNumber;
        this.userTreeTarget = userTreeTarget;

        editor.putString(SharedPrefConstants.target, userTreeTarget).commit();
        editor.putString(SharedPrefConstants.name, userName).commit();

    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public long getMobileNumber() {
        return userMobileNumber;
    }
}
