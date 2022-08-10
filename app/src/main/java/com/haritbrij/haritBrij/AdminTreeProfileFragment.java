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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haritbrij.haritBrij.models.Tree;


public class AdminTreeProfileFragment extends Fragment {
    AdminViewModel viewModel;
    ImageView treeProfileImageView, uploadImageView1, uploadImageView2, uploadImageView3;
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
        villageTextView = view.findViewById(R.id.villageTextView);
        treeProfileImageView = view.findViewById(R.id.treeProfileImageView);
        uploadImageView1 = view.findViewById(R.id.UploadImageView1);
        uploadImageView2 = view.findViewById(R.id.UploadImageView2);
        uploadImageView3 = view.findViewById(R.id.UploadImageView3);
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
