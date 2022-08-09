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
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.ImageHelper;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TreeRegisterFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private final String TAG = "TreeRegisterFragment";
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
    TextView treesPlantedTextView;

    public TreeRegisterFragment() {
        super(R.layout.fragment_tree_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(UserMainViewModel.class);
        String baseUrl = VolleySingleton.getBaseUrl();

        Spinner districtSpinner = view.findViewById(R.id.districtSpinner);
        Spinner blockSpinner = view.findViewById(R.id.blockSpinner);
        Spinner villageSpinner = view.findViewById(R.id.villageSpinner);
        Spinner speciesSpinner = view.findViewById(R.id.speciesSpinner);
        treesPlantedTextView = ((UserMainActivity) getContext()).findViewById(R.id.registeredTreesTextView);

        addTreeImageView = view.findViewById(R.id.registerTreeImageView);
        registerTreeButton = view.findViewById(R.id.treeRegisterSubmitButton);
        String myUrl = baseUrl + "getalluser.php";
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try {
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        JSONArray jsonArray = myJsonObject.getJSONArray("body");
                        for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
                            JSONObject indexedOrg = jsonArray.getJSONObject(jsonArrayIndex);
                            Log.e("Target", Integer.parseInt(indexedOrg.getString("target"))+" " +Integer.parseInt(indexedOrg.getString("completed")));
                            if(indexedOrg.getString("uid").equals(viewModel.sharedPreferences.getString("uid", "0"))){
                                if (Integer.parseInt(indexedOrg.getString("target")) <= Integer.parseInt(indexedOrg.getString("completed"))) {

                                    addTreeImageView.setEnabled(false);
                                    registerTreeButton.setEnabled(false);
                                } else {
                                    addTreeImageView.setEnabled(true);
                                    registerTreeButton.setEnabled(true);
                                }
                            }
                        }
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                },
                error -> {
                    Log.e(getClass().getSimpleName(), error.toString());
                });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);

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


                if (addTreeImageView != null && selectedDistrict != null && selectedBlock != null && selectedVillage != null && selectedSpecies != null && latitude != null && longitude != null) {
                    //Construct the Json object
                    JSONObject object = new JSONObject();
                    try {
                        String encodedImage = ImageHelper.encodeImage(treeRegisterBitmap);

                        object.put("name", viewModel.sharedPreferences.getString(SharedPrefConstants.name, ""));
                        object.put("mobile", String.valueOf(viewModel.sharedPreferences.getLong(SharedPrefConstants.mobileNumber, 0)));
                        object.put("uid", viewModel.sharedPreferences.getString(SharedPrefConstants.uid, ""));
                        object.put("district", selectedDistrict);
                        object.put("block", selectedBlock);
                        object.put("village", selectedVillage);
                        object.put("species", selectedSpecies);
                        object.put("lat", String.valueOf(latitude));
                        object.put("long", String.valueOf(longitude));
                        object.put("img1", encodedImage);
                        Log.d("Data", object.toString());
                    } catch (JSONException exception) {
                        Log.d("error", exception.getMessage());
                    }


                    String myUrl = baseUrl + "treesignup.php/";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, myUrl, object,
                            response -> {
                                try {
                                    String utid = response.getString("strutid");
                                    Log.d("ResponseRegister", utid);
                                    viewModel.setUtid(utid);
                                    String myUrl1 = baseUrl + "searchbyutid.php/?strutid=" + utid;
                                    StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl1,
                                            res -> {
                                                try {
                                                    JSONObject myJsonObject = new JSONObject(res);
                                                    Log.d("UTID", utid);
                                                    Log.d("UTID", myJsonObject.getString("strutid"));
                                                    Tree tree = new Tree();
                                                    tree.id = utid;
                                                    tree.longitude = Double.parseDouble(myJsonObject.getString("long"));
                                                    tree.block = myJsonObject.getString("block");
                                                    tree.village = myJsonObject.getString("village");
                                                    tree.district = myJsonObject.getString("district");
                                                    tree.species = myJsonObject.getString("species");
                                                    tree.latitude = Double.parseDouble(myJsonObject.getString("lat"));
                                                    tree.image1 = myJsonObject.getString("img1");
                                                    tree.image2 = myJsonObject.getString("img2");
                                                    tree.image3 = myJsonObject.getString("img3");
                                                    tree.image4 = myJsonObject.getString("img4");
                                                    viewModel.setTree(tree);
                                                    TreeProfileFragment treeProfileFragment = new TreeProfileFragment();
                                                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.main_user_fragment_container_view, treeProfileFragment).addToBackStack(null).commit();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            },
                                            volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
                                    );
                                    VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            },
                            error -> {
                                Log.d("errorRegister", error.toString());
                                Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            }
                    );

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
                }
                String myUrl = baseUrl + "getalluser.php";
                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                            try {
                                //Create a JSON object containing information from the API.
                                JSONObject myJsonObject = new JSONObject(response);
                                JSONArray jsonArray = myJsonObject.getJSONArray("body");
                                for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
                                    JSONObject indexedOrg = jsonArray.getJSONObject(jsonArrayIndex);
                                    Log.e("Target", Integer.parseInt(indexedOrg.getString("target")) + " " + Integer.parseInt(indexedOrg.getString("completed")));
                                    if (indexedOrg.getString("uid").equals(viewModel.sharedPreferences.getString("uid", "0")))
                                        treesPlantedTextView.setText(String.valueOf(Integer.parseInt(indexedOrg.getString("completed"))+1));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
                );

                VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);
            }
        });
    }

    private void getTreeData(String utid) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap resizedImage = Bitmap.createScaledBitmap(imageBitmap, 500, 500, true);
            treeRegisterBitmap = resizedImage;
            addTreeImageView.setImageBitmap(resizedImage);
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
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        Toast.makeText(getActivity(), isGPSEnabled + " " + isNetworkEnabled, Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else if (isGPSEnabled || isNetworkEnabled) {
            @SuppressLint("MissingPermission") Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Log.d(TAG, "gps location.");
                Log.d(TAG, longitude + " ------- " + latitude);
            } else {
                Log.d(TAG, "Unable to find gps location.");
                if (isNetworkEnabled) {
                    @SuppressLint("MissingPermission") Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (locationNetwork != null) {
                        double lat = locationNetwork.getLatitude();
                        double longi = locationNetwork.getLongitude();
                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(longi);
                        Log.d(TAG, "network location.");
                        Log.d(TAG, longitude + " ------- " + latitude);
                    }
//                    else {
//                        Log.d(TAG, "Unable to find network location.");
//                        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
//                        @SuppressLint("MissingPermission") GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
//
//                        int cellid = cellLocation.getCid();
//                        int celllac = cellLocation.getLac();
//
//                        Log.d(TAG + " CellLocation", cellLocation.toString());
//                        Log.d(TAG + " Cellid", String.valueOf(cellid));
//                        Log.d(TAG + " cellcode", String.valueOf(celllac));
//                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to find  location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


