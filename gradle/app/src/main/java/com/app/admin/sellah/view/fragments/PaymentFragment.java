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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.CardDetails.Card;
import com.app.admin.sellah.model.extra.CardDetails.CardDetailModel;
import com.app.admin.sellah.view.CustomAnimations.MyBounceInterpolator;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.CardListAdapter;
import com.cooltechworks.creditcarddesign.CreditCardUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.stripe.StripeSession.API_ACCESS_TOKEN;

@SuppressLint("ValidFragment")
public class PaymentFragment extends Fragment {
    @BindView(R.id.txt_card_detail)
    TextView txtCardDetail;
    @BindView(R.id.edt_card_edit)
    TextView edtCardEdit;
    @BindView(R.id.bankImg)
    ImageView bankImg;
    @BindView(R.id.banl_acct_text)
    TextView banlAcctText;
    @BindView(R.id.dbs_text)
    TextView dbsText;
    @BindView(R.id.txt_card_number)
    TextView txtCardNumber;
    @BindView(R.id.txt_connect_stripe)
    TextView txtConnectStripe;
    @BindView(R.id.rel_stripe_connect)
    RelativeLayout relStripeConnect;
    @BindView(R.id.rel_add_card)
    RelativeLayout relAddCard;
    @BindView(R.id.btn_stripe_connect)
    StripeButton btnStripeConnect;
    @BindView(R.id.rec_cards)
    RecyclerView recCards;
    @BindView(R.id.txt_connection_status)
    TextView txtConnectionStatus;
    private Dialog dialog;
    private Animation myAnim;
    HashMap<EditText, String> bankDetaildialogMessages;
    ArrayList<EditText> allBankDetailFields;
    ArrayList<TextInputLayout> allBankdetailInputLayouts;
    final int GET_NEW_CARD = 2;

    @BindView(R.id.txt_edit_bank_detail)
    TextView txtEditbankDetail;

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
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        if (TextUtils.isEmpty(HelperPreferences.get(getActivity()).getString(API_ACCESS_TOKEN))) {
//            relStripeConnect.setVisibility(View.VISIBLE);
            relAddCard.setVisibility(View.GONE);
            txtConnectionStatus.setVisibility(View.VISIBLE);
        } else {
            txtConnectionStatus.setVisibility(View.GONE);
//            relStripeConnect.setVisibility(View.GONE);
            relAddCard.setVisibility(View.VISIBLE);
        }
        new ApisHelper().getCardApi(getActivity(), new ApisHelper.OnGetCardDataListners() {
            @Override
            public void onGetDataSuccess(CardDetailModel body) {
//                cardDetailModel = body;
                /*if (body.getRecord().size() > 0) {

                    cardHolderName = body.getRecord().get(0).getCardHolderName();
                    cardNumber = body.getRecord().get(0).getCardNumber();
                    cardExp = body.getRecord().get(0).getExpDate();
                    txtCardNumber.setText("ending in " + cardNumber.substring(12));
                    edtCardEdit.setText("EDIT");

                } else {
                    txtCardNumber.setText("No card detail available");
                    edtCardEdit.setText("ADD");
                }*/

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                setUpcards(body.getCards());
            }

            @Override
            public void onGetDataFailure() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
//                cardDetailModel = new GetCardDetailModel();
                txtCardNumber.setText("No card detail available");
                txtCardNumber.setVisibility(View.VISIBLE);
//                edtCardEdit.setText("ADD");
            }
        });

        StripConnect();
        return view;
    }

    private void setUpcards(List<Card> cards) {
        if (cards != null && cards.size() > 0) {
//            txtCardNumber.setText("No card detail available");
            txtCardNumber.setVisibility(View.GONE);
            CardListAdapter cardListAdapter = new CardListAdapter(getActivity(), cards, new CardListAdapter.OnCardOptionSelection() {
                @Override
                public void onCardSelectionClick(int pos, String cardId) {

                }

                @Override
                public void onCardRemoveListner(int pos, int updatedSize) {

                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recCards.setLayoutManager(layoutManager);
            recCards.setAdapter(cardListAdapter);
            Global.getTotalHeightofLinearRecyclerView(getActivity(), recCards, R.layout.card_adapter_layout, 0);
        } else {
            Global.getTotalHeightofLinearRecyclerView(getActivity(), recCards, R.layout.card_adapter_layout, 0);
            txtCardNumber.setText("No card detail available");
            txtCardNumber.setVisibility(View.VISIBLE);
        }
//        recCards.setAnimation(new DefaultItemAnimator());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            new ApisHelper().getCardApi(getActivity(), new ApisHelper.OnGetCardDataListners() {
                @Override
                public void onGetDataSuccess(CardDetailModel body) {
                    setUpcards(body.getCards());
                }

                @Override
                public void onGetDataFailure() {

                }
            });
        }
    }

    private void StripConnect() {
        StripeAppmApp = new StripeApp(getActivity(), "geniusAppDeveloper", "ca_9bmLYpWBQDumLtFp2KZ7bE90kHjXS5le",
                "sk_test_QW9KCbQ08S6BSGogNk3XKDTa", "https://developer.android.com", "read_write");
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
                            txtConnectionStatus.setVisibility(View.GONE);
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
        Intent intent = new Intent(getActivity(), AddCreditCardDetailFragment.class);
   /*     intent.putExtra(SAConstants.Keys.CARDHOLDER_NAME, cardHolderName);
        intent.putExtra(SAConstants.Keys.CARD_NUMBER, cardNumber);
        intent.putExtra(SAConstants.Keys.CARD_EXP_YEAR, cardExp);*/
        startActivityForResult(intent, GET_NEW_CARD);
    }

    @OnClick(R.id.txt_edit_bank_detail)
    public void editBankDetail() {
        bankDetail();
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
    public void onStop() {
        super.onStop();
        new ApisHelper().cancel_striipe_request();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_NEW_CARD && resultCode == RESULT_OK) {

            String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);


//            Log.e("Card_deails",cardHolderName);
            // Your processing goes here.

        }

    }
/*

    @OnClick(R.id.btn_stripe_connect)
    public void onStripeConnectClicked() {
        */
/*Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        CreateStripeAccount stripeAccount = new CreateStripeAccount(getActivity(), new CreateStripeAccount.CreateAccountController() {
            @Override
            public void onAccountCreationSuccess(Account account) {
                new ApisHelper().linkStripApi(getActivity(), account.getId(), new ApisHelper.StripeConnectCallback() {
                    @Override
                    public void onStripeConnectSuccess(String msg) {
                        Snackbar.make(((MainActivity) getActivity()).relRoot, msg, Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        relStripeConnect.setVisibility(View.GONE);
                        relAddCard.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onStripeConnectFailure() {
                        Snackbar.make(getActivity().getWindow().getDecorView(), "Unable to link account with stripe at this movements", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();

                    }
                });
            }

            @Override
            public void onAccountCreationFailure() {

            }
        });
        stripeAccount.setDialog(dialog);
        stripeAccount.showDialog();
        stripeAccount.execute("sk_test_4eC39HqLyjWDarjtT1zdp7dc");*//*
     */
    /*

     *//*

        String url = "https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_9bmL4zjsRk60bCUfXO6KcJLjkqhupXIB&scope=read_write&redirect_uri=https://developer.android.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
*/

}
