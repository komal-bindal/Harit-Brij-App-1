package com.haritbrij.haritBrij;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchTreeFragment extends Fragment {

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

        String myUrl = "http://172.16.78.31/api/getalltree.php/";
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                response -> {
                    try{
                        //Create a JSON object containing information from the API.
                        JSONObject myJsonObject = new JSONObject(response);
                        JSONArray jsonArray = myJsonObject.getJSONArray("body");

                        //save the from response in new tree object
                        Tree tree = new Tree();
//                        totalCasesWorld.setText(myJsonObject.getString("cases"));
//                        totalRecoveredWorld.setText(myJsonObject.getString("recovered"));
//                        totalDeathsWorld.setText(myJsonObject.getString("deaths"));

                        Toast.makeText(getContext(), myJsonObject.getString("itemCount"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(myRequest);
    }
}