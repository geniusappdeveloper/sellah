package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyerMakeOfferDialog extends Dialog {

    Context context;
    @BindView(R.id.txt_original_price)
    TextView txtOriginalPrice;
    @BindView(R.id.edt_add_price)
    EditText edtAddPrice;
    @BindView(R.id.il_add_price)
    NoChangingBackgroundTextInputLayout ilAddPrice;
    @BindView(R.id.edt_add_quantity)
    EditText edtAddQuantity;
    @BindView(R.id.il_add_quantity)
    NoChangingBackgroundTextInputLayout ilAddQuantity;
    @BindView(R.id.btn_makeOffer)
    Button btnMakeOffer;
    @BindView(R.id.txt_total_stroke)
    TextView txtTotalStroke;
    private WebService webService;
    private Dialog dialog;

    private String productQuantity = "";
    private String productPrice = "";
    OnmakeOfferClick callback;

    protected BuyerMakeOfferDialog(Context context, String productPrice, String productQuantity, OnmakeOfferClick callback) {
        super(context);
        this.context = context;
        Log.e( "onClick: ", productPrice);
        Log.e( "onClick: ", productQuantity);
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.callback = callback;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        webService = Global.WebServiceConstants.getRetrofitinstance();
//        dialog = S_Dialogs.getLoadingDialog(context);
        setContentView(R.layout.buyer_offer_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);

        // new changes by me
        //setUpViews();
        steTextWacher();

    }

    private void setUpViews() {
        btnMakeOffer.setAlpha(0.5f);
        btnMakeOffer.setClickable(false);
        ilAddQuantity.setError("Available products in stock : " + (Integer.parseInt(productQuantity) - 1));

    }

    private void steTextWacher() {
//        ilAddQuantity.setError(productQuantity);
        txtTotalStroke.setText(String.valueOf((Integer.parseInt(productQuantity) - 1)));
        txtOriginalPrice.setText("$"+productPrice);
        edtAddPrice.setText("$"+productPrice);
        ilAddPrice.setError("");

        edtAddPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ilAddPrice.setError("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ilAddPrice.setErrorEnabled(false);
                    btnMakeOffer.setAlpha(1f);
                    btnMakeOffer.setClickable(true);
                }/*else{
                    ilAddPrice.setError("");
                }*/
                String prefix = "S$";
//                edtPrice.setText("S$" + edtPrice.getText().toString());
                if (!s.toString().startsWith("S$")) {
                    String cleanString;
                    String deletedPrefix = prefix.substring(0, prefix.length() - 1);
                    if (s.toString().startsWith(deletedPrefix)) {
                        cleanString = s.toString().replaceAll(deletedPrefix, "");
                    } else {
                        cleanString = s.toString().replaceAll(prefix, "");
                    }
                    edtAddPrice.setText(prefix + cleanString);
                    edtAddPrice.setSelection(Global.getText(edtAddPrice).length());
                  /*  edtPrice.setText("S$");
                    Selection.setSelection(edtPrice.getText(), edtPrice
                            .getText().length());*/
                } else {
                   /* if(s.toString().equalsIgnoreCase("S$")){
                            edtPrice.setText("");
                    }*/
                }
            }
        });
        edtAddQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                ilAddQuantity.setError("Available products in stock : "+productQuantity);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    String string = s.toString();
                    int remainingitems = Integer.parseInt(productQuantity) - Integer.parseInt(string);
                    if (Integer.parseInt(string) <= Integer.parseInt(productQuantity)) {
                        ilAddQuantity.setError("Available products in stock : " + remainingitems);
                        btnMakeOffer.setClickable(true);
                        btnMakeOffer.setAlpha(1f);
                    } else {
                        ilAddQuantity.setError("Entered quantity is not available in stock");
                        btnMakeOffer.setClickable(true);
                        btnMakeOffer.setAlpha(0.5f);
                    }

                } else {
                    btnMakeOffer.setClickable(false);
                    btnMakeOffer.setAlpha(0.5f);
                    ilAddQuantity.setError("Available products in stock : " + productQuantity);
                }

            }
        });
    }

    public static BuyerMakeOfferDialog create(Context context, String productPrice, String productQuantity, OnmakeOfferClick callback) {
        return new BuyerMakeOfferDialog(context, productPrice, productQuantity, callback);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_makeOffer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_makeOffer:
                if (Global.getText(edtAddPrice).equalsIgnoreCase("")) {
                    ilAddPrice.setError("Please enter offering price");
                } else if (Global.getText(edtAddPrice).equalsIgnoreCase("")) {
                    ilAddQuantity.setError("Please add product quantity.");
                } else {
                    callback.onMakeOfferClick(Global.getText(edtAddPrice).replace("S$", ""), Global.getText(edtAddQuantity), BuyerMakeOfferDialog.this);
                }
                break;
        }
    }

    public interface OnmakeOfferClick {
        void onMakeOfferClick(String price, String quantity, BuyerMakeOfferDialog buyerMakeOfferDialog);
    }
}
