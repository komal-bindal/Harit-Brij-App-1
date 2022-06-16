package com.haritbrij.haritBrij;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haritbrij.haritBrij.R;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchTreeFragment extends Fragment {
    UserMainViewModel viewModel;

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

        RecyclerView searchRecyclerView = view.findViewById(R.id.search_tree_recycler_view);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        String baseUrl = VolleySingleton.getBaseUrl();
        String myUrl = baseUrl + "getalltree.php/";
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try{
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        JSONArray jsonArray = myJsonObject.getJSONArray("body");
                        List<Tree> treeList = new ArrayList<>();

                        //save the from response in new tree object
                        for(int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
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
                            treeList.add(tree);
                        }

                        viewModel.setTreeList(treeList);

                        searchRecyclerView.setAdapter(new TreeListAdapter(treeList));

                        Toast.makeText(getContext(), "Number of Trees - " + myJsonObject.getString("itemCount"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Log.e(getClass().getSimpleName(), volleyError.getMessage())
        );

        VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);
    }
}