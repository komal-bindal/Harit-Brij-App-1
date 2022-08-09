package com.haritbrij.haritBrij;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchTreeFragment extends Fragment {
    UserMainViewModel viewModel;
    ArrayList<Tree> mData = new ArrayList<>();
    TreeListAdapter mTreeListAdapter;
//    TextView treesPlantedTextView;

    public SearchTreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_tree, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(UserMainViewModel.class);

        EditText searchTreeEditText = view.findViewById(R.id.searchTreeByUtid);
        ImageView searchTreeImageView = view.findViewById(R.id.searchTreeIcon);
//        treesPlantedTextView = ((UserMainActivity) getContext()).findViewById(R.id.registeredTreesTextView);

        RecyclerView searchRecyclerView = view.findViewById(R.id.search_tree_recycler_view);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mTreeListAdapter = new TreeListAdapter(mData);


        String baseUrl = VolleySingleton.getBaseUrl();

        //TODO: Commenting the below line for now. The api is not returning the correct trees according to the user id.
        String myUrl = baseUrl + "readusertree.php/?uid=" + viewModel.sharedPreferences.getString("uid", "0");
//        String myUrl = baseUrl + "getalltree.php";
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try {
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        JSONArray jsonArray = myJsonObject.getJSONArray("body");

                        mData.clear();
                        //save the from response in new tree object
                        for (int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {

                            JSONObject indexedTree = jsonArray.getJSONObject(jsonArrayIndex);
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
                            Log.d("TreeDetails", tree.latitude + " "+ tree.longitude);
                            mData.add(tree);
                        }

                        viewModel.setTreeList(mData);

                        mTreeListAdapter = new TreeListAdapter(mData);
                        searchRecyclerView.setAdapter(mTreeListAdapter);
//                        treesPlantedTextView.setText(myJsonObject.getString("itemCount"));

                        Toast.makeText(getContext(), "Number of Trees - " + myJsonObject.getString("itemCount"), Toast.LENGTH_SHORT).show();
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
                viewModel.setUtid(mData.get(position).id);
                viewModel.setTree(mData.get(position));
                TreeProfileFragment treeProfileFragment = new TreeProfileFragment();
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_user_fragment_container_view, treeProfileFragment).addToBackStack(null).commit();
            }
        });

        searchTreeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(getClass().getSimpleName(), "onClick: ");
                String enteredUtid = searchTreeEditText.getText().toString();
                mData = viewModel.getTreeList();
                for (Tree tree : mData) {
                    if (enteredUtid.equals(tree.id)) {
                        mData.clear();
                        mData.add(tree);
                        break;
                    }
                }
                mTreeListAdapter.filterList(mData);
            }
        });
    }
}