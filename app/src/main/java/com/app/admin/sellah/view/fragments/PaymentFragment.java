package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.stripe.StripeApp;
import com.app.admin.sellah.controller.stripe.StripeButton;
import com.app.admin.sellah.controller.stripe.StripeConnectListener;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CardDetails.Card;
import com.app.admin.sellah.model.extra.CardDetails.CardDetailModel;
import com.app.admin.sellah.view.CustomAnimations.MyBounceInterpolator;
import com.app.admin.sellah.view.CustomDialogs.Stripe_dialogfragment;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;
import com.app.admin.sellah.view.activities.MainActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.stripe.StripeSession.API_ACCESS_TOKEN;
import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;

/**
 * PaymentFragment.class
 * By Raghubeer singh virk
 *
 * @implSpec have options to connect account with stripe and manage saved cards.
 * @implNote allows add card functionality id sellah account is connected with stripe connect.
 */
@SuppressLint("ValidFragment")
public class PaymentFragment extends Fragment {

    @BindView(R.id.edt_card_edit)
    TextView edtCardEdit;


    @BindView(R.id.rel_stripe_connect)
    RelativeLayout relStripeConnect;

    @BindView(R.id.btn_stripe_connect)
    StripeButton btnStripeConnect;


    @BindView(R.id.p_onnewcardnumber)
    TextView pOnnewcardnumber;
    @BindView(R.id.p_onnewcardholdername)
    TextView pOnnewcardholdername;
    @BindView(R.id.p_onnewcard_expire)
    TextView pOnnewcardExpire;
    @BindView(R.id.p_onnewcard_cvv)
    TextView pOnnewcardCvv;
    @BindView(R.id.rl_addnewstrpeaccount)
    RelativeLayout rlAddnewstrpeaccount;
    @BindView(R.id.rl_sellahwallet_clicklink)
    RelativeLayout rlSellahwalletClicklink;
    private Dialog dialog;
    private Animation myAnim;
    HashMap<EditText, String> bankDetaildialogMessages;
    ArrayList<EditText> allBankDetailFields;
    ArrayList<TextInputLayout> allBankdetailInputLayouts;
    final int GET_NEW_CARD = 2;


    Unbinder unbinder;

    CardDetailModel cardDetailModel;
    private String cardHolderName = "";
    private String cardNumber = "";
    private String cardExp = "";
    private String stripeId = "";
    private StripeApp StripeAppmApp;

    @SuppressLint("ValidFragment")
    public PaymentFragment(String stripeId) {
        this.stripeId = stripeId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_payment_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

         Log.e("onGetDataSuccess: ", "d"+(HelperPreferences.get(getActivity()).getString(STRIPE_VERIFIED)));

        if ((HelperPreferences.get(getActivity()).getString(STRIPE_VERIFIED).equals("") || HelperPreferences.get(getActivity()).getString(STRIPE_VERIFIED).equals("N"))) {
              rlSellahwalletClicklink.setVisibility(View.VISIBLE);
            Log.e( "onCreateView: ","1" );
            rlAddnewstrpeaccount.setVisibility(View.GONE);
        } else {
            Log.e( "onCreateView: ","2" );
            rlSellahwalletClicklink.setVisibility(View.GONE);
            rlAddnewstrpeaccount.setVisibility(View.VISIBLE);
        }

        new ApisHelper().getCardApi(getActivity(), new ApisHelper.OnGetCardDataListners() {
            @Override
            public void onGetDataSuccess(CardDetailModel body) {

                Gson gson = new GsonBuilder().create();
                setUpcards(body.getCards());//set card list adapter
            }

            @Override
            public void onGetDataFailure() {


            }
        });
        StripConnect();
        return view;
    }


