package com.app.admin.sellah.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.ChatActivityController;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.view.CustomDialogs.BuyerMakeOfferDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.bumptech.glide.Glide;

import java.lang.reflect.Executable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.CheckOutItemHolder> {

    private List<Result> recordList;
    Context context;
    boolean[] isCheckList;
    ChatActivityController controller;
    //    Dialog dialog;
    long subTotal = 0;
    WebService service;
    ActionCallback actionCallback;
    Button txtsend_offer;
    int pos=-1;
    boolean check;
    String price,qty;

    public CheckoutProductAdapter(List<Result> itemList, Context context,Button txt_sendoffer, ActionCallback checkSelection) {
        this.recordList = itemList;
        this.context = context;
        this.txtsend_offer =txt_sendoffer;
        isCheckList = new boolean[itemList.size()];
        controller = new ChatActivity();
//        this.dialog = dialog;
        for (int i = 0; i < recordList.size(); i++) {
//            subTotal += Long.parseLong(recordList.get(i).getPrice());
            isCheckList[i] = false;
        }
        this.actionCallback = checkSelection;
        service = Global.WebServiceConstants.getRetrofitinstance();
//        Log.e("isCheckList", "CheckoutProductAdapter: " +isCheckList);
    }

    @Override
    public CheckOutItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checkout_product, parent, false);
        CheckOutItemHolder gvh = new CheckOutItemHolder(view);
        return gvh;
    }


    @Override
    public void onBindViewHolder(CheckOutItemHolder holder, final int position) {
        holder.chkSelect.setChecked(isCheckList[position]);
//        if(dialog.){}
//        txtSubTotal.setText(subTotal);

        holder.txtProductName.setText(recordList.get(position).getName());
        holder.txtProductCost.setText("S$ " + recordList.get(position).getPrice());
        holder.qty.setText(recordList.get(position).getQuantity());
        Glide.with(context)
                .load(!recordList.get(position).getProductImages().isEmpty() ? recordList.get(position).getProductImages().get(0).getImage() : "")
                .apply(Global.getGlideOptions())
                .into(holder.imgProduct);

        holder.chkSelect.setTag(position);

        holder.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recordList.get(position).getQuantity().isEmpty() || Integer.parseInt(recordList.get(position).getQuantity())<=0)
                {
                    Toast.makeText(context, "Product is out of stock", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pos =position;
                    notifyDataSetChanged();
                }




            }
        });

        if (pos ==position)
        {
            actionCallback.onCheckclicked(recordList.get(pos).getName(), recordList.get(pos).getId(), recordList.get(position).getPrice(),recordList.get(position).getQuantity());

            holder.checkout.setBackgroundColor(Color.parseColor("#dbdbdb"));

            price =recordList.get(position).getPrice();
            qty =recordList.get(position).getQuantity();


        }
        else
        {
            holder.checkout.setBackgroundColor(Color.parseColor("#ffffff"));

        }

        txtsend_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.imgMakeOffer.performClick();

            }
        });

        holder.imgMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                BuyerMakeOfferDialog.create(context,price, qty, new BuyerMakeOfferDialog.OnmakeOfferClick() {
                    @Override
                    public void onMakeOfferClick(String price, String quantity, BuyerMakeOfferDialog buyerMakeOfferDialog) {
//                        makeOfferApi(position,recordList.get(position).getId(), price, recordList.get(position).getName(), buyerMakeOfferDialog);
                        makeOfferApi(position, price, recordList.get(position).getName(), quantity, buyerMakeOfferDialog);

                    }
                }).show();
               /* S_Dialogs.getMakeOfferDialog(context,recordList.get(position).getPrice(),(dialog,input)->{
                    if (TextUtils.isEmpty(input)||input.toString().equalsIgnoreCase("0")) {
                        Toast.makeText(context, "Please enter offering amount", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                    } else {

                        makeOfferApi(position,input.toString().trim(),recordList.get(position).getName(),dialog);
                    }
                }).show();*/
            }
        });

        holder.chkSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.chkSelect.getTag();
                try {
                    if (isCheckList[pos]) {
                        isCheckList[pos] = false;
                        subTotal = subTotal - Long.parseLong(recordList.get(pos).getPrice());
                    } else {

                        subTotal = subTotal + Long.parseLong(recordList.get(pos).getPrice());
                        isCheckList[pos] = true;
                    }
                    int quantity = 0;
                    for (int i = 0; i < isCheckList.length; i++) {
                        quantity += isCheckList[i] ? 1 : 0;
                    }
                    actionCallback.onCheckclicked(recordList.get(pos).getName(), recordList.get(pos).getId(), String.valueOf(subTotal), String.valueOf(quantity));
                } catch (Exception e) {

                }

//                txtSubTotal.setText(subTotal);
//                controller.updateSubTotal(String.valueOf(subTotal));
            }

        });

    }

    public interface ActionCallback {
        public void onCheckclicked(String name, String id, String subtotal, String quantity);

        public void onMakeOffer(MakeOfferModel body, String name);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class CheckOutItemHolder extends RecyclerView.ViewHolder {
        CheckBox chkSelect;
        TextView txtProductName, txtProductCost,qty;
        ImageView imgProduct, imgMakeOffer;
        RelativeLayout checkout;

        public CheckOutItemHolder(View view) {
            super(view);
            chkSelect = view.findViewById(R.id.chk_select);
            txtProductName = view.findViewById(R.id.txt_product_name);
            txtProductCost = view.findViewById(R.id.txt_product_cost);
            imgProduct = view.findViewById(R.id.img_product);
            imgMakeOffer = view.findViewById(R.id.img_btn_make_offer);
            checkout = view.findViewById(R.id.root_checkout);
            qty = view.findViewById(R.id.txt_product_qty);
        }
    }

    private void makeOfferApi(int pos, String price, String name, String quantity, BuyerMakeOfferDialog dialog) {
        Dialog dialog1 = S_Dialogs.getLoadingDialog(context);
        dialog1.show();
        Call<MakeOfferModel> makeOfferCall = service.makeOfferApi(HelperPreferences.get(context).getString(UID), recordList.get(pos).getId(), recordList.get(pos).getUserId(), price, "P", name, quantity);
        makeOfferCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (response.isSuccessful()) {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        dialog.dismiss();
                        actionCallback.onMakeOffer(response.body(), recordList.get(pos).getName());
                    }
                } else {
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                    Toast.makeText(context, "Unable to make an offer on this product.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeOfferModel> call, Throwable t) {
                if (dialog1 != null && dialog1.isShowing()) {
                    dialog1.dismiss();
                }
                Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("MakeOfferApi", "onFailure: " + t.getMessage());
            }
        });
    }

}
