package com.app.admin.sellah.view.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.PromotePackages.PackagesList;
import com.app.admin.sellah.model.extra.PromotePackages.PromotePackageModel;
import com.app.admin.sellah.view.adapter.PromoteOfferAdapter;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.stripe.android.view.CardMultilineWidget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class AddProductPromoteDialog extends AlertDialog {

    @BindView(R.id.img_tick_1)
    ImageView imgTick1;
    @BindView(R.id.rv_offer_list)
    RecyclerView rvOfferList;

    Context context;
    Button btnProceed;

    String CardNumber, Cvc, Currency;
    Integer ExpMonth, ExpYear;
    public static final String PUBLISHABLE_KEY = "pk_test_3eq8eJ4CcA0kgn0JN9AG0fHQ";

    String cardNumberStr;

    CardMultilineWidget cardMultilineWidget;
    CreditCardView creditCardView;
    EditText edtCvv;
    Button btnCancel;
    @BindView(R.id.pb_root)
    ProgressBar pbRoot;
    @BindView(R.id.sv_root)
    ScrollView svRoot;
    @BindView(R.id.cd_root)
    CardView cdRoot;
    private BottomSheetDialog bottomSheetDialog;
    WebService webService;
    private Dialog dialog;
    private boolean isCardBack;
    private LinearLayout liCardDetail;
    private LinearLayout liCardDetailError;
    private ProgressBar pbLoading;
    private ViewGroup transitionsContainer;
    PromoteCallback callback;
    String productId = "";
    List<PackagesList> packagesLists;
    public AddProductPromoteDialog(Context context, PromoteCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.productId = productId;
        packagesLists=new ArrayList<>();
    }

    public static AddProductPromoteDialog create(Context context, PromoteCallback callback) {
        return new AddProductPromoteDialog(context, callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webService = Global.WebServiceConstants.getRetrofitinstance();
        dialog = S_Dialogs.getLoadingDialog(context);
        setContentView(R.layout.layout_promote_product);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);

        PackagesList list= new PackagesList();
        list.setValidity("NA");
        list.setId("NA");
        list.setClicks("00");
        list.setAmount("00");
        packagesLists.add(list);
        getPromotePackages();


    }

    private void hideProgress(List<PackagesList> packagesList) {
     /*   new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {*/
                svRoot.setVisibility(View.VISIBLE);
                pbRoot.setVisibility(GONE);

                PromoteOfferAdapter adapter = new PromoteOfferAdapter(context, packagesList, (id) -> {
                    callback.onPackageSelected(id);
                    dismiss();
                });
                LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                rvOfferList.setLayoutManager(horizontalLayoutManager1);
                rvOfferList.setAdapter(adapter);
                Global.getTotalHeightofLinearRecyclerView(context, rvOfferList, R.layout.layout_promote_offer_list_design, 0);
       /*     }

        }, 1000);*/
    }

    public interface PromoteCallback {
        void onPackageSelected(String id);

//        void onPromoteFailure();
    }
    private void getPromotePackages() {
        Call<PromotePackageModel> promotePackageCall = webService.getPromotePackagesApi(HelperPreferences.get(context).getString(UID));
        promotePackageCall.enqueue(new Callback<PromotePackageModel>() {
            @Override
            public void onResponse(Call<PromotePackageModel> call, Response<PromotePackageModel> response) {
                if (response.isSuccessful()) {
                    Log.e("PromoteProductApi", "onResponse: success" + response.body().getStatus());
                  if(response.body().getStatus().equalsIgnoreCase("1")){
                      packagesLists.addAll(response.body().getPackagesList());
                      hideProgress(packagesLists);
                  }else{
                      Toast.makeText(context, "Unable to get promote packages at this movement.", Toast.LENGTH_SHORT).show();
                      dismiss();
                  }
                } else {
                    Toast.makeText(context, "Unable to get promote packages at this movement.", Toast.LENGTH_SHORT).show();
                    dismiss();
                    try {
                        Log.e("PromoteProductApi_error", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PromotePackageModel> call, Throwable t) {
                Toast.makeText(context, "Something went's wrong.", Toast.LENGTH_SHORT).show();
                dismiss();
                Log.e("PromoteProductApi", "onFailure: " + t.getMessage());
            }
        });
    }
}
