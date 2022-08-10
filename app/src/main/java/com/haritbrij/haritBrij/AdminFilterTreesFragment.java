package com.haritbrij.haritBrij;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Calendar;

public class AdminFilterTreesFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    static int flag = 0;
    static StringBuilder startSelectedDate, endSelectedDate;
    static TextView adminStartDateTextView;
    static TextView adminEndDateTextView;
    AdminViewModel viewModel;
    ArrayList<Tree> mData = new ArrayList<>();
    TreeListAdapter mTreeListAdapter;
    String selectedDistrict;
    String selectedBlock;
    String selectedVillage;
    String selectedSpecies;
    String selectedStatus;

    Button search;
    TextView adminDistrictTextView;
    TextView adminBlockTextView;
    TextView adminVillageTextView;
    TextView adminSpeciesTextView;
    TextView adminStatusTextView;
    ImageButton adminstartDateSeletor, adminendDateSelector;
    int district1 = 0, block1 = 0, village1 = 0, species1 = 0;


    MapView mapView;
    GoogleMap mGoogleMap;

    public AdminFilterTreesFragment() {
        super(R.layout.activity_admin_filter_trees_fragment);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(AdminViewModel.class);

        Spinner districtSpinner = view.findViewById(R.id.adminDistrictSpinner);
        Spinner blockSpinner = view.findViewById(R.id.adminBlockSpinner);
        Spinner villageSpinner = view.findViewById(R.id.adminVillageSpinner);
        Spinner speciesSpinner = view.findViewById(R.id.adminSpeciesSpinner);
        Spinner statusSpinner = view.findViewById(R.id.adminStatusSpinner);

        search = view.findViewById(R.id.adminSearchButton);
        adminDistrictTextView = view.findViewById(R.id.adminDistrictTextView);
        adminBlockTextView = view.findViewById(R.id.adminBlockTextView);
        adminVillageTextView = view.findViewById(R.id.adminVillageTextView);
        adminSpeciesTextView = view.findViewById(R.id.adminSpeciesTextView);
        mapView = view.findViewById(R.id.adminTreeMapView);
        adminstartDateSeletor = view.findViewById(R.id.adminFromDateImageButton);
        adminendDateSelector = view.findViewById(R.id.adminToDateImageButton);
        adminStartDateTextView = view.findViewById(R.id.adminStartDateTextView);
        adminEndDateTextView = view.findViewById(R.id.adminEndDateTextView);
        adminStatusTextView = view.findViewById(R.id.adminStatusTextView);
        mapView.onCreate(savedInstanceState);
        mapView.setVisibility(View.INVISIBLE);

        RecyclerView searchRecyclerView = view.findViewById(R.id.adminTreeListRecyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mTreeListAdapter = new TreeListAdapter(mData);


        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.District, R.layout.simple_spinner_item_1);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> blockAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Block, R.layout.simple_spinner_item_1);
        blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockSpinner.setAdapter(blockAdapter);
        blockSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> villageAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Villages, R.layout.simple_spinner_item_1);
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        villageSpinner.setAdapter(villageAdapter);
        villageSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> speciesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Species, R.layout.simple_spinner_item_1);
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciesSpinner.setAdapter(speciesAdapter);
        speciesSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Status, R.layout.simple_spinner_item_1);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(this);



        adminstartDateSeletor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 0;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        adminendDateSelector.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = 1;
                // TODO Auto-generated method stub
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String baseUrl = VolleySingleton.getBaseUrl();
                String myUrl = baseUrl + "getalltree.php";
                int[] district = {0};
                int[] block = {0};
                int[] village = {0};
                int[] species = {0};
                int[] status = {0};
                mData.clear();


                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                            try {
                                //Create a JSON object containing information from the API.
                                JSONObject myJsonObject = new JSONObject(response);
                                JSONArray jsonArray = myJsonObject.getJSONArray("body");


                                //save the from response in new tree object
                                for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {

                                    JSONObject indexedTree = jsonArray.getJSONObject(jsonArrayIndex);
                                    Log.d("Filter", indexedTree.getString("district") + " " + selectedDistrict);


                                    if (selectedDistrict.equals("All(District)")) {
                                        district[0] = jsonArray.length();
                                    } else if (indexedTree.getString("district").equals(selectedDistrict)) {
                                        district[0]++;
                                    }

                                    if (selectedBlock.equals("All(Block)")) {
                                        block[0] = jsonArray.length();
                                    } else if (indexedTree.getString("block").equals(selectedBlock) && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))) {
                                        block[0]++;
                                    }
                                    if (selectedVillage.equals("All(Villages)")) {
                                        village[0] = jsonArray.length();
                                    } else if (indexedTree.getString("village").equals(selectedVillage) && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))
                                            && (selectedBlock.equals("All(Block)") ||
                                            selectedBlock.equals(indexedTree.getString("block")))) {
                                        village[0]++;
                                    }
                                    if (selectedSpecies.equals("All(Species)")) {
                                        species[0] = jsonArray.length();
                                        Tree tree = new Tree();
                                        tree.id = indexedTree.getString("strutid");
                                        tree.district = indexedTree.getString("district");
                                        tree.block = indexedTree.getString("block");
                                        tree.village = indexedTree.getString("village");
                                        tree.species = indexedTree.getString("species");
                                        tree.image1 = indexedTree.getString("img1");
                                        tree.image2 = indexedTree.getString("img2");
                                        tree.image3 = indexedTree.getString("img3");
                                        tree.image4 = indexedTree.getString("img4");
                                        tree.latitude = indexedTree.getDouble("lat");
                                        tree.longitude = indexedTree.getDouble("long");
                                        Log.d("TreeDetails", tree.latitude + " " + tree.longitude);
                                        mData.add(tree);
                                    } else if (indexedTree.getString("species").equals(selectedSpecies) && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))
                                            && (selectedBlock.equals("All(Block)") ||
                                            selectedBlock.equals(indexedTree.getString("block")))
                                            && (selectedVillage.equals("All(Villages)") ||
                                            selectedVillage.equals(indexedTree.getString("village")))) {
                                        species[0]++;
                                        if (indexedTree.getString("time").split(" ")[0].compareTo(String.valueOf(startSelectedDate)) > 0 && indexedTree.getString("time").split(" ")[0].compareTo(String.valueOf(endSelectedDate)) < 0){

                                            Tree tree = new Tree();
                                            tree.id = indexedTree.getString("strutid");
                                            tree.district = indexedTree.getString("district");
                                            tree.block = indexedTree.getString("block");
                                            tree.village = indexedTree.getString("village");
                                            tree.species = indexedTree.getString("species");
                                            tree.image1 = indexedTree.getString("img1");
                                            tree.image2 = indexedTree.getString("img2");
                                            tree.image3 = indexedTree.getString("img3");
                                            tree.image4 = indexedTree.getString("img4");
                                            tree.latitude = indexedTree.getDouble("lat");
                                            tree.longitude = indexedTree.getDouble("long");
                                            Log.d("TreeDetails", tree.latitude + " " + tree.longitude);
                                            mData.add(tree);
                                        }
                                        if((startSelectedDate==null && endSelectedDate==null)) {
                                            Tree tree = new Tree();
                                            tree.id = indexedTree.getString("strutid");
                                            tree.district = indexedTree.getString("district");
                                            tree.block = indexedTree.getString("block");
                                            tree.village = indexedTree.getString("village");
                                            tree.species = indexedTree.getString("species");
                                            tree.image1 = indexedTree.getString("img1");
                                            tree.image2 = indexedTree.getString("img2");
                                            tree.image3 = indexedTree.getString("img3");
                                            tree.image4 = indexedTree.getString("img4");
                                            tree.latitude = indexedTree.getDouble("lat");
                                            tree.longitude = indexedTree.getDouble("long");
                                            Log.d("TreeDetails", tree.latitude + " " + tree.longitude);
                                            mData.add(tree);
                                        }
                                        if (indexedTree.getString("status2").equals(selectedStatus)) {
                                            status[0]++;
                                        }

                                    }

                                    Log.e("District", String.valueOf(district[0]));
                                    adminDistrictTextView.setText(String.valueOf(district[0]));
                                    adminBlockTextView.setText(String.valueOf(block[0]));
                                    adminVillageTextView.setText(String.valueOf(village[0]));
                                    adminSpeciesTextView.setText(String.valueOf(species[0]));
                                    adminStatusTextView.setText(String.valueOf(status[0]));

                                }
                                Toast.makeText(getActivity(), String.valueOf(mData.size()), Toast.LENGTH_SHORT).show();
                                viewModel.setTreeList(mData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
                );

                VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);
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

                            setTreeMarker();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                mapView.setVisibility(View.VISIBLE);


                mTreeListAdapter = new TreeListAdapter(mData);
                searchRecyclerView.setAdapter(mTreeListAdapter);

                mTreeListAdapter.setOnItemClickListener(new TreeListAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        viewModel.setPosition(position);
                        AdminTreeProfileFragment admintreeProfileFragment = new AdminTreeProfileFragment();
                        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_admin_fragment_container_view, admintreeProfileFragment).addToBackStack(null).commit();
                    }
                });


            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String adapterViewSelectedItem = adapterView.getItemAtPosition(i).toString();

        switch (adapterView.getId()) {
            case R.id.adminDistrictSpinner:
                selectedDistrict = adapterViewSelectedItem;
                break;
            case R.id.adminBlockSpinner:
                selectedBlock = adapterViewSelectedItem;
                break;
            case R.id.adminVillageSpinner:
                selectedVillage = adapterViewSelectedItem;
                break;
            case R.id.adminSpeciesSpinner:
                selectedSpecies = adapterViewSelectedItem;
                break;
        }
    }

    private void setTreeMarker() {
        ArrayList<Tree> treeList = viewModel.getTreeList();
        for (Tree tree : treeList) {
            if (tree.district.equals(selectedDistrict) || tree.block.equals(selectedBlock) || tree.village.equals(selectedVillage)
                    || tree.species.equals(selectedSpecies)) {
                double latitude = tree.latitude;
                double longitude = tree.longitude;
                LatLng treeMarker = new LatLng(latitude, longitude);
                mGoogleMap.addMarker(new MarkerOptions().position(treeMarker));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceSateate) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen
            if (flag == 0) {
                if(month<10 && day<10){
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append("0").append(day);
                }
                if(day<10 && month>10){
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append("0").append(day);
                }
                if(month<10 && day>10){
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append(day);
                }
                if(month>10 && day>10) {
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append(day);
                }
                adminStartDateTextView.setText(String.valueOf(new StringBuilder().append(day).append("/")
                        .append(month).append("/").append(year)));
            }
            if (flag == 1) {
                if(month<10 && day<10){
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append("0").append(day);
                }
                if(day<10 && month>10){
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append("0").append(day);
                }
                if(month<10 && day>10){
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append(day);
                }
                if(month>10 && day>10) {
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append(day);
                }
                adminEndDateTextView.setText(String.valueOf(new StringBuilder().append(day).append("/")
                        .append(month).append("/").append(year)));
            }

        }

    }


}

