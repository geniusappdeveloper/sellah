package com.app.admin.sellah.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.AddProductDatabase;
import com.app.admin.sellah.model.extra.AddProductModel;
import com.app.admin.sellah.model.extra.SpinnerStateCheck;
import com.app.admin.sellah.model.extra.getProductsModel.GetProductList;
import com.app.admin.sellah.model.extra.getProductsModel.Result;
import com.app.admin.sellah.view.CustomDialogs.AddProductPromoteDialog;
import com.app.admin.sellah.view.CustomDialogs.PaymentDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.adapter.SpinnerStateAdapter;
import com.app.admin.sellah.view.fragments.HomeFragment;
import com.app.admin.sellah.view.fragments.ProductFrgament;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.PROFILETAG;
import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class AddNewTransaction extends AppCompatActivity {


    @BindView(R.id.addnewtrans_back)
    ImageView addnewtransBack;
    @BindView(R.id.addnewvideo_toolbar)
    RelativeLayout addnewvideoToolbar;
    @BindView(R.id.spinner_payment_mode)
    Spinner spinnerPaymentMode;
    @BindView(R.id.spinner_mode_of_delivery)
    Spinner spinnerModeOfDelivery;
    @BindView(R.id.delivery_img)
    ImageView deliveryImg;
    @BindView(R.id.delivery_rl)
    RelativeLayout deliveryRl;
    @BindView(R.id.spin_sell_internationally)
    Spinner spinSellInternationally;
    @BindView(R.id.currency_img)
    ImageView currencyImg;
    @BindView(R.id.currency_rl)
    RelativeLayout currencyRl;
    @BindView(R.id.featured_sp)
    Spinner featuredSp;
    @BindView(R.id.featured_img)
    ImageView featuredImg;
    @BindView(R.id.feature_rl)
    RelativeLayout featureRl;
    @BindView(R.id.edt_budget)
    EditText edtBudget;
    @BindView(R.id.txt_no_of_clicks)
    TextView txtNoOfClicks;
    @BindView(R.id.add_product_post)
    Button addProductPost;
    ArrayList<SpinnerStateCheck> paymentOptions = new ArrayList<>();
    ArrayList<SpinnerStateCheck> deliveryOptions = new ArrayList<>();
    @BindView(R.id.txt_payment_type)
    TextView txtPaymentType;
    @BindView(R.id.rl3)
    RelativeLayout rl3;
    @BindView(R.id.payemt_img)
    ImageView payemtImg;
    @BindView(R.id.txt_delivery)
    TextView txtDelivery;
    @BindView(R.id.rootTag)
    RelativeLayout rootTag;
    private String paymentType = "";
    private String deliveryType = "";
    private Result productDetails;
    List<String> imageList = new ArrayList<>();
    private boolean isEditing = false;
    boolean isPromotClicked = false;
    String isPromotingProduct = "N";
    private String promotepackageId = "NA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_transaction);
        ButterKnife.bind(this);
        setUpPaymentOptions();
        setUpDeliveryOptions();
        getdata();


    }


    private void setUpPaymentOptions() {
        List<String> items = null;
        if (productDetails != null) {
            items = Arrays.asList(productDetails.getPaymentMode().split("\\s*,\\s*"));
        }
        final CharSequence[] select_paymentoption = getResources().getTextArray(R.array.add_product_payment_method);
        for (int i = 0; i < select_paymentoption.length; i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(select_paymentoption[i].toString());
            if (items != null && items.size() > 0) {
                for (String option : items) {
                    if (select_paymentoption[i].toString().toLowerCase().contains(option)) {
                        stateVO.setSelected(true);
                    } else {
                        stateVO.setSelected(false);
                    }
                }

                paymentType = productDetails.getPaymentMode();


            } else {
                Log.e("come: ", AddProductDatabase.payment_method);

                if (!AddProductDatabase.payment_method.isEmpty()) {
                    paymentType = AddProductDatabase.payment_method;
                    txtPaymentType.setTextColor(getResources().getColor(R.color.colorBlack));
                    payemtImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    rl3.setBackgroundResource(R.drawable.live_product_detail_background);

                } else {
                    paymentType = "Payment Method";
                    txtPaymentType.setTextColor(getResources().getColor(R.color.light_grey));
                }


               /* ArrayList<String> selection = new ArrayList<>();
                if (select_paymentoption[i].toString().toLowerCase().contains("credit/debit")) {
                    stateVO.setSelected(true);

                    selection.add(select_paymentoption[i].toString());
//                    paymentType = select_paymentoption[i].toString();
                } else {
                    stateVO.setSelected(false);
                }
                paymentType = TextUtils.join(", ", selection);*/
            }

            txtPaymentType.setText(paymentType);
            paymentOptions.add(stateVO);
        }

        SpinnerStateAdapter myAdapterPaymentOtions = new SpinnerStateAdapter(this, 0, paymentOptions, true, "payment", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            paymentType = selection;


            if (TextUtils.isEmpty(selection)) {

                txtPaymentType.setText(select_paymentoption[0]);
                txtPaymentType.setTextColor(getResources().getColor(R.color.light_grey));
                payemtImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                rl3.setBackgroundResource(R.drawable.live_product_detail_grey_background);

            } else {

                txtPaymentType.setText(selection);
                txtPaymentType.setTextColor(getResources().getColor(R.color.colorBlack));
                payemtImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                rl3.setBackgroundResource(R.drawable.live_product_detail_background);


            }
        });
        spinnerPaymentMode.setAdapter(myAdapterPaymentOtions);

    }


    private void setUpDeliveryOptions() {
        List<String> items = null;
        if (productDetails != null) {
            items = Arrays.asList(productDetails.getDelivery().split("\\s*,\\s*"));

        }
//        List<String> items = Arrays.asList(productDetails.getDelivery().split("\\s*,\\s*"));
        final CharSequence[] selectDeliveryOption = getResources().getTextArray(R.array.add_product_delivery);

        for (int i = 0; i < selectDeliveryOption.length; i++) {
            SpinnerStateCheck stateVO = new SpinnerStateCheck();
            stateVO.setTitle(selectDeliveryOption[i].toString());
            if (items != null && items.size() > 0) {
                for (String option : items) {
                    if (selectDeliveryOption[i].toString().toLowerCase().contains(option)) {
                        stateVO.setSelected(true);
                    } else {
                        stateVO.setSelected(false);
                    }
                }
                deliveryType = productDetails.getDelivery();

            } else {

                if (!AddProductDatabase.modes_of_delivery.isEmpty()) {
                    deliveryType = AddProductDatabase.modes_of_delivery;
                    txtDelivery.setTextColor(getResources().getColor(R.color.colorBlack));
                    deliveryImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    deliveryRl.setBackgroundResource(R.drawable.live_product_detail_background);
                } else {
                    ArrayList<String> selction = new ArrayList<>();
                    selction.add(selectDeliveryOption[0].toString());
                    deliveryType = TextUtils.join(", ", selction);
                    txtDelivery.setTextColor(getResources().getColor(R.color.light_grey));
                }


//                deliveryType = selectDeliveryOption[i].toString();
                // stateVO.setSelected(true);
            }
            txtDelivery.setText(deliveryType);
            deliveryOptions.add(stateVO);
        }
        SpinnerStateAdapter myAdapterDelivery = new SpinnerStateAdapter(this, 0, deliveryOptions, true, "delivery", (selection, instanceOf) -> {
            Log.e("Selection", "onCreate: " + selection + " : " + instanceOf);
            deliveryType = selection;
            if (TextUtils.isEmpty(selection)) {
                txtDelivery.setText(selectDeliveryOption[0]);
                txtDelivery.setTextColor(getResources().getColor(R.color.light_grey));
                deliveryImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                deliveryRl.setBackgroundResource(R.drawable.live_product_detail_dash_background);

            } else {
                txtDelivery.setText(selection);
                txtDelivery.setTextColor(getResources().getColor(R.color.colorBlack));
                deliveryImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                deliveryRl.setBackgroundResource(R.drawable.live_product_detail_background);


            }
        });
        spinnerModeOfDelivery.setAdapter(myAdapterDelivery);

        ArrayAdapter<String> conditionSpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.currency));
        conditionSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        spinSellInternationally.setAdapter(conditionSpinAdapter);

        spinSellInternationally.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Log.e("category", item + " ");
                if (!item.toString().equalsIgnoreCase(
                        "Select Currency")) {
                    ((TextView) spinSellInternationally.getSelectedView()).setTextColor(Color.BLACK);
                    currencyImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    currencyRl.setBackgroundResource(R.drawable.live_product_detail_background);

                } else {
                    currencyImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                    ((TextView) spinSellInternationally.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                    currencyRl.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> featureSpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.feature));
        featureSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        featuredSp.setAdapter(featureSpinAdapter);

        featuredSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Log.e("category", item + " ");
                if (!item.toString().equalsIgnoreCase(
                        "Select option")) {
                    ((TextView) featuredSp.getSelectedView()).setTextColor(Color.BLACK);
                    featuredImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    featureRl.setBackgroundResource(R.drawable.live_product_detail_background);
                } else {
                    featuredImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                    featureRl.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                    ((TextView) featuredSp.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    @OnClick({R.id.addnewtrans_back, R.id.rl3, R.id.delivery_rl, R.id.currency_rl, R.id.feature_rl, R.id.add_product_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addnewtrans_back:
                adddata();
                onBackPressed();
                break;
            case R.id.rl3:
                spinnerPaymentMode.performClick();
                break;
            case R.id.delivery_rl:
                spinnerModeOfDelivery.performClick();
                break;
            case R.id.currency_rl:
                spinSellInternationally.performClick();
                break;
            case R.id.feature_rl:
                featuredSp.performClick();
                break;
            case R.id.add_product_post:


                Global.hideKeyboard(rootTag, this);
                imageList.addAll(AddProductDatabase.imageListG);
                if (imageList.size() == 0) {
                    Snackbar.make(rootTag, "Please add at-least one image for product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.name.equalsIgnoreCase("")) {
                    Snackbar.make(rootTag, "Please enter product name", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.category==0) {
                    Snackbar.make(rootTag, "Please select an category", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.sub_category==0) {
                    Snackbar.make(rootTag, "Please select an subcategory", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (paymentType.equals("")||paymentType.equalsIgnoreCase("Payment Mehtod")) {
                    Snackbar.make(rootTag, "Please select mode of payment", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (deliveryType.equalsIgnoreCase("")||deliveryType.equalsIgnoreCase("Select delivery mode")) {
                    Snackbar.make(rootTag, "Please select mode of delivery", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }/* else if (spinSellInternationally.getSelectedItem().toString().equalsIgnoreCase("sell internationally")) {
            Snackbar.make(rootTag, "Please select selling strategy", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else if (AddProductDatabase.price.equalsIgnoreCase("")) {
                    Snackbar.make(rootTag, "Please enter price for product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }/* else if (Global.getText(edtPrice).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter price for product", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else if (AddProductDatabase.condition==0) {
                    Snackbar.make(rootTag, "Please select condition of product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                } else if (AddProductDatabase.quantity.equalsIgnoreCase("")) {
                    Snackbar.make(rootTag, "Please select quantity of product", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }/* else if (Global.getText(edtBudget).equalsIgnoreCase("")) {
            Snackbar.make(rootTag, "Please enter budget", Snackbar.LENGTH_SHORT)
                    .setAction("", null).show();
        }*/ else {

                    AddProductModel model = new AddProductModel();
                    model.setUser_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(HelperPreferences.get(this).getString(UID))));
                    model.setName(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.name));
                    model.setCat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AddProductDatabase.catid)));
//            model.setCat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(subCatId)));
                    model.setSub_cat_id(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AddProductDatabase.subcatid)));
                    model.setPayment_mode(RequestBody.create(MediaType.parse("text/plain"), paymentType));
                    model.setDelivery(RequestBody.create(MediaType.parse("text/plain"), deliveryType));
                    model.setTags((RequestBody.create(MediaType.parse("text/plain"), TextUtils.join("|", AddProductDatabase.tagListG))));
                    if (spinSellInternationally.getSelectedItem().toString().equalsIgnoreCase("sell internationally")) {
                        model.setSell_internationally(RequestBody.create(MediaType.parse("text/plain"), "Y"));
                    } else {
                        model.setSell_internationally(RequestBody.create(MediaType.parse("text/plain"), "N"));
                    }
                    model.setPrice(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.price.replace("S$", "")));
                    if (AddProductDatabase.type==2) {
                        model.setFixed_price(RequestBody.create(MediaType.parse("text/plain"), "Y"));
                    } else {
                        model.setFixed_price(RequestBody.create(MediaType.parse("text/plain"), "N"));
                    }
                    if (AddProductDatabase.condition==2) {
                        model.setProduct_type(RequestBody.create(MediaType.parse("text/plain"), "N"));
                    } else {
                        model.setProduct_type(RequestBody.create(MediaType.parse("text/plain"), "U"));
                    }
                    model.setQuantity(RequestBody.create(MediaType.parse("text/plain"),AddProductDatabase.quantity));
                    model.setDescription(RequestBody.create(MediaType.parse("text/plain"), AddProductDatabase.description));
                    model.setBudget(RequestBody.create(MediaType.parse("text/plain"), Global.getText(edtBudget).replace("S$", "")));
                    model.setNo_of_clicks(RequestBody.create(MediaType.parse("text/plain"), "0"));

                    for (int i = 0; i < imageList.size(); i++) {

                        if (i == 0) {
                            model.setImage1(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image1"));
                            Log.e("image1", model.getImage1() + "");
                        } else if (i == 1) {
                            model.setImage2(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image2"));
                        } else if (i == 2) {
                            model.setImage3(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image3"));
                        } else if (i == 3) {
                            model.setImage4(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image4"));
                        } else if (i == 4) {
                            model.setImage5(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image5"));
                        } else if (i == 5) {
                            model.setImage6(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image6"));
                        } else if (i == 6) {
                            model.setImage7(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image7"));
                        } else if (i == 7) {
                            model.setImage8(ImageUploadHelper.convertImageTomultipart(imageList.get(i), "image8"));
                        }
                        if (Global.videopath.equals("no_image")) {

                        } else {
                            model.setProductVideo(ImageUploadHelper.convertImageTomultipart(Global.videopath, "product_video"));
                        }
                    }
                    if (isEditing) {
                       // model.setProduct_id(RequestBody.create(MediaType.parse("text/plain"), productId));
                       // editProduct(model);
                        Log.e("EditProduct", "allDone");

                    } else {
              /*  if (isPromotClicked) {
                    if (!promotepackageId.equalsIgnoreCase("NA")) {
                        isPromotingProduct="Y";
                        model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), promotepackageId));
                    } else {
                        isPromotingProduct="N";
                        model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), ""));
                    }
                    model.setPromote_product(RequestBody.create(MediaType.parse("text/plain"), isPromotingProduct));

                    addProduct(model);
                    Log.e("addProduct", "allDone");
                } else {*/
                        AddProductPromoteDialog.create(this, new AddProductPromoteDialog.PromoteCallback() {
                            @Override
                            public void onPackageSelected(String id) {
                                isPromotClicked = true;
                                promotepackageId = id;
                                if (!promotepackageId.equalsIgnoreCase("NA")) {
                                    isPromotingProduct = "Y";
                                    model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), promotepackageId));
                                } else {
                                    isPromotingProduct = "N";
                                    model.setPackageId(RequestBody.create(MediaType.parse("text/plain"), ""));
                                }
                                model.setPromote_product(RequestBody.create(MediaType.parse("text/plain"), isPromotingProduct));
                                addProduct(model);
                                Log.e("addProduct", "allDone");
                            }
                        }).show();
                    }

                }


                break;
        }
    }

    private void adddata() {


        AddProductDatabase data = new AddProductDatabase();


        if (!spinnerPaymentMode.getSelectedItem().toString().isEmpty())
            data.payment_method = paymentType;

        if (!spinnerModeOfDelivery.getSelectedItem().toString().isEmpty())
            data.modes_of_delivery = txtDelivery.getText().toString();


        if (!spinSellInternationally.getSelectedItem().toString().isEmpty())
            data.currency = spinSellInternationally.getSelectedItemPosition();

        if (!featuredSp.getSelectedItem().toString().isEmpty())
            data.feature = featuredSp.getSelectedItemPosition();


    }


    private void getdata() {

        if (AddProductDatabase.currency >= 0)
            spinSellInternationally.setSelection(AddProductDatabase.currency);

        if (AddProductDatabase.feature >= 0)
            featuredSp.setSelection(AddProductDatabase.feature);

    }


    public void addProduct(AddProductModel addProductModel) {

        WebService service = Global.WebServiceConstants.getRetrofitinstance();
        Dialog dialog = S_Dialogs.getLoadingDialog(AddNewTransaction.this);
        dialog.show();
        Call<GetProductList> addProductCall = service.addProductApi(addProductModel.getUser_id(), addProductModel.getName(), addProductModel.getCat_id(), addProductModel.getSub_cat_id()
                , addProductModel.getPayment_mode(), addProductModel.getDelivery(), addProductModel.getSell_internationally(), addProductModel.getPrice()
                , addProductModel.getFixed_price(), addProductModel.getProduct_type(), addProductModel.getQuantity(), addProductModel.getDescription()
                , addProductModel.getPromote_product(), addProductModel.getNo_of_clicks(), addProductModel.getBudget(), addProductModel.getTags()
                , addProductModel.getImage1(), addProductModel.getImage2(), addProductModel.getImage3(), addProductModel.getImage4(), addProductModel.getImage5(), addProductModel.getImage6()
                , addProductModel.getImage7(), addProductModel.getImage8(), addProductModel.getPackageId(), addProductModel.getProductVideo());
        addProductCall.enqueue(new Callback<GetProductList>() {
            @Override
            public void onResponse(Call<GetProductList> call, Response<GetProductList> response) {
                if (response.isSuccessful()) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                        Global.videopath="no_image";

                        if (isPromotingProduct.equalsIgnoreCase("Y")) {
                            try {
                                PaymentDialog.create(AddNewTransaction.this, "", HelperPreferences.get(AddNewTransaction.this).getString(UID), response.body().getResult().get(0).getId(), response.body().getPackage_id(), response.body().getPromote_id(), new PaymentDialog.PaymentCallBack() {
                                    @Override
                                    public void onPaymentSuccess() {

                                        Toast.makeText(AddNewTransaction.this, "Product promoted successfully.", Toast.LENGTH_SHORT).show();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();

                                        Intent intent = new Intent(AddNewTransaction.this,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));

                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onPaymentFail(String message) {
                                        Toast.makeText(AddNewTransaction.this, "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();

                                    }

                                    @Override
                                    public void onCancelDialog() {
                                        Intent intent = new Intent(AddNewTransaction.this,MainActivity.class);
                                        intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
                               /* PromoteDialog.create(getActivity(), response.body().getResult().get(0).getId(), new PromoteDialog.PromoteCallback() {
                                    @Override
                                    public void onPromoteSuccess() {
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                                        ProductFrgament fragment = new ProductFrgament();
                                        fragment.setArguments(bundle);
                                        ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

                                    }

                                    @Override
                                    public void onPromoteFailure() {
                                        Toast.makeText(getActivity(), "Unable to promote product at the movement", Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                                        clearForm(rootTag);
                                    }
                                }).show();*/
                            } catch (Exception e) {

                            }
                        } else {

                            Intent intent = new Intent(AddNewTransaction.this,MainActivity.class);
                            intent.putExtra(SAConstants.Keys.PRODUCT_DETAIL, response.body().getResult().get(0));
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).addToBackStack(PROFILETAG).commit();
                        }
                       /* Bundle bundle=new Bundle();
                        bundle.putString(SAConstants.Keys.CAT_ID, catId);
                        bundle.putString(SAConstants.Keys.SUB_CAT_ID, subCatId);
                        bundle.putParcelableArrayList(SAConstants.Keys.SUB_CAT_LIST, (ArrayList<? extends Parcelable>) subcategories);
                        SubCategoryFragment fragment=new SubCategoryFragment();
                        fragment.setArguments(bundle);*/

                    } else {
                        Snackbar.make(rootTag, response.body().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        Log.e("AddProductError", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(rootTag, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<GetProductList> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e( "onFailure: ",t.getLocalizedMessage());
                Log.e( "onFailure: ",""+t.getCause().toString());
                Snackbar.make(rootTag, "Please try again later.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }



}
