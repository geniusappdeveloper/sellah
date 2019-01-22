package com.app.admin.sellah.view.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CheckOutModel;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.adapter.CheckoutProductAdapter;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class MakeOfferDialog extends AlertDialog {

    OfferController controller;
    Context context;
    @BindView(R.id.rec_product)
    RecyclerView recProduct;
    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    @BindView(R.id.txt_send_offer)
    Button txtSendOffer;
    @BindView(R.id.txt_status_canceled)
    TextView txtStatusCanceled;
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.rl_list)
    RelativeLayout rlList;
    @BindView(R.id.li_sendOffer)
    LinearLayout liSendOffer;
    private WebService webService;
    private Dialog dialog;
    private ArrayList<CheckOutModel> recordList;
    private CheckoutProductAdapter checkoutProductAdapter;
    private String itemQuantity;
    String otherUserId;
    MakeOfferModel makeOfferModel;
    String productName;
    String productId;


    protected MakeOfferDialog(Context context, OfferController controller, String otherUserId) {
        super(context);
        this.controller = controller;
        this.context = context;
        this.otherUserId = otherUserId;
    }

    public static MakeOfferDialog create(Context context, OfferController controller, String otherUserId) {
        return new MakeOfferDialog(context, controller, otherUserId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_order_dialog);
        webService = Global.WebServiceConstants.getRetrofitinstance();
        dialog = S_Dialogs.getLoadingDialog(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        getForsaleList(otherUserId);

    }

    private void checkOutProductList(GetProductList body) {


        pbLoading.setVisibility(View.GONE);
        recProduct.setVisibility(View.VISIBLE);
        card1.setVisibility(View.VISIBLE);
        checkoutProductAdapter = new CheckoutProductAdapter(body.getResult(), context, new CheckoutProductAdapter.ActionCallback() {
            @Override
            public void onCheckclicked(String name, String id, String subtotal, String quantity) {
                txtSubtotal.setText("S$ " + subtotal);
                itemQuantity = quantity;
                productId = id;
                productName = name;
            }

            @Override
            public void onMakeOffer(MakeOfferModel body, String name) {
//                makeOfferModel=body;
//                productName=name;
                controller.onMakeOffer(body, name);
                dismiss();
            }
        });

        LinearLayoutManager birthHorizontalManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recProduct.setLayoutManager(birthHorizontalManager);
        recProduct.setAdapter(checkoutProductAdapter);
    }

    @OnClick(R.id.txt_send_offer)
    public void onViewClicked() {

        if (txtSubtotal.getText().toString().equalsIgnoreCase("S$ 0")) {
            controller.onErrorSelection();
        } else {
            makeOfferApi(productId, txtSubtotal.getText().toString().replace("S$", ""), productName, "1");
        }

    }

    public interface OfferController {
        void onMakeOfferButtonClick(MakeOfferModel offerPrice, String itemQuantity);

        void onMakeOffer(MakeOfferModel body, String productName);

        void onErrorSelection();

    }

    private void getForsaleList(String otherUserId) {
        dialog.show();
        Call<GetProductList> recordsCall = webService.getForSalelistApi(otherUserId);
        recordsCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Log.e("ForSaleData", response.body().getResult().toString());
                        checkOutProductList(response.body());
                    }
                } else {
                    dismiss();
                    Toast.makeText(context, "No product available to show.", Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("ForSaleData", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                dismiss();
                Toast.makeText(context, "Something went's wrong.", Toast.LENGTH_SHORT).show();
                Log.e("ForSaleData", "failure" + t.getMessage());
            }
        });
    }

    private void makeOfferApi(String productId, String price, String name, String tt) {
        Dialog dialog1 = S_Dialogs.getLoadingDialog(context);
        dialog1.show();
        Call<MakeOfferModel> makeOfferCall = Global.WebServiceConstants.getRetrofitinstance().makeOfferApi(HelperPreferences.get(context).getString(UID), productId, otherUserId, price, "P", name, itemQuantity);
        makeOfferCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        controller.onMakeOfferButtonClick(response.body(), productName);
                        dismiss();

                    }
                    if (dialog1 != null && dialog1.isShowing()) {
                        dialog1.dismiss();
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
