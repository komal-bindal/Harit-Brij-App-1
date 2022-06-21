package com.haritbrij.haritBrij;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.haritbrij.haritBrij.onboarding.UserRegistrationDetailsFragment;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class TreeRegisterFragment extends Fragment  implements AdapterView.OnItemSelectedListener {
    UserMainViewModel viewModel;
    LocationManager locationManager;
    String latitude, longitude;

    Bitmap treeRegisterBitmap;
    ImageView addTreeImageView;
    Button registerTreeButton;

    String selectedDistrict;
    String selectedBlock;
    String selectedVillage;
    String selectedSpecies;

    public TreeRegisterFragment() {
        super(R.layout.fragment_tree_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(UserMainViewModel.class);

        Spinner districtSpinner = view.findViewById(R.id.districtSpinner);
        Spinner blockSpinner = view.findViewById(R.id.blockSpinner);
        Spinner villageSpinner = view.findViewById(R.id.villageSpinner);
        Spinner speciesSpinner = view.findViewById(R.id.speciesSpinner);

        addTreeImageView = view.findViewById(R.id.registerTreeImageView);
        registerTreeButton = view.findViewById(R.id.treeRegisterSubmitButton);

        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.District, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> blockAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Block, android.R.layout.simple_spinner_item);
        blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockSpinner.setAdapter(blockAdapter);
        blockSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> villageAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Villages, android.R.layout.simple_spinner_item);
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        villageSpinner.setAdapter(villageAdapter);
        villageSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> speciesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Species, android.R.layout.simple_spinner_item);
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciesSpinner.setAdapter(speciesAdapter);
        speciesSpinner.setOnItemSelectedListener(this);

        addTreeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 2);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(getActivity(), "Sorry Camera not found", Toast.LENGTH_LONG).show();
                }
            }
        });

        registerTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }

                if(addTreeImageView != null && selectedDistrict != null && selectedBlock != null && selectedVillage != null && selectedSpecies != null) {
                    //Construct the Json object
                    JSONObject object = new JSONObject();
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        treeRegisterBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                        byte[] b = baos.toByteArray();

                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                        object.put("name", viewModel.sharedPreferences.getString(SharedPrefConstants.name, ""));
                        object.put("mobile",String.valueOf(viewModel.sharedPreferences.getLong(SharedPrefConstants.mobileNumber, 0)));
                        object.put("uid", viewModel.sharedPreferences.getString(SharedPrefConstants.uid, ""));
                        object.put("district", selectedDistrict);
                        object.put("block", selectedBlock);
                        object.put("village", selectedVillage);
                        object.put("species", selectedSpecies);
                        object.put("lat", String.valueOf(latitude));
                        object.put("long", String.valueOf(longitude));
                        object.put("img1", encodedImage);

                    } catch (JSONException exception) {

                    }

                    String baseUrl = VolleySingleton.getBaseUrl();
                    String myUrl = baseUrl + "treesignup.php/";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, myUrl, object,
                            response -> {
                                Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            },
                            error -> {
                                Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            }
                    );

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            treeRegisterBitmap = imageBitmap;

            addTreeImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String adapterViewSelectedItem = adapterView.getItemAtPosition(i).toString();

        switch (adapterView.getId()) {
            case R.id.districtSpinner:
                selectedDistrict = adapterViewSelectedItem;
                break;
            case R.id.blockSpinner:
                selectedBlock = adapterViewSelectedItem;
                break;
            case R.id.villageSpinner:
                selectedVillage = adapterViewSelectedItem;
                break;
            case R.id.speciesSpinner:
                selectedSpecies = adapterViewSelectedItem;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            @SuppressLint("MissingPermission") Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
            } else {
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
