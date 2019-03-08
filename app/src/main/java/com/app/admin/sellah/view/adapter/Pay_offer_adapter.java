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
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CardDetails.Card;
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


    public Pay_offer_adapter(Context activity,  ArrayList<Map<String,String>> list) {
        context = activity;
        this.list = list;

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
        TextView name,price,qty;
        ImageView offerimage;

        public Payooferholder(View view) {
            super(view);
            name = view.findViewById(R.id.pay_txt_item_name);
            price = view.findViewById(R.id.pay_txt_item_cost);
            qty = view.findViewById(R.id.pay_txt_item_quantity);
            offerimage = view.findViewById(R.id.pay_offer_image);


        }
    }

    public interface OnCardOptionSelection {
        void onCardSelectionClick(int pos, String cardId);
        void onCardRemoveListner(int pos, int updatedSize);
    }

}