    private void setUpcards(List<Card> cards) {
        if (cards != null && cards.size() > 0) {
//            txtCardNumber.setText("No card detail available");

            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).getDefault_card().equals("Y")) {
                    pOnnewcardnumber.setText("**** **** **** " + cards.get(i).getLast4());
                    pOnnewcardholdername.setText(cards.get(i).getName());
                    pOnnewcardExpire.setText(cards.get(i).getExpMonth() + "/" + cards.get(i).getExpYear());
                    break;
                }
            }


        } else {

        }

    }


    private void StripConnect() {

        StripeAppmApp = new StripeApp(getActivity(), "geniusAppDeveloper", "ca_EWK1BYRqruSX1X92DbtLY8UiV46ADGoC",
                "sk_test_HDkDbhty58uz3aaJi2TDllrR", "https://developer.android.com", "read_write");
//        mStripeButton = (StripeButton) findViewById(R.id.btnStripeConnect);
        btnStripeConnect.setStripeApp(StripeAppmApp);
        btnStripeConnect.setConnectMode(StripeApp.CONNECT_MODE.DIALOG);
        btnStripeConnect.addStripeConnectListener(new StripeConnectListener() {

            @Override
            public void onConnected() {
                Log.e("Connected_as", "onConnected: " + StripeAppmApp.getUserId());
                if (StripeAppmApp.getUserId() != null) {

                    new ApisHelper().linkStripApi(getActivity(), StripeAppmApp.getUserId(), new ApisHelper.StripeConnectCallback() {
                        @Override
                        public void onStripeConnectSuccess(String msg) {
                            Snackbar.make(((MainActivity) getActivity()).relRoot, msg, Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();
                            HelperPreferences.get(getActivity()).saveString(API_ACCESS_TOKEN, StripeAppmApp.getUserId());

                        }

                        @Override
                        public void onStripeConnectFailure() {
                            Snackbar.make(getActivity().getWindow().getDecorView(), "Unable to link account with stripe at this movements", Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();

                        }
                    });
                }
//                tvSummary.setText("Connected as " + StripeAppmApp.getAccessToken());
            }

            @Override
            public void onDisconnected() {
                Log.e("Disconnected", "Disconnected: ");
//                tvSummary.setText("Disconnected");
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }

        });
    }


    @OnClick(R.id.edt_card_edit)
    public void editCardDetail() {
        Intent intent = new Intent(getActivity(), ShowCreditCardDetailFragment.class);
        intent.putExtra("payment", "payment");
        startActivityForResult(intent, GET_NEW_CARD);
    }


    public void bankDetail() {

        dialog = new Dialog(getActivity());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.edit_bank_account_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        EditText edtBankName = dialog.findViewById(R.id.edt_bank_name);
        EditText edtAccountNo = dialog.findViewById(R.id.edt_account_no);

        NoChangingBackgroundTextInputLayout ilBankName = dialog.findViewById(R.id.il_bank_name);
        NoChangingBackgroundTextInputLayout ilAccountNo = dialog.findViewById(R.id.il_account_no);
//        NoChangingBackgroundTextInputLayout il_postal= dialog.findViewById(R.id.il_postal);

        allBankDetailFields = new ArrayList<>();
        allBankDetailFields.add(edtBankName);
        allBankDetailFields.add(edtAccountNo);


        allBankdetailInputLayouts = new ArrayList<>();
        allBankdetailInputLayouts.add(ilBankName);
        allBankdetailInputLayouts.add(ilAccountNo);

        bankDetaildialogMessages = new HashMap<>();
        bankDetaildialogMessages.put(edtBankName, "Please enter bank name");
        bankDetaildialogMessages.put(edtAccountNo, "Please enter account no.");

        Button btn_change = dialog.findViewById(R.id.btn_done);

        ImageView back = dialog.findViewById(R.id.back_img);

        myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);
        back.startAnimation(myAnim);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(), "weyqdfueyfuyru", Toast.LENGTH_SHORT).show();

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
//                dialog.dismiss();
            }
        });


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* if (edt_current_pass.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter your cu", Toast.LENGTH_SHORT).show();
                } else {

                }*/
                if (isBankDetailFormValid()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            }
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected boolean isBankDetailFormValid() {

        for (EditText editText : allBankDetailFields) {
            if (editText.getText().toString().trim().isEmpty()) {
                allBankdetailInputLayouts.get(allBankDetailFields.indexOf(editText))
                        .setError(bankDetaildialogMessages.get(editText));
//                requestFocus(editText);
                return false;
            } else {
                allBankdetailInputLayouts.get(allBankDetailFields.indexOf(editText)).setErrorEnabled(false);
            }
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_NEW_CARD && resultCode == RESULT_OK) {

            String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);


        }

    }

    @OnClick(R.id.rl_sellahwallet_clicklink)
    public void onViewClicked() {
        Stripe_dialogfragment stripe_dialogfragment = new Stripe_dialogfragment();
        stripe_dialogfragment.show(getActivity().getFragmentManager(), "");
        // btnStripeConnect.performClick();
    }

    @Override
    public void onStop() {
        super.onStop();
        new ApisHelper().cancel_striipe_request();
    }
}
