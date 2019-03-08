package com.app.admin.sellah.view.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.Extras.Datedialog;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.Validations;
import com.app.admin.sellah.view.activities.LoginActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Currency;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.stripe.StripeSession.STRIPE_VERIFIED;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class Stripe_dialogfragment extends DialogFragment {

    @BindView(R.id.addnewinfo_back)
    ImageView addnewinfoBack;
    @BindView(R.id.addnewvideo_toolbar)
    RelativeLayout addnewvideoToolbar;
    @BindView(R.id.str_edt_firstname)
    EditText strEdtFirstname;
    @BindView(R.id.str_edt_lastname)
    EditText strEdtLastname;
    @BindView(R.id.str_edt_dob)
    TextView strEdtDob;
    @BindView(R.id.str_edt_personalid)
    EditText strEdtPersonalid;

    @BindView(R.id.str_address)
    EditText strAddress;



    @BindView(R.id.str_edt_postal_coe)
    EditText strEdtPostalCoe;

    @BindView(R.id.str_edt_accountnumber)
    EditText strEdtAccountnumber;
    @BindView(R.id.rootTag)
    LinearLayout rootTag;
    @BindView(R.id.add_info_post)
    Button addInfoPost;
    Unbinder unbinder;

    @BindView(R.id.str_doblayout)
    LinearLayout strDoblayout;
    private WebService service;
    String firstname, lastname, ip, dob,  pid, address,  postalcodem,  accountnumber;
    String currencyCode;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stripe_new_ui, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        unbinder = ButterKnife.bind(this, view);
        service = Global.WebServiceConstants.getRetrofitinstance();
        getLocalIpAddress();

        focuslisteneres();




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Global.from_register = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getstrings() {
        firstname = strEdtFirstname.getText().toString();
        lastname = strEdtLastname.getText().toString();
        dob = strEdtDob.getText().toString();
        pid = strEdtPersonalid.getText().toString();

        address = strAddress.getText().toString();

        postalcodem = strEdtPostalCoe.getText().toString();
        accountnumber = strEdtAccountnumber.getText().toString();




    }

    private void focuslisteneres() {


        strEdtFirstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    strEdtFirstname.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    strEdtFirstname.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });

        strEdtLastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    strEdtLastname.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    strEdtLastname.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });

        strEdtDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    strEdtDob.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    strEdtDob.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });


        strEdtPersonalid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    strEdtPersonalid.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    strEdtPersonalid.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });


        strAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    strAddress.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    strAddress.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });







        strEdtPostalCoe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    strEdtPostalCoe.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    strEdtPostalCoe.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });



        strEdtAccountnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    strEdtAccountnumber.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    strEdtAccountnumber.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });

    }

    @OnClick({R.id.str_doblayout,R.id.addnewinfo_back, R.id.add_info_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.str_doblayout:

                Log.e("onViewClicked: ", "jkm");
                Datedialog datedialog = new Datedialog();
                datedialog.seteditext(strEdtDob);
                datedialog.show(getActivity().getFragmentManager(), "");

                strEdtDob.setBackgroundResource(R.drawable.live_product_detail_background);
                break;
            case R.id.addnewinfo_back:
                dismiss();
                break;
            case R.id.add_info_post:
                getstrings();
                Log.e("firs: ", firstname);
                Log.e("last: ",lastname);
                Log.e("dob: ", dob);
                Log.e("pid: ", pid);
                Log.e("bid: ", "");
                Log.e("add: ", address);
                Log.e("bank: ", "");
                Log.e("country: ", "");
                Log.e("state: ", "");
                Log.e("city: ", "");
                Log.e("postal: ", postalcodem);
                Log.e("currrency: ", "");
                Log.e("account: ", accountnumber);
                Log.e("Ip: ", ip);

                boolean valid = new Validations().stripevalid(getActivity(), firstname, lastname, dob, pid,  address, "Dd", "dd", "dd", postalcodem, "dd", accountnumber, "fgfg");
                if (valid) {
                   addtripeaccount(getActivity());
                }

                break;
        }
    }


    public void addtripeaccount(Context context) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Log.e("user", "" + HelperPreferences.get(context).getString(UID));
        Call<JsonObject> getProfileCall = service.stripeadd(HelperPreferences.get(context).getString(UID), firstname, lastname, dob, pid,  address, "SG", "Singapore", "Singapore", postalcodem, "SGD", accountnumber, "1100-000", ip, "SG");
        getProfileCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                  //  Log.e("onResponse: ", response.errorBody().string());
                    Log.e("onResponse: ", response.body().toString());
                    JSONObject obj = new JSONObject(response.body().toString());
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("0"))
                    {
                        Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_LONG).show();
                        ((MainActivity) getActivity()).getProfileData();
                        Stripe_image_verification_dialogfragment dialogfragment = new Stripe_image_verification_dialogfragment();
                        dialogfragment.show(getActivity().getFragmentManager(),"");
                        dismiss();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Wrong information",Toast.LENGTH_LONG).show();
                }





            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                Log.e("stripe", "onFailure: " + t.getMessage());
            }
        });
    }


    private Map<String, String> getAvailableCurrencies() {
        Locale[] locales = Locale.getAvailableLocales();

        // We use TreeMap so that the order of the data in the map sorted
        // based on the country name.
        Map<String, String> currencies = new TreeMap<>();
        for (Locale locale : locales) {
            try {
                currencies.put(locale.getDisplayCountry(),
                        Currency.getInstance(locale).getCurrencyCode());
            } catch (Exception e) {
                // when the locale is not supported
            }
        }
        return currencies;
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.e("***** IP=", "" + ip);

                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("eroor", ex.toString());
        }
        return null;
    }


}
