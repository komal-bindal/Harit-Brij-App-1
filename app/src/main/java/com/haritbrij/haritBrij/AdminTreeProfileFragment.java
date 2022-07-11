package com.haritbrij.haritBrij;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.ImageHelper;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;


public class AdminTreeProfileFragment extends Fragment {
    AdminViewModel viewModel;
    ImageView treeProfileImageView,uploadImageView1,uploadImageView2,uploadImageView3;
    TextView utIdTextView;
    TextView districtTextView;
    TextView blockTextView;
    TextView villageTextView;
    MapView mapView;
    GoogleMap mGoogleMap;

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
        villageTextView  = view.findViewById(R.id.villageTextView);
        treeProfileImageView = view.findViewById(R.id.treeProfileImageView);
        uploadImageView1=view.findViewById(R.id.UploadImageView1);
        uploadImageView2=view.findViewById(R.id.UploadImageView2);
        uploadImageView3=view.findViewById(R.id.UploadImageView3);
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

        treeProfileImageView.setImageBitmap(decodedByte);

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
                    LatLng sydney = new LatLng(27.60522281732449, 77.59289534421812);
                    googleMap.addMarker(new MarkerOptions()
                            .position(sydney));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });








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
