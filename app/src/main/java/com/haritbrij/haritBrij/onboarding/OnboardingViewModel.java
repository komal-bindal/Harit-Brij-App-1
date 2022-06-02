package com.haritbrij.haritBrij.onboarding;

import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.haritbrij.haritBrij.MainActivity;
import com.haritbrij.haritBrij.R;

import java.util.concurrent.TimeUnit;

public class OnboardingViewModel extends AndroidViewModel {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getApplication().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        //TODO: mAuth = FirebaseAuth.getInstance();
        private FirebaseAuth mAuth;
        private String verificationId;

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

        public void sendVerificationCode(String number, Activity activity) {
                // this method is used for getting
                // OTP on user phone number.
                mAuth = FirebaseAuth.getInstance();

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(number)            // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(activity)                 // Activity (for callback binding)
                                .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
        }

        // callback method is called on Phone auth provider.
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks

                // initializing our callbacks for on
                // verification callback method.
                mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                // below method is used when
                // OTP is sent from Firebase
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        // when we receive the OTP it
                        // contains a unique id which
                        // we are storing in our string
                        // which we have already created.
                        verificationId = s;
                }

                // this method is called when user
                // receive OTP from Firebase.
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // below line is used for getting OTP code
                        // which is sent in phone auth credentials.
                        final String code = phoneAuthCredential.getSmsCode();

                        // checking if the code
                        // is null or not.
                        if (code != null) {
                                // if the code is not null then
                                // we are setting that code to
                                // our OTP edittext field.
//                                edtOTP.setText(code);

                                // after setting this code
                                // to OTP edittext field we
                                // are calling our verifycode method.
//                                verifyCode(code);
                        }
                }

                // this method is called when firebase doesn't
                // sends our OTP code due to any error or issue.
                @Override
                public void onVerificationFailed(FirebaseException e) {
                        // displaying error message with firebase exception.
                        Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
        };
}
