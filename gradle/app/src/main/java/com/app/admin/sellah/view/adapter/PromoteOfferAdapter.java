package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.PromotePackages.PackagesList;

import java.util.List;


public class PromoteOfferAdapter extends RecyclerView.Adapter<PromoteOfferAdapter.ViewHolder> {

    LayoutInflater mInflater;
    Context context;
    View view;
    List<PackagesList> packagesList;
    OfferCallBack callBack;

    public PromoteOfferAdapter(Context context, List<PackagesList> packagesList, OfferCallBack callBack) {
       mInflater=LayoutInflater.from(context);
       this.context=context;
       this.packagesList=packagesList;
       this.callBack=callBack;
    }


    @NonNull
    @Override
    public PromoteOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = mInflater.inflate(R.layout.layout_promote_offer_list_design, parent, false);
        return new PromoteOfferAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PromoteOfferAdapter.ViewHolder holder, int position) {

        holder.txtOffer.setText("S$ "+packagesList.get(position).getAmount());
        holder.txtOfferPrice.setText(packagesList.get(position).getClicks()+" clicks");
        holder.txtDuration.setText(packagesList.get(position).getValidity()+" days");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onOfferSelect(packagesList.get(position).getId());
            }
        });
        if (position==packagesList.size()-1){
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
            holder.txtOffer.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.txtOfferPrice.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.txtDuration.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
    }



    @Override
    public int getItemCount() {
//        if (saleList != null) {
        return packagesList.size();
//        } else {
//            return 0;
//        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtOffer,txtOfferPrice,txtDuration;
        public ViewHolder(View v) {
            super(v);
            cardView=view.findViewById(R.id.cd_root);
            txtOffer=view.findViewById(R.id.txt_offer);
            txtOfferPrice=view.findViewById(R.id.txt_offer_price);
            txtDuration=view.findViewById(R.id.txt_offer_duration);
        }
    }

    public interface OfferCallBack{
        void onOfferSelect(String id);
    }

}
