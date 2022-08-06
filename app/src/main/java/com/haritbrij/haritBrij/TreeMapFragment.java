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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeMapFragment extends Fragment {
    MapView mapView;
    UserMainViewModel viewModel;
    GoogleMap mGoogleMap;
    EditText searchTreeByUtid;
    ImageView mapSearchTreeImageView;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    List<MarkerOptions> markerList;

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



        ArrayList<Tree> mData = new ArrayList<>();

        String baseUrl = VolleySingleton.getBaseUrl();

        String myUrl = baseUrl + "readusertree.php/?uid=" + viewModel.sharedPreferences.getString("uid", "0");




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
//                    LatLng sydney = new LatLng(27.60522281732449, 77.59289534421812);
//                    googleMap.addMarker(new MarkerOptions()
//                            .position(sydney));
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                    builder = new LatLngBounds.Builder();
                    StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                            response -> {
                                try {
                                    JSONObject myJsonObject = new JSONObject(response);
                                    JSONArray jsonArray = myJsonObject.getJSONArray("body");
                                    mData.clear();
                                    for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
                                        JSONObject indexedTree = jsonArray.getJSONObject(jsonArrayIndex);
                                        Tree tree = new Tree();
                                        tree.id = indexedTree.getString("strutid");
                                        tree.district = indexedTree.getString("district");
                                        tree.block = indexedTree.getString("block");
                                        tree.village = indexedTree.getString("village");
                                        tree.species = indexedTree.getString("species");
                                        tree.image1 = indexedTree.getString("img1");
                                        tree.latitude = indexedTree.getDouble("lat");
                                        tree.longitude = indexedTree.getDouble("long");
                                        mData.add(tree);
//                                        Log.d("TreeMap", tree.latitude + "------"+tree.longitude);
                                    }
                                    viewModel.setTreeList(mData);
//                        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                                    setTreeMarker();
                                    bounds = builder.build();
//                                    Log.d("TreeMap", mData.toString());
                                    int width = getResources().getDisplayMetrics().widthPixels;
                                    int height = getResources().getDisplayMetrics().heightPixels;
                                    int padding = (int) (width * 0.30);

                                    // Zoom and animate the google map to show all markers

                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                                    googleMap.animateCamera(cu);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
                    );

                    VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);




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
        markerList=new ArrayList<>();
        for(Tree tree: treeList) {
            double latitude = tree.latitude;
            double longitude = tree.longitude;
            LatLng treeMarker = new LatLng(latitude, longitude);
//            mGoogleMap.addMarker(new MarkerOptions().position(treeMarker));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(treeMarker);
            mGoogleMap.addMarker(markerOptions);
            builder.include(markerOptions.getPosition());
            Log.d("Tree", treeList.toString());
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