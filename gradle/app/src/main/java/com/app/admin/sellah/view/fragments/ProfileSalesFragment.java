package com.app.admin.sellah.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.SaleModelClass;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.SalesAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ProfileSalesFragment extends Fragment {


    //SalesRecycler
    ArrayList<SaleModelClass> saleList = new ArrayList<>();
    RecyclerView saleRecycler;
    SalesAdapter salesAdapter;

    View view;
    WebService service;
    GetProductList forSaleProducts;

    SalesAdapter.TabTextController controller;
    @BindView(R.id.img_no_data)
    ImageView imgNoData;
    Unbinder unbinder;
    @BindView(R.id.txt_heading)
    TextView txtHeading;

    public void onAttachToParentFragment(Fragment fragment) {
        try {
            controller = (SalesAdapter.TabTextController) fragment;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sales_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        saleRecycler = view.findViewById(R.id.salesRecycler);
        service = Global.WebServiceConstants.getRetrofitinstance();
        getSaledata();
//        saleList();
        return view;
    }

    private void setSalesData(GetProductList list) {
        salesAdapter = new SalesAdapter(list, getActivity(), true, (productid, pos) -> {
            S_Dialogs.getConfirmation(getActivity(), getActivity().getResources().getString(R.string.dialog_title_mark_as_sold), ((dialog1, which) -> {
                removeSaledata(productid, pos);
            })).show();
        }, (productid, pos) -> {
            S_Dialogs.getConfirmation(getActivity(), getActivity().getResources().getString(R.string.dialog_title_delete_product), ((dialog1, which) -> {
                deleteSaledata(productid, pos);
            })).show();
        }, null);
        LinearLayoutManager horizontalLayoutManager = new GridLayoutManager(getActivity(), 2);
        saleRecycler.setLayoutManager(horizontalLayoutManager);
        saleRecycler.setAdapter(salesAdapter);
        controller.tabTextController(forSaleProducts != null ? forSaleProducts.getResult().size() : 0);
        if (!(forSaleProducts.getResult().size() > 0)) {
            showErrorMsg();
        } else {
            hideErrorMsg();
        }
    }

    public int getitemCount() {
        if (forSaleProducts != null) {
            return forSaleProducts.getResult().size();
        } else {
            return 0;
        }
    }
    Call<GetProductList> recordsCall;
    private void getSaledata() {
//        Dialog dialog=S_Dialogs.getLoadingDialog(getActivity());
         recordsCall = service.getForSalelistApi(HelperPreferences.get(getActivity()).getString(UID));
        recordsCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        forSaleProducts = response.body();
                        setSalesData(forSaleProducts);

                    }
                } else {
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {

            }
        });
    }
    Call<Common> markAsSoldCall;
    private void removeSaledata(String productId, int pos) {
//        Dialog dialog=S_Dialogs.getLoadingDialog(getActivity());
         markAsSoldCall = service.markProductAsSoldApi(HelperPreferences.get(getActivity()).getString(UID), productId);
        markAsSoldCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
//                        setSalesData(response.body());
                        forSaleProducts.getResult().remove(pos);
                        salesAdapter.notifyItemRemoved(pos);
                        salesAdapter.notifyItemRangeChanged(pos, forSaleProducts.getResult().size());
                        controller.tabTextController(forSaleProducts != null ? forSaleProducts.getResult().size() : 0);
                        if (!(forSaleProducts.getResult().size() > 0)) {
                            showErrorMsg();
                        } else {
                            hideErrorMsg();
                        }
                    }
                } else {
//                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {

            }
        });
    }

    private void deleteSaledata(String productId, int pos) {
//        Dialog dialog=S_Dialogs.getLoadingDialog(getActivity());
        Call<Common> deleteItemCall = service.deleteProductApi(HelperPreferences.get(getActivity()).getString(UID), productId);
        deleteItemCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
//                        setSalesData(response.body());
                        forSaleProducts.getResult().remove(pos);
                        salesAdapter.notifyItemRemoved(pos);
                        salesAdapter.notifyItemRangeChanged(pos, forSaleProducts.getResult().size());
                        controller.tabTextController(forSaleProducts != null ? forSaleProducts.getResult().size() : 0);
                        if (!(forSaleProducts.getResult().size() > 0)) {
                            showErrorMsg();
                        } else {
                            hideErrorMsg();
                        }
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {

            }
        });
    }

    public void showErrorMsg() {
        if (view != null) {
            txtHeading.setVisibility(View.GONE);
            imgNoData.setVisibility(View.VISIBLE);
        }
    }

    public void hideErrorMsg() {
        if (view != null) {
            txtHeading.setVisibility(View.VISIBLE);
            imgNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recordsCall!=null)
        {recordsCall.cancel();}
        if (markAsSoldCall!=null)
        {markAsSoldCall.cancel();}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
