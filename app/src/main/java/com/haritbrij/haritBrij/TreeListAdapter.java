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
import androidx.recyclerview.widget.RecyclerView;

import com.haritbrij.haritBrij.models.Tree;

import java.util.ArrayList;
import java.util.List;

public class TreeListAdapter extends RecyclerView.Adapter<TreeListAdapter.ViewHolder> {
    private ArrayList<Tree> treesList;
    public TreeListAdapter(List<Tree>treeList) {
        treesList = (ArrayList<Tree>) treeList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView treeIdTextView;
        public ImageView treeImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            treeIdTextView = itemView.findViewById(R.id.tree_id_item);
            treeImageView = itemView.findViewById(R.id.tree_image_item);
        }
    }

    @NonNull
    @Override
    public TreeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treelistitem, parent, false);

        return new TreeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeListAdapter.ViewHolder holder, int position) {
        holder.treeIdTextView.setText(treesList.get(position).id);

        byte[] decodedString = Base64.decode(treesList.get(position).image1, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.treeImageView.setImageBitmap(decodedByte);
    }

    @Override
    public int getItemCount() {
        return treesList.size();
    }
}