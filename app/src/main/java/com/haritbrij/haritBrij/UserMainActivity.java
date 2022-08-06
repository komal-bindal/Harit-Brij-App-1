package com.haritbrij.haritBrij;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.haritbrij.haritBrij.onboarding.OnboardingActivity;
import com.haritbrij.haritBrij.utils.ImageHelper;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    UserMainViewModel viewModel;
    //    OnboardingViewModel onboardViewModel;
    TextView treeTargetTextView;
    TextView userNameTextView;
    TextView treesPlantedTextView;
    ImageView userImageView;
    ImageView logoutButtonView;
    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        treeTargetTextView = findViewById(R.id.treeTargetTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        treesPlantedTextView = findViewById(R.id.registeredTreesTextView);
        userImageView = findViewById(R.id.userImageView);
        logoutButtonView = findViewById(R.id.logoutButtonUser);

        viewModel = new ViewModelProvider(this).get(UserMainViewModel.class);
//        onboardViewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        treeTargetTextView.setText(viewModel.sharedPreferences.getString(SharedPrefConstants.target, ""));
        userNameTextView.setText(viewModel.sharedPreferences.getString(SharedPrefConstants.name, ""));
        bottomNavigationView = findViewById(R.id.main_user_bottom_navigation_view);

        userImageView.setImageResource(R.drawable.ic_baseline_photo_camera_24);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final TreeRegisterFragment treeRegisterFragment = new TreeRegisterFragment();
        final SearchTreeFragment searchTreeFragment = new SearchTreeFragment();
        final TreeMapFragment treeMapFragment = new TreeMapFragment();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            switch (item.getItemId()) {
                case R.id.action_tree_data:
                    selectedFragment = searchTreeFragment;
                    break;
                case R.id.action_tree_map:
                    selectedFragment = treeMapFragment;
                    break;
                default:
                    selectedFragment = treeRegisterFragment;
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.main_user_fragment_container_view, selectedFragment).commit();
            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.action_tree_register);

        String baseUrl = VolleySingleton.getBaseUrl();
        long mobileNumber = viewModel.sharedPreferences.getLong("mobileNumber", 0);
        String myUrl = baseUrl + "login.php/" + "?mobile=" + mobileNumber;
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try {
                        JSONObject myJsonObject = new JSONObject(response);
                        viewModel.getSharedPreferenceEditor().putString(SharedPrefConstants.uid, myJsonObject.getString("uid")).apply();
                        String display = myJsonObject.getString("display");
                        Bitmap image = ImageHelper.decodeImage(display);
                        if (image != null) {
                            userImageView.setImageBitmap(getCroppedBitmap(image));
                            flag = 1;
                        }
                treesPlantedTextView.setText(String.valueOf(myJsonObject.getString("completed")));
//                Matrix matrix = new Matrix();
//                matrix.postScale(0.5f, 0.5f);
//                Bitmap croppedBitmap = Bitmap.createBitmap(image, 100, 100,100, 100, matrix, true);
//                userImageView.setImageBitmap(croppedBitmap);
                    } catch (JSONException exception) {
                        Toast.makeText(this, "Please register again " + mobileNumber, Toast.LENGTH_LONG).show();

                    }
                },
                volleyError -> {
//                    Toast.makeText(this, "Please register again " + mobileNumber, Toast.LENGTH_LONG).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(myRequest);

        //set the number of registered trees.
        String url = baseUrl + "readusertree.php/?uid=" + viewModel.sharedPreferences.getString("uid", "0");
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        JSONArray jsonArray = myJsonObject.getJSONArray("body");

                        viewModel.setPlantedTrees(Integer.parseInt(myJsonObject.getString("itemCount")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
        );
        VolleySingleton.getInstance(this).addToRequestQueue(request);

        logoutButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getSharedPreferenceEditor().putBoolean(SharedPrefConstants.isUserSignedIn, false).apply();
                Intent intent = new Intent(UserMainActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if (flag == 0) {
            userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(takePictureIntent, 2);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                        Toast.makeText(UserMainActivity.this, "Sorry Camera not found", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        output.setHeight(217);
//        output.setWidth(200);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap resizedImage = Bitmap.createScaledBitmap(imageBitmap, 500, 500, true);
            userImageView.setImageBitmap(getCroppedBitmap(resizedImage));
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uid", viewModel.sharedPreferences.getString(SharedPrefConstants.uid, ""));
                jsonObject.put("name", viewModel.sharedPreferences.getString(SharedPrefConstants.name, ""));
                jsonObject.put("mobile", viewModel.sharedPreferences.getLong(SharedPrefConstants.mobileNumber, 0));
                jsonObject.put("target", viewModel.sharedPreferences.getString(SharedPrefConstants.target, ""));
                jsonObject.put("display", ImageHelper.encodeImage(resizedImage));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String baseUrl = VolleySingleton.getBaseUrl();
            String url = baseUrl + "displayupload.php/";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    response -> {
                        Toast.makeText(getApplication(), "DataUpdated", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), response.toString());
                    },
                    error -> {
                        Toast.makeText(getApplication(), "DataUpdated", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), error.toString());
                    }
            );

            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            requestQueue.add(jsonObjectRequest);


        }
    }
}
