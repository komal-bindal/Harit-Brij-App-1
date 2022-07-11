package com.haritbrij.haritBrij;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class TreeListAdapter extends RecyclerView.Adapter<TreeListAdapter.ViewHolder> {
    private ArrayList<Tree> treesList;
    private static ItemClickListener clickListener;

    public TreeListAdapter(List<Tree>treeList) {
        treesList = (ArrayList<Tree>) treeList;
        Log.d(getClass().getSimpleName(), String.valueOf(treeList.size()));
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView treeIdTextView;
        public ImageView treeImageView;
        public ConstraintLayout treeItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            treeIdTextView = itemView.findViewById(R.id.tree_id_item);
            treeImageView = itemView.findViewById(R.id.tree_image_item);
            treeItemLayout = itemView.findViewById(R.id.treeListItemLayout);
            treeItemLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition());
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

        Bitmap decodedByte = ImageHelper.decodeImage(treesList.get(position).image1);

        holder.treeImageView.setImageBitmap(getCroppedBitmap(decodedByte));

//        holder.treeItemLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                TreeProfileFragment treeProfileFragment = new TreeProfileFragment();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return treesList.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        TreeListAdapter.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void filterList(ArrayList<Tree> filteredList) {
        treesList = filteredList;
        notifyDataSetChanged();
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}