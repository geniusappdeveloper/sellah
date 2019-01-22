package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.SellProductInterface;

import java.io.File;
import java.util.List;

public class Add_Product_Cars_Adapter  extends RecyclerView.Adapter<Add_Product_Cars_Adapter.AddProductViewHolder> {
    private List<String> add_list;
    Context context;
    SellProductInterface sellProductInterface;


    public Add_Product_Cars_Adapter(FragmentActivity activity, List<String> imageList, SellProductInterface sellProductInterface) {

        this.add_list = imageList;
        context = activity;
        this.sellProductInterface=sellProductInterface;
    }

    @Override
    public Add_Product_Cars_Adapter.AddProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_car_adapter, parent, false);
//        Add_Product_Cars_Adapter.AddProductViewHolder cvh = new Add_Product_Cars_Adapter.AddProductViewHolder(groceryProductView);
        return new AddProductViewHolder(groceryProductView);
    }

    @Override
    public void onBindViewHolder(final Add_Product_Cars_Adapter.AddProductViewHolder holder, final int position) {
        File f = new File(getRealPathFromURI(Uri.parse(add_list.get(position))));
        Drawable d = Drawable.createFromPath(f.getAbsolutePath());
        holder.add_car.setBackground(d);
        if(position==0){
            holder.addCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }else{
            holder.addCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }
        if(add_list.size()!=8){
            sellProductInterface.setImageCaptureVisiblty(true);
        }else{
            sellProductInterface.setImageCaptureVisiblty(false);
        }
        Log.e("items",""+add_list.get(position));
        holder.crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, add_list.size());
//                Toast.makeText(context,"item removed", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return add_list.size();
    }

    public class AddProductViewHolder extends RecyclerView.ViewHolder {
        ImageView crossImage,add_car;
        CardView addCardView;
        public AddProductViewHolder(View view) {
            super(view);
            crossImage = view.findViewById(R.id.crossImg);
            add_car = view.findViewById(R.id.add_car_Img);
            addCardView = view.findViewById(R.id.add_car_photo);

        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
