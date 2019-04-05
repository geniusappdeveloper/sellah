package com.app.admin.sellah.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CardDetails.Card;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cooltechworks.creditcarddesign.CreditCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class Pay_offer_adapter extends RecyclerView.Adapter<Pay_offer_adapter.Payooferholder> {

    Context context;
    ArrayList<Map<String,String>> list;
   OnCardOptionSelection cardOptionSelection;
   String cross_status="";


    public Pay_offer_adapter(Context activity,  ArrayList<Map<String,String>> list ,OnCardOptionSelection cardOptionSelection,String cross_status) {
        context = activity;
        this.list = list;
        this.cardOptionSelection = cardOptionSelection;
        this.cross_status = cross_status;

    }

    public Pay_offer_adapter(Context activity,  ArrayList<Map<String,String>> list ,OnCardOptionSelection cardOptionSelection) {
        context = activity;
        this.list = list;
        this.cardOptionSelection = cardOptionSelection;

    }

    @Override
    public Pay_offer_adapter.Payooferholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_adapter_view, parent, false);
        Payooferholder cvh = new Payooferholder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull Payooferholder holder, int position) {

        holder.name.setText(list.get(position).get("product_name"));
        holder.price.setText(list.get(position).get("price_cost"));
        holder.qty.setText(list.get(position).get("quantity"));
        Glide.with(context)
                .load(!list.get(position).get("product_image").isEmpty() ? list.get(position).get("product_image") : "")
                .apply(Global.getGlideOptions())
                .into(holder.offerimage);


        if (list.get(position).get("order_status").equalsIgnoreCase("A")) {
            holder.cancel_offer.setVisibility(View.GONE);
        }
        else
        {holder.cancel_offer.setVisibility(View.VISIBLE);}

        if (cross_status.isEmpty())
        {
            holder.cancel_offer.setVisibility(View.GONE);

        }
        else
        {
            if (list.get(position).get("order_status").equalsIgnoreCase("A")) {
                holder.cancel_offer.setVisibility(View.VISIBLE);
            }
            else
            {holder.cancel_offer.setVisibility(View.GONE);}


        }

        holder.cancel_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardOptionSelection.removeoffer(holder.getAdapterPosition());

            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return list.size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Payooferholder extends RecyclerView.ViewHolder {
        TextView name,price,qty,paid_status;
        ImageView offerimage,cancel_offer;

        public Payooferholder(View view) {
            super(view);
            name = view.findViewById(R.id.pay_txt_item_name);
            price = view.findViewById(R.id.pay_txt_item_cost);
            qty = view.findViewById(R.id.pay_txt_item_quantity);
            offerimage = view.findViewById(R.id.pay_offer_image);
            cancel_offer = view.findViewById(R.id.cancel_offer);
            paid_status = view.findViewById(R.id.paid_or_not);


        }
    }

    public interface OnCardOptionSelection {
        void removeoffer(int pos);


    }



}