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
import com.haritbrij.haritBrij.utils.LanguageTranslationHelper;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminFilterTreesFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    static int flag = 0;
    static StringBuilder startSelectedDate, endSelectedDate;
    static TextView adminStartDateTextView;
    static TextView adminEndDateTextView;
    static long startDate;
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
        startSelectedDate=null;
        endSelectedDate=null;

        viewModel = new ViewModelProvider(getActivity()).get(AdminViewModel.class);
//        Log.e("dates", startSelectedDate+" "+endSelectedDate );
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
                flag = 1;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        adminendDateSelector.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = 0;
                // TODO Auto-generated method stub
                if (startSelectedDate != null) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                } else {
                    Toast.makeText(getActivity(), "First Select start date", Toast.LENGTH_SHORT).show();
                }
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


                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                            try {
                                mData.clear();
                                //Create a JSON object containing information from the API.
                                JSONObject myJsonObject = new JSONObject(response);
                                JSONArray jsonArray = myJsonObject.getJSONArray("body");


                                //save the from response in new tree object
                                for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {

                                    JSONObject indexedTree = jsonArray.getJSONObject(jsonArrayIndex);
                                    Log.d("Filter", indexedTree.getString("district") + " " + selectedDistrict);


                                    if (selectedDistrict.equals("All(District)") && selectedBlock.equals("All(Block)") && selectedVillage.equals("All(Villages)") && selectedSpecies.equals("All(Species)")) {
                                        district[0] = jsonArray.length();
                                    } else if (selectedDistrict.equals("All(District)") || indexedTree.getString("district").equals(selectedDistrict)) {
                                        district[0]++;
                                    }

                                    if (selectedDistrict.equals("All(District)") && selectedBlock.equals("All(Block)") && selectedVillage.equals("All(Villages)") && selectedSpecies.equals("All(Species)")) {
                                        block[0] = jsonArray.length();
                                    } else if ((selectedBlock.equals("All(Block)") || indexedTree.getString("block").equals(selectedBlock)) && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))) {
                                        block[0]++;
                                    }
                                    if (selectedDistrict.equals("All(District)") && selectedBlock.equals("All(Block)") && selectedVillage.equals("All(Villages)") && selectedSpecies.equals("All(Species)")) {
                                        village[0] = jsonArray.length();
                                    } else if ((selectedVillage.equals("All(Villages)") || indexedTree.getString("village").equals(selectedVillage)) && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))
                                            && (selectedBlock.equals("All(Block)") ||
                                            selectedBlock.equals(indexedTree.getString("block")))) {
                                        village[0]++;
                                    }
                                    if (selectedDistrict.equals("All(District)") && selectedBlock.equals("All(Block)") && selectedVillage.equals("All(Villages)") && selectedSpecies.equals("All(Species)")) {
                                        species[0] = jsonArray.length();
                                    } else if ((selectedSpecies.equals("All(Species)") || indexedTree.getString("species").equals(selectedSpecies)) && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))
                                            && (selectedBlock.equals("All(Block)") ||
                                            selectedBlock.equals(indexedTree.getString("block")))
                                            && (selectedVillage.equals("All(Villages)") ||
                                            selectedVillage.equals(indexedTree.getString("village")))) {
                                        species[0]++;


                                    }
                                    if (selectedStatus.equals("Alive") && indexedTree.getString("status2").equals("1") && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))
                                            && (selectedBlock.equals("All(Block)") ||
                                            selectedBlock.equals(indexedTree.getString("block")))
                                            && (selectedVillage.equals("All(Villages)") ||
                                            selectedVillage.equals(indexedTree.getString("village")))
                                            && (selectedSpecies.equals("All(Species)") ||
                                            selectedSpecies.equals(indexedTree.getString("species")))) {
                                        status[0]++;
                                        if (indexedTree.getString("time").split(" ")[0].compareTo(String.valueOf(startSelectedDate)) > 0 && indexedTree.getString("time").split(" ")[0].compareTo(String.valueOf(endSelectedDate)) < 0) {

                                            Tree tree = new Tree();
                                            tree.id = indexedTree.getString("strutid");
                                            tree.image1 = indexedTree.getString("img1");
                                            tree.image2 = indexedTree.getString("img2");
                                            tree.image3 = indexedTree.getString("img3");
                                            tree.image4 = indexedTree.getString("img4");
                                            tree.latitude = indexedTree.getDouble("lat");
                                            tree.longitude = indexedTree.getDouble("long");
                                            tree.status1 = indexedTree.getString("status1");
                                            tree.status2 = indexedTree.getString("status2");
                                            tree.status3 = indexedTree.getString("status3");
                                            if (viewModel.sharedPreferences.getString("user_language", null).equals("hi")) {
                                                tree.block = LanguageTranslationHelper.blockEnglishToHindi(indexedTree.getString("block"));
                                                tree.village = LanguageTranslationHelper.villageEnglishToHindi(indexedTree.getString("village"));
                                                tree.district = LanguageTranslationHelper.districtEnglishToHindi(indexedTree.getString("district"));
                                                tree.species = LanguageTranslationHelper.speciesEnglishToHindi(indexedTree.getString("species"));
                                            } else {
                                                tree.block = indexedTree.getString("block");
                                                tree.village = indexedTree.getString("village");
                                                tree.district = indexedTree.getString("district");
                                                tree.species = indexedTree.getString("species");
                                            }
                                            Log.d("TreeDetails", tree.latitude + " " + tree.longitude);
                                            mData.add(tree);
                                        }
                                        if ((startSelectedDate == null && endSelectedDate == null)) {
                                            Tree tree = new Tree();
                                            tree.id = indexedTree.getString("strutid");

                                            tree.image1 = indexedTree.getString("img1");
                                            tree.image2 = indexedTree.getString("img2");
                                            tree.image3 = indexedTree.getString("img3");
                                            tree.image4 = indexedTree.getString("img4");
                                            tree.latitude = indexedTree.getDouble("lat");
                                            tree.longitude = indexedTree.getDouble("long");
                                            tree.status1 = indexedTree.getString("status1");
                                            tree.status2 = indexedTree.getString("status2");
                                            tree.status3 = indexedTree.getString("status3");

                                            if (viewModel.sharedPreferences.getString("user_language", null).equals("hi")) {
                                                tree.block = LanguageTranslationHelper.blockEnglishToHindi(indexedTree.getString("block"));
                                                tree.village = LanguageTranslationHelper.villageEnglishToHindi(indexedTree.getString("village"));
                                                tree.district = LanguageTranslationHelper.districtEnglishToHindi(indexedTree.getString("district"));
                                                tree.species = LanguageTranslationHelper.speciesEnglishToHindi(indexedTree.getString("species"));
                                            } else {
                                                tree.block = indexedTree.getString("block");
                                                tree.village = indexedTree.getString("village");
                                                tree.district = indexedTree.getString("district");
                                                tree.species = indexedTree.getString("species");
                                            }
                                            Log.d("TreeDetails", tree.latitude + " " + tree.longitude);
                                            mData.add(tree);
                                        }
                                    } else if (selectedStatus.equals("Dead") && (indexedTree.getString("status2").equals("0") && indexedTree.getString("img3") != null) && (
                                            selectedDistrict.equals("All(District)") ||
                                                    selectedDistrict.equals(indexedTree.getString("district")))
                                            && (selectedBlock.equals("All(Block)") ||
                                            selectedBlock.equals(indexedTree.getString("block")))
                                            && (selectedVillage.equals("All(Villages)") ||
                                            selectedVillage.equals(indexedTree.getString("village")))
                                            && (selectedSpecies.equals("All(Species)") ||
                                            selectedSpecies.equals(indexedTree.getString("species")))) {
                                        status[0]++;
                                        if (indexedTree.getString("time").split(" ")[0].compareTo(String.valueOf(startSelectedDate)) > 0 && indexedTree.getString("time").split(" ")[0].compareTo(String.valueOf(endSelectedDate)) < 0) {

                                            Tree tree = new Tree();
                                            tree.id = indexedTree.getString("strutid");

                                            tree.image1 = indexedTree.getString("img1");
                                            tree.image2 = indexedTree.getString("img2");
                                            tree.image3 = indexedTree.getString("img3");
                                            tree.image4 = indexedTree.getString("img4");
                                            tree.latitude = indexedTree.getDouble("lat");
                                            tree.longitude = indexedTree.getDouble("long");
                                            tree.status1 = indexedTree.getString("status1");
                                            tree.status2 = indexedTree.getString("status2");
                                            tree.status3 = indexedTree.getString("status3");

                                            if (viewModel.sharedPreferences.getString("user_language", null).equals("hi")) {
                                                tree.block = LanguageTranslationHelper.blockEnglishToHindi(indexedTree.getString("block"));
                                                tree.village = LanguageTranslationHelper.villageEnglishToHindi(indexedTree.getString("village"));
                                                tree.district = LanguageTranslationHelper.districtEnglishToHindi(indexedTree.getString("district"));
                                                tree.species = LanguageTranslationHelper.speciesEnglishToHindi(indexedTree.getString("species"));
                                            } else {
                                                tree.block = indexedTree.getString("block");
                                                tree.village = indexedTree.getString("village");
                                                tree.district = indexedTree.getString("district");
                                                tree.species = indexedTree.getString("species");
                                            }
                                            Log.d("TreeDetails", tree.latitude + " " + tree.longitude);
                                            mData.add(tree);
                                        }
                                        if ((startSelectedDate == null && endSelectedDate == null)) {
                                            Tree tree = new Tree();
                                            tree.id = indexedTree.getString("strutid");

                                            tree.image1 = indexedTree.getString("img1");
                                            tree.image2 = indexedTree.getString("img2");
                                            tree.image3 = indexedTree.getString("img3");
                                            tree.image4 = indexedTree.getString("img4");
                                            tree.latitude = indexedTree.getDouble("lat");
                                            tree.longitude = indexedTree.getDouble("long");
                                            tree.status1 = indexedTree.getString("status1");
                                            tree.status2 = indexedTree.getString("status2");
                                            tree.status3 = indexedTree.getString("status3");

                                            if (viewModel.sharedPreferences.getString("user_language", null).equals("hi")) {
                                                tree.block = LanguageTranslationHelper.blockEnglishToHindi(indexedTree.getString("block"));
                                                tree.village = LanguageTranslationHelper.villageEnglishToHindi(indexedTree.getString("village"));
                                                tree.district = LanguageTranslationHelper.districtEnglishToHindi(indexedTree.getString("district"));
                                                tree.species = LanguageTranslationHelper.speciesEnglishToHindi(indexedTree.getString("species"));
                                            } else {
                                                tree.block = indexedTree.getString("block");
                                                tree.village = indexedTree.getString("village");
                                                tree.district = indexedTree.getString("district");
                                                tree.species = indexedTree.getString("species");
                                            }
                                            Log.d("TreeDetails", tree.latitude + " " + tree.longitude);
                                            mData.add(tree);
                                        }
                                    }


                                    Log.e("District", String.valueOf(district[0]));
                                    adminDistrictTextView.setText(String.valueOf(district[0]));
                                    adminBlockTextView.setText(String.valueOf(block[0]));
                                    adminVillageTextView.setText(String.valueOf(village[0]));
                                    adminSpeciesTextView.setText(String.valueOf(species[0]));
                                    adminStatusTextView.setText(String.valueOf(status[0]));

                                }
                                viewModel.setTreeList(mData);
                                Toast.makeText(getActivity(), String.valueOf(mData.size()), Toast.LENGTH_SHORT).show();
                                Log.e("FilterTrees", String.valueOf(mData));
                                mTreeListAdapter = new TreeListAdapter(mData);
                                searchRecyclerView.setAdapter(mTreeListAdapter);
                                Log.e("ItemCount", String.valueOf(mTreeListAdapter.getItemCount()));
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
                                mapView.setVisibility(View.VISIBLE);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
                );

                VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);


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
                selectedDistrict = LanguageTranslationHelper.districtHindiToEnglish(adapterViewSelectedItem);
                break;
            case R.id.adminBlockSpinner:
                selectedBlock = LanguageTranslationHelper.blockHindiToEnglish(adapterViewSelectedItem);
                break;
            case R.id.adminVillageSpinner:
                selectedVillage = LanguageTranslationHelper.villageHindiToEnglish(adapterViewSelectedItem);
                break;
            case R.id.adminSpeciesSpinner:
                selectedSpecies = LanguageTranslationHelper.speciesHindiToEnglish(adapterViewSelectedItem);
                break;
            case R.id.adminStatusSpinner:
                selectedStatus = LanguageTranslationHelper.statusHindiToEnglish(adapterViewSelectedItem);
                break;
        }
    }

    private void setTreeMarker() {
        mGoogleMap.clear();
        ArrayList<Tree> treeList = viewModel.getTreeList();
        Log.e("FilteredScreen", String.valueOf(treeList));
        for (Tree tree : treeList) {
            double latitude = tree.latitude;
            double longitude = tree.longitude;
            LatLng treeMarker = new LatLng(latitude, longitude);
            Log.e("ScreenFilter", String.valueOf(treeMarker));
            mGoogleMap.addMarker(new MarkerOptions().position(treeMarker));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(treeMarker));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
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

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
            datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            Log.e("CurrentDate", String.valueOf(System.currentTimeMillis()));
            long millis = 0;
            if (flag == 0) {
                try {
                    millis = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(startSelectedDate)).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("startSelectedDate", String.valueOf(millis));
                datePicker.getDatePicker().setMinDate(millis);
            }
            return datePicker;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen
            if (flag == 1) {
                if (month < 10 && day < 10) {
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append("0").append(day);
                }
                if (day < 10 && month > 10) {
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append("0").append(day);
                }
                if (month < 10 && day > 10) {
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append(day);
                }
                if (month > 10 && day > 10) {
                    startSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append(day);
                }
//                startDate=String.valueOf(startSelectedDate);
//                Log.e("startSelectedDate", String.valueOf(startDate))
                adminStartDateTextView.setText(String.valueOf(new StringBuilder().append(day).append("/")
                        .append(month).append("/").append(year)));
            }
            if (flag == 0) {
//                Log.e("startSelectedDate", String.valueOf(startDate));
                if (month < 10 && day < 10) {
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append("0").append(day);
                }
                if (day < 10 && month > 10) {
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append("0").append(day);
                }
                if (month < 10 && day > 10) {
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append("0").append(month + 1).append("-").append(day);
                }
                if (month > 10 && day > 10) {
                    endSelectedDate = new StringBuilder().append(year).append("-")
                            .append(month + 1).append("-").append(day);
                }
                adminEndDateTextView.setText(String.valueOf(new StringBuilder().append(day).append("/")
                        .append(month).append("/").append(year)));
            }

        }

    }


}

