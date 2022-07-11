package com.haritbrij.haritBrij;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.haritbrij.haritBrij.models.Organisation;
import com.haritbrij.haritBrij.models.Tree;

import java.util.ArrayList;
import java.util.List;

public class OrgListAdapter extends RecyclerView.Adapter<OrgListAdapter.ViewHolder> {
    private ArrayList<Organisation> orgList;
    private static ItemClickListener clickListener;
    public OrgListAdapter(List<Organisation> orgList) {
        this.orgList = (ArrayList<Organisation>) orgList;
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView orgNameTextView;
        public TextView treesPlantedTextView;
        public TextView organisationId;
        public ConstraintLayout orgItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orgNameTextView = itemView.findViewById(R.id.organisationNameTextView);
            treesPlantedTextView = itemView.findViewById(R.id.treesPlantedOrganisation);
            organisationId = itemView.findViewById(R.id.organisationId);
            orgItemLayout = itemView.findViewById(R.id.orgListItemLayout);
            orgItemLayout.setOnClickListener(this);
        }
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public OrgListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orglistitem, parent, false);

        return new OrgListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrgListAdapter.ViewHolder holder, int position) {
        holder.organisationId.setText(orgList.get(position).id);
        holder.orgNameTextView.setText(orgList.get(position).name);
        holder.treesPlantedTextView.setText(orgList.get(position).target);
    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }
    public void setOnItemClickListener(OrgListAdapter.ItemClickListener itemClickListener) {
        OrgListAdapter.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void filterList(ArrayList<Organisation> filteredList) {
        orgList = filteredList;
        notifyDataSetChanged();
    }
}
