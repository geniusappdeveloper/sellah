package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.controller.utils.Global;

import static com.app.admin.sellah.controller.utils.Global.clearFocus;
import static com.app.admin.sellah.controller.utils.Global.convertUTCToLocal;
import static com.app.admin.sellah.controller.utils.Global.getTimeAgo;

public class WishRecordAdapter  extends RecyclerView.Adapter<WishRecordAdapter.WishHolder> {
    private GetProductList recordList;
    Context context;

    public WishRecordAdapter(GetProductList sublist, Context context) {
        this.recordList = sublist;
        this.context = context;
    }

    @Override
    public WishRecordAdapter.WishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_birth_adapter, parent, false);
        WishRecordAdapter.WishHolder gvh = new WishRecordAdapter.WishHolder(view);
        return gvh;
    }



    @Override
    public void onBindViewHolder(WishRecordAdapter.WishHolder holder, final int position) {
      /*  RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);*/
        String imageUrl = "";
        if (recordList.getResult().get(position).getProductImages() != null) {
            imageUrl = !recordList.getResult().get(position).getProductImages().isEmpty() ? recordList.getResult().get(position).getProductImages().get(0).getImage() : "";
            imageUrl = imageUrl.replace("productimages","thproductimages");
        }
        RequestOptions requestOptions=Global.getGlideOptions();
        Glide.with(context)
                .load(imageUrl).apply(requestOptions)
                .into(holder.profileImg);
        holder.BirthText.setText(recordList.getResult().get(position).getName());
        holder.costText.setText("S$ "+recordList.getResult().get(position).getPrice());
        holder.timeText.setText(getTimeAgo(convertUTCToLocal(recordList.getResult().get(position).getCreatedAt())));
        holder.remove.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return recordList.getResult().size();
    }

    public class WishHolder extends RecyclerView.ViewHolder {
        ImageView profileImg,messImg,shareImg;
        TextView BirthText,costText,timeText;
        ImageButton remove;


        public WishHolder(View view) {
            super(view);

            profileImg = view.findViewById(R.id.img_product);
            messImg = view.findViewById(R.id.mess_image);
            shareImg = view.findViewById(R.id.share_image);
            BirthText = view.findViewById(R.id.text_birthday);
            costText = view.findViewById(R.id.cake_cost);
            timeText = view.findViewById(R.id.text_time);
            remove = view.findViewById(R.id.wish_remove_img);
        }



    }
}
