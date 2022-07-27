package com.haritbrij.haritBrij;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haritbrij.haritBrij.models.Tree;

import java.util.ArrayList;
import java.util.Objects;

public class TreeMapFragment extends Fragment {
    MapView mapView;
    UserMainViewModel viewModel;
    GoogleMap mGoogleMap;
    EditText searchTreeByUtid;
    ImageView mapSearchTreeImageView;

    public TreeMapFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tree_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserMainViewModel.class);

        searchTreeByUtid = view.findViewById(R.id.searchTreeByUtid);
        mapSearchTreeImageView = view.findViewById(R.id.mapSearchTreeIcon);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.user_map_view);
        mapView.onCreate(savedInstanceState);

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
//                    LatLng sydney = new LatLng(-33.88, 151.21);
//                    googleMap.addMarker(new MarkerOptions()
//                            .position(sydney));
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mapSearchTreeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUtid = searchTreeByUtid.getText().toString();
                ArrayList<Tree> treeList = viewModel.getTreeList();
                for(Tree tree: treeList) {
                    if(enteredUtid.equals(tree.id)) {
                        mGoogleMap.clear();
                        double latitude = tree.latitude;
                        double longitude = tree.longitude;
                        LatLng treeMarker = new LatLng(latitude, longitude);
                        mGoogleMap.addMarker(new MarkerOptions().position(treeMarker));
                    }
                }
            }
        });
    }

    private void setTreeMarker() {
        ArrayList<Tree> treeList = viewModel.getTreeList();
        for(Tree tree: treeList) {
            double latitude = tree.latitude;
            double longitude = tree.longitude;
            LatLng treeMarker = new LatLng(latitude, longitude);
            Log.e("TreeMapFragment", latitude+" "+longitude);
            mGoogleMap.addMarker(new MarkerOptions().position(treeMarker));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(treeMarker));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
        }
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
