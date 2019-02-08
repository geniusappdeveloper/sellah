package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.AddCardVPAdapter;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.stripe.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Account;
import com.stripe.model.Card;
import com.stripe.model.ExternalAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.CARDHOLDER_NAME;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.CARD_EXP_MONTH;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.CARD_EXP_YEAR;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.CARD_NUMBER;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.view.CustomDialogs.PromoteDialog.PUBLISHABLE_KEY;

public class AddCreditCardDetailFragment extends AppCompatActivity implements AddCardDetails.OnTextWacherCallBack {

    Unbinder unbinder;
    View view;
    @BindView(R.id.credit_card_view)
    CreditCardView creditCardView;
    @BindView(R.id.vp_cardDetails)
    ViewPager vpCardDetails;
    @BindView(R.id.li_card_detail)
    LinearLayout liCardDetail;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.li_bottom_root)
    RelativeLayout liBottomRoot;

    boolean isCardBack = false;
    boolean isCardNumberAvailable = false;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.title_sell)
    TextView titleSell;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    String cardHolderName, cardNumber, cardExp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_credit_card_detail);
        unbinder = ButterKnife.bind(this);
        Global.StatusBarLightMode(this);

        getIntenData();


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new AddCardDetails("1", this, cardHolderName));
        fragments.add(new AddCardDetails("2", this, cardNumber));
        fragments.add(new AddCardDetails("3", this, cardExp));
        fragments.add(new AddCardDetails("4", this, ""));
        vpCardDetails.setAdapter(new AddCardVPAdapter(getSupportFragmentManager(), fragments));


        vpCardDetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                int currentItem = vpCardDetails.getCurrentItem();

                if (currentItem == (vpCardDetails.getAdapter().getCount() - 1)) {
//                    right.setVisibility(View.INVISIBLE);
                    if (!isCardBack) {
                        creditCardView.showBack();
                        isCardBack = true;
                    }
                } else {
                    if (isCardBack) {
                        creditCardView.showFront();
                        isCardBack = false;
                    }
//                    right.setVisibility(View.VISIBLE);
                }
               /* if(position==vpCardDetails.getChildCount()){


                }else{
                    if(isCardBack){
                        creditCardView.showFront();
                        isCardBack=false;
                    }
                }*/
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getIntenData() {
        Intent in = getIntent();
        cardHolderName = in.hasExtra(SAConstants.Keys.CARDHOLDER_NAME) ? in.getStringExtra(CARDHOLDER_NAME) : "";
        cardNumber = in.hasExtra(SAConstants.Keys.CARD_NUMBER) ? "XXXXXXXXXXXX"+in.getStringExtra(CARD_NUMBER) : "";
        String expYear="",expMonth="";
        expYear=in.hasExtra(SAConstants.Keys.CARD_EXP_YEAR) ? in.getStringExtra(CARD_EXP_YEAR) : "";
        expMonth=in.hasExtra(SAConstants.Keys.CARD_EXP_MONTH) ? in.getStringExtra(CARD_EXP_MONTH) : "";
        cardExp = !TextUtils.isEmpty((expMonth+"/"+expYear))?(expMonth+"/"+expYear):"";
    }

   /* @Override
    public View onCreate(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_credit_card_detail, container, false);
            unbinder = ButterKnife.bind(this, view);
            ArrayList<Fragment> fragments=new ArrayList<>();
            fragments.add(new AddCardDetails("1"));
            fragments.add(new AddCardDetails("2"));
            fragments.add(new AddCardDetails("3"));
            fragments.add(new AddCardDetails("4"));
            vpCardDetails.setAdapter(new AddCardVPAdapter(getChildFragmentManager(),fragments));
        } else {
            unbinder = ButterKnife.bind(this, view);
        }

        unbinder1 = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/

    @OnClick({R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
        int currentItem = vpCardDetails.getCurrentItem();
        switch (view.getId()) {
            case R.id.btn_previous:
                if (currentItem > 0) {
                    vpCardDetails.setCurrentItem(vpCardDetails.getCurrentItem() - 1);
                } else if (currentItem == 0) {
                    onBackPressed();
                } else {
                    vpCardDetails.setCurrentItem(vpCardDetails.getCurrentItem());
                }
                break;
            case R.id.btn_next:
                if (currentItem == (vpCardDetails.getAdapter().getCount() - 1)) {
                    if (creditCardView.getCardHolderName().equalsIgnoreCase("")) {
                        Snackbar.make(liBottomRoot, "Please enter card holder name.", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        vpCardDetails.setCurrentItem(0);
                    } else if (creditCardView.getCardNumber().equalsIgnoreCase("")) {
                        Snackbar.make(liBottomRoot, "Please enter card number.", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        vpCardDetails.setCurrentItem(1);
                    } else if (creditCardView.getExpiry().equalsIgnoreCase("")) {
                        Snackbar.make(liBottomRoot, "Please enter expiry date.", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        vpCardDetails.setCurrentItem(2);
                    }else if (creditCardView.getCVV().equalsIgnoreCase("")) {
                        Snackbar.make(liBottomRoot, "Please enter CVV code.", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        vpCardDetails.setCurrentItem(3);
                    } else {
                        updateCreditCardApi();
                    }
                } else {
                    vpCardDetails.setCurrentItem(vpCardDetails.getCurrentItem() + 1);
                }
                break;
        }
    }

    @Override
    public void onNameChangeListner(String name) {
        Log.e("card_name", name);
        creditCardView.setCardHolderName(name);
    }

    @Override
    public void oncardNumberChangeListner(String number) {
        Log.e("card_name", number);
        creditCardView.setCardNumber(number.replace(" ", ""));
    }

    @Override
    public void oncardExpChangeListner(String expDate) {
        Log.e("card_name", expDate);
        creditCardView.setCardExpiry(expDate);
    }

    @Override
    public void oncardCvvChangeListner(String cvv) {
        Log.e("card_name", cvv);
        creditCardView.setCVV(cvv);
    }

    private void updateCreditCardApi() {

        List<String> items = Arrays.asList(creditCardView.getExpiry().split("\\s*/\\s*"));
        Log.e("List", "updateCreditCardApi: "+items.size());
        String expMonth="",expYear="";
        for(int i=0;i<items.size();i++){
            if(i==0){
                expMonth=items.get(i);
            }if(i==1){
                expYear=items.get(i);
            }
        }
        Log.e("Expiry", "updateCreditCardApi: "+expMonth+"/"+expYear);

        Dialog dialog=S_Dialogs.getLoadingDialog(AddCreditCardDetailFragment.this);
        dialog.show();
        Call<Common> updateCall = Global.WebServiceConstants.getRetrofitinstance().addCardApi(HelperPreferences.get(AddCreditCardDetailFragment.this).getString(UID)
                , creditCardView.getCardHolderName(), creditCardView.getCardNumber(), expMonth, expYear,creditCardView.getCVV());
        updateCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(AddCreditCardDetailFragment.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        /*
                        Snackbar.make(liBottomRoot, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();*/
//                        new On.Handle(
                    } else {
                        Snackbar.make(liBottomRoot, "Something went's wrong", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                    if(dialog!=null&&dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                Snackbar.make(liBottomRoot, "Please try again latter.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();

                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.menu)
    public void onViewClicked() {
        onBackPressed();
    }

   /* class CreateStripeAccount extends AsyncTask<String, Void, ExternalAccount> {

        private Exception exception;
        Dialog dialog;

        public Dialog getDialog() {
//            dialog.show();
            return dialog;
        }

        public void setDialog(Dialog dialog) {
            this.dialog = dialog;
        }


        public void showDialog() {
            this.dialog.show();
        }


        protected ExternalAccount doInBackground(String... key) {

//            try {
//                Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";

            Stripe.apiKey = "sk_test_QW9KCbQ08S6BSGogNk3XKDTa";
            String token=key[0];
            Account account = null;
//            Stripe.apiKey = "sk_test_QW9KCbQ08S6BSGogNk3XKDTa";

            Map<String, Object> tokenParams = new HashMap<String, Object>();

            Map<String, Object> cardParams = new HashMap<String, Object>();
            cardParams.put("number", "5200828282828888");
            cardParams.put("exp_month", 12);
            cardParams.put("exp_year", 2022);
            cardParams.put("cvc", "314");
            cardParams.put("currency", "usd");
            cardParams.put("default_for_currency",true);
            tokenParams.put("card", cardParams);
            com.stripe.model.Token token2=null;
            try {
                    token2=com.stripe.model.Token.create(tokenParams);
            } catch (AuthenticationException e) {
                Log.e("StripeConnect_T", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                Log.e("StripeConnect_T", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (APIConnectionException e) {
                Log.e("StripeConnect_T", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (CardException e) {
                Log.e("StripeConnect_T", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (APIException e) {
                Log.e("StripeConnect_T", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("external_account", token2.getId());

            ExternalAccount card = null;
            try {
                account = Account.retrieve("acct_1DmbuTD7ESFeSWB1", null);
//                card=account.getExternalAccounts().create(params);
                card = account.getExternalAccounts().create(params);
            } catch (AuthenticationException e) {
                e.printStackTrace();
                Log.e("StripeConnect_get", "onStripeConnectClicked: " + e.getMessage());
            } catch (InvalidRequestException e) {
                Log.e("StripeConnect_get", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (APIConnectionException e) {
                Log.e("StripeConnect_get", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (CardException e) {
                Log.e("StripeConnect_get", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            } catch (APIException e) {
                Log.e("StripeConnect_get", "onStripeConnectClicked: " + e.getMessage());
                e.printStackTrace();
            }
            return card;
        }

        protected void onPostExecute(ExternalAccount feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (feed != null) {
                Card card = (Card) feed;
                Log.e("StripeConnect", "onPostExecute: " + card.getId());
//                account.getLoginLinks().create();
//                feed.set
            } else {
                Log.e("StripeConnect", "onPostExecute: unable to register");
            }
        }
    }
    String token1="";
    class CreateToken extends AsyncTask<String, Void, String> {

        private Exception exception;
        Dialog dialog;

        public Dialog getDialog() {
//            dialog.show();
            return dialog;
        }

        public void setDialog(Dialog dialog) {
            this.dialog = dialog;
        }


        public void showDialog() {
            this.dialog.show();
        }


        protected String doInBackground(String... key) {

//            try {
//                Stripe.apiKey = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";

            Stripe.apiKey = key[0];
            com.stripe.android.model.Card card = new com.stripe.android.model.Card(
                    cardNumber,
                    02,
                    2022,
                    "123");
            //   card.setCurrency(Currency);
//        boolean validation = card.validateCard();


//            if (validation) {

                com.stripe.android.Stripe stripe = new com.stripe.android.Stripe(AddCreditCardDetailFragment.this, PUBLISHABLE_KEY);
                stripe.createToken(
                        card,
                        PUBLISHABLE_KEY,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                Log.e("tokengetHere", "t  " + token.getId() + "");
                                token1=token.getId();
                            }

                            public void onError(Exception error) {

                            }
                        });
            return token1;
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (feed != null) {
                CreateStripeAccount stripeAccount = new CreateStripeAccount();
                stripeAccount.setDialog(dialog);
                stripeAccount.showDialog();
                stripeAccount.execute(feed);
//                Card card = (Card) feed;
                Log.e("StripeConnect", "onPostExecute: " + feed);
//                account.getLoginLinks().create();
//                feed.set
            } else {
                Log.e("StripeConnect", "onPostExecute: unable to register");
            }
        }
    }*/
}
