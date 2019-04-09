package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.activities.QrCodeActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class PayDialog_QRScan extends Dialog {
    String availBal;
    Context context;
    @BindView(R.id.edt_availBal)
    TextView edtAvailBal;
    @BindView(R.id.edt_offered_price)
    EditText edtOfferedPrice;


    public PayDialog_QRScan(Context con, String availBal) {
        super(con);
        this.context = con;
        this.availBal = availBal;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay_dialog_qrscan);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);

        edtAvailBal.setText("S$"+availBal);




    }


    @OnClick({R.id.btn_cancel_qrscan, R.id.btn_pay_qrScan,R.id.qr_code_black})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_qrscan:
                dismiss();
                break;
            case R.id.btn_pay_qrScan:

                if (edtOfferedPrice.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please enter offered price", Toast.LENGTH_SHORT).show();
                } else {

                    float avail = Float.parseFloat(availBal);
                    float offered = Float.parseFloat(edtOfferedPrice.getText().toString().trim());
                    if (avail >= offered) {
                        dismiss();
                    } else {
                        Toast.makeText(context, "Insufficient balance", Toast.LENGTH_SHORT).show();
                    }

                }
                break;


            case R.id.qr_code_black:
                context.startActivity(new Intent(context,QrCodeActivity.class));

                break;
        }
    }




}
