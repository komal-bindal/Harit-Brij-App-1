package com.haritbrij.haritBrij;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AdminTreeProfileFragment extends Fragment {
    AdminViewModel viewModel;
    ImageView treeProfileImageView, uploadImageView1, uploadImageView2, uploadImageView3;
    TextView utIdTextView;
    TextView districtTextView;
    TextView blockTextView;
    TextView villageTextView;
    MapView mapView;
    GoogleMap mGoogleMap;
    TextView statusOfTree;

    Tree tree;

    public AdminTreeProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tree_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        int position = viewModel.getPosition();
        tree = viewModel.getTreeList().get(position);

        utIdTextView = view.findViewById(R.id.utidTextView);
        districtTextView = view.findViewById(R.id.districtTextView);
        blockTextView = view.findViewById(R.id.blockTextView);
        villageTextView = view.findViewById(R.id.villageTextView);
        treeProfileImageView = view.findViewById(R.id.treeProfileImageView);
        uploadImageView1 = view.findViewById(R.id.UploadImageView1);
        uploadImageView2 = view.findViewById(R.id.UploadImageView2);
        uploadImageView3 = view.findViewById(R.id.UploadImageView3);
        statusOfTree=view.findViewById(R.id.StatusTextView);
        uploadImageView2.setVisibility(View.INVISIBLE);
        uploadImageView3.setVisibility(View.INVISIBLE);
        uploadImageView1.setVisibility(View.INVISIBLE);

        mapView = view.findViewById(R.id.treeProfileMapView);
        mapView.onCreate(savedInstanceState);


        utIdTextView.setText(tree.id);
        districtTextView.setText(tree.district);
        blockTextView.setText(tree.block);
        villageTextView.setText(tree.village);


        byte[] decodedString = Base64.decode(tree.image1, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        treeProfileImageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 123, 110, false));
//        String baseUrl = VolleySingleton.getBaseUrl();
//        String myUrl = baseUrl + "getalltree.php";
//        @SuppressLint("SetTextI18n") StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
//                response -> {
//                    try {
//                        //Create a JSON object containing information from the API.
//                        JSONObject myJsonObject = new JSONObject(response);
//                        JSONArray jsonArray = myJsonObject.getJSONArray("body");
//
//
//                        //save the from response in new tree object
//                        for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
//
//                            JSONObject indexedTree = jsonArray.getJSONObject(jsonArrayIndex);
//                            if(indexedTree.getString("strutid").equals(tree.id)) {
//                                tree.image2 = indexedTree.getString("img2");
//                                tree.image3 = indexedTree.getString("img3");
//                                tree.image4 = indexedTree.getString("img4");
//                                tree.status1=indexedTree.getString("status1");
//                                tree.status2=indexedTree.getString("status2");
//                                tree.status3=indexedTree.getString("status3");
//                                Log.e("images", tree.image2+" "+tree.image3+" "+tree.image4);
//
//                                if(!tree.image2.equals("null")){
//                                    byte[] decodedStr = Base64.decode(tree.image2, Base64.DEFAULT);
//                                    Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
//                                    uploadImageView1.setVisibility(View.VISIBLE);
//                                    uploadImageView1.setImageBitmap(Bitmap.createScaledBitmap(decodeByte, 500, 500, false));
//                                }
//
//                                if(!tree.image3.equals("null")){
//                                    byte[] decodedStr = Base64.decode(tree.image3, Base64.DEFAULT);
//                                    Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
//                                    uploadImageView2.setVisibility(View.VISIBLE);
//                                    uploadImageView2.setImageBitmap(Bitmap.createScaledBitmap(decodeByte, 500, 500, false));
//                                }
//                                if(!tree.image4.equals("null")){
//                                    byte[] decodedStr = Base64.decode(tree.image4, Base64.DEFAULT);
//                                    Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
//                                    uploadImageView3.setVisibility(View.VISIBLE);
//                                    uploadImageView3.setImageBitmap(Bitmap.createScaledBitmap(decodeByte, 500, 500, false));
//                                }
//                                if((tree.status1.equals("0") && !tree.image2.equals("null")) || (tree.status2.equals("0") && !tree.image3.equals("null")) || (tree.status3.equals("0") && !tree.image4.equals("null"))){
//                                    statusOfTree.setText("Dead");
//                                }
//                                else{
//                                    statusOfTree.setText("Alive");
//                                }
//                            }
//                        }
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
//        );
//
//        VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);


        if(!tree.image2.equals("null")){
            byte[] decodedStr = Base64.decode(tree.image2, Base64.DEFAULT);
            Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
            uploadImageView1.setVisibility(View.VISIBLE);
            uploadImageView1.setImageBitmap(Bitmap.createScaledBitmap(decodeByte, 500, 500, false));
        }

        if(!tree.image3.equals("null")){
            byte[] decodedStr = Base64.decode(tree.image3, Base64.DEFAULT);
            Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
            uploadImageView2.setVisibility(View.VISIBLE);
            uploadImageView2.setImageBitmap(Bitmap.createScaledBitmap(decodeByte, 500, 500, false));
        }
        if(!tree.image4.equals("null")){
            byte[] decodedStr = Base64.decode(tree.image4, Base64.DEFAULT);
            Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedStr, 0, decodedStr.length);
            uploadImageView3.setVisibility(View.VISIBLE);
            uploadImageView3.setImageBitmap(Bitmap.createScaledBitmap(decodeByte, 500, 500, false));
        }
        if((tree.status1.equals("0") && !tree.image2.equals("null")) || (tree.status2.equals("0") && !tree.image3.equals("null")) || (tree.status3.equals("0") && !tree.image4.equals("null"))){
            statusOfTree.setText(R.string.Dead);
        }
        else{
            statusOfTree.setText(R.string.Alive);
        }


        //setting up the mapView
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mGoogleMap = googleMap;
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);
                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                try {
                    MapsInitializer.initialize(requireActivity());
                    setTreeMarker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void setTreeMarker() {
        double latitude = tree.latitude;
        double longitude = tree.longitude;
        LatLng treeMarker = new LatLng(latitude, longitude);
        Log.e("TreeMapFragment", latitude + " " + longitude);
        mGoogleMap.addMarker(new MarkerOptions().position(treeMarker));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(treeMarker, 13.0f));

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
