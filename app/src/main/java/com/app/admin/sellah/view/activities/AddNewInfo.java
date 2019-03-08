package com.app.admin.sellah.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SellProductInterface;
import com.app.admin.sellah.controller.utils.Validations;
import com.app.admin.sellah.model.AddProductDatabase;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.view.CustomDialogs.Add_New_Product_tutorial_secondDialog;
import com.app.admin.sellah.view.adapter.AddProductTagsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class AddNewInfo extends AppCompatActivity implements SellProductInterface {


    @BindView(R.id.add_info_post)
    Button addInfoPost;
    @BindView(R.id.edt_tags)
    EditText edtTags;
    @BindView(R.id.addnewvideo_toolbar)
    RelativeLayout addnewvideoToolbar;
    @BindView(R.id.thumbnail_video)
    ImageView thumbnailVideo;
    @BindView(R.id.upload_video)
    CardView uploadVideo;
    @BindView(R.id.add_product_car_recycler)
    RecyclerView addProductCarRecycler;
    @BindView(R.id.add_photo)
    CardView addPhoto;
    @BindView(R.id.edt_product_name)
    EditText edtProductName;
    @BindView(R.id.sub_cat_img)
    ImageView subCatImg;
    @BindView(R.id.spinner_sub_catagory)
    Spinner spinnerSubCatagory;
    @BindView(R.id.sub_cat_rl)
    RelativeLayout subCatRl;
    @BindView(R.id.edt_price)
    EditText edtPrice;

    @BindView(R.id.spin_fixed_price)
    Spinner spinFixedPrice;
    @BindView(R.id.fix_img)
    ImageView fixImg;
    @BindView(R.id.fixed_rl)
    RelativeLayout fixedRl;
    @BindView(R.id.condition_img)
    ImageView conditionImg;
    @BindView(R.id.spin_condition)
    Spinner spinCondition;
    @BindView(R.id.condition_rl)
    RelativeLayout conditionRl;
    @BindView(R.id.edt_quantity)
    EditText edtQuantity;
    @BindView(R.id.add_product_tags_recycler)
    RecyclerView addProductTagsRecycler;
    @BindView(R.id.edtDescription)
    EditText edtDescription;
    @BindView(R.id.spinner_city)
    TextView spinnerCity;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.rl5)
    RelativeLayout rl5;
    @BindView(R.id.edt_budget)
    EditText edtBudget;
    @BindView(R.id.spin_number_of)
    Spinner spinNumberOf;
    @BindView(R.id.txt_no_of_clicks)
    TextView txtNoOfClicks;
    @BindView(R.id.rootTag)
    LinearLayout rootTag;
    @BindView(R.id.add_tags)
    ImageView addTags;
    @BindView(R.id.addnewinfo_back)
    ImageView addnewinfoBack;
    @BindView(R.id.horizontal_infoview)
    HorizontalScrollView horizontalInfoview;
    private ArrayList<String> subCategory;
    @BindView(R.id.cat_img)
    ImageView catImg;
    @BindView(R.id.spinner_catagoryinfo)
    Spinner spinnerCatagory;
    @BindView(R.id.cat_rl)
    RelativeLayout catRl;
    String subCatId, tag="";
    ArrayList<String> tagList = new ArrayList<>();
    AddProductTagsAdapter addProductTagsAdapter;

    TextWatcher priceTextWacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // makeTransperantStatusBar(this, false);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_product_info);
        ButterKnife.bind(this);

        spinneradapters();
        focuslisteneres();
        setTextWacher();
        setUpTagLayoutAdapter();
            getdata();



    }


    public void spinneradapters() {


        GetCategoriesModel model = ExpandableListData.getData();
        List<String> categoryList = new ArrayList<>();
        HashMap<String, ArrayList<String>> subCategoryList = new HashMap<>();
        categoryList.add("Select Category");
        subCategory = new ArrayList<>();
        subCategory.clear();
        subCategory.add("Select Sub-Category");


        try {
            for (int i = 0; i < model.getResult().size(); i++) {
                ArrayList<String> subCategories = new ArrayList<>();
                ArrayList<String> categoriesMain = new ArrayList<>();
                Log.e("categoryDataNav", model.getResult().get(i).getName());

                subCategories.add("Select Sub-Category");
                for (int j = 0; j < model.getResult().get(i).getSubcategories().size(); j++) {
                    Log.e("  SubcategoryDataNav", model.getResult().get(i).getSubcategories().get(j).getName());
                    subCategories.add(model.getResult().get(i).getSubcategories().get(j).getName());
                }
                categoryList.add(model.getResult().get(i).getName());
                Log.e("spinneradapters: ", "" + categoryList);
                subCategoryList.put(model.getResult().get(i).getName(), subCategories);
            }
        } catch (Exception e) {

        }
        ArrayAdapter<String> categorySpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        categoryList);
        categorySpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerCatagory.setAdapter(categorySpinAdapter);

        /*--------------------------------------------------------------------------------------------*/

        spinnerCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                remove_focus();
                Log.e("onItemSelected: ", "1");

                if (!item.toString().equalsIgnoreCase("Select Category")) {
                    Log.e("category1", item + " ");
                    AddProductDatabase.catid = model.getResult().get(position - 1).getCatId();
                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.BLACK);
                    catRl.setBackgroundResource(R.drawable.live_product_detail_background);
                    catImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        catImg.setImageTintList(null);
                    }
                    try {
                        subCategory.clear();
                        subCategory.addAll(subCategoryList.get(item));
                        subCategory.remove(1);
                        spinnerSubCatagory.getAdapter().notify();
                    } catch (Exception e) {

                    }

                } else {
                    catRl.setBackgroundResource(R.drawable.live_product_detail_grey_background);
                    catImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                    subCategory.clear();
                    subCategory.add("Select Sub-Category");

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//      *************************************Sub-Category Spinner adapter*******************************


        //      *************************************price Method Spinner adapter*******************************
        ArrayAdapter<String> priceModeSpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.add_product_fixed));
        priceModeSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinFixedPrice.setAdapter(priceModeSpinAdapter);

        spinFixedPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                remove_focus();
                if (!item.toString().equalsIgnoreCase("Select Type")) {

                    ((TextView) spinFixedPrice.getSelectedView()).setTextColor(Color.BLACK);
                    fixedRl.setBackgroundResource(R.drawable.live_product_detail_background);
                    fixImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));

                } else {

                    ((TextView) spinFixedPrice.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                    fixImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //      *************************************Condition Spinner adapter*******************************
        ArrayAdapter<String> conditionSpinAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        getResources().getStringArray(R.array.add_product_condition));
        conditionSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinCondition.setAdapter(conditionSpinAdapter);

        spinCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                remove_focus();
                if (!item.toString().equalsIgnoreCase("Select Condition")) {

                    ((TextView) spinCondition.getSelectedView()).setTextColor(Color.BLACK);
                    conditionRl.setBackgroundResource(R.drawable.live_product_detail_background);
                    conditionImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));

                } else {

                    ((TextView) spinCondition.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                    conditionImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //sub category==========================

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        subCategory);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        if (!subCategory.isEmpty()) {
            spinnerSubCatagory.setAdapter(spinnerArrayAdapter);

            spinnerSubCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    remove_focus();
                    if (!item.toString().equalsIgnoreCase("Select Sub-Category")) {
                        AddProductDatabase.subcatid = model.getResult().get(spinnerCatagory.getSelectedItemPosition() - 1).getSubcategories().get(position).getId();
                        ((TextView) spinnerSubCatagory.getSelectedView()).setTextColor(Color.BLACK);
                        subCatRl.setBackgroundResource(R.drawable.live_product_detail_background);
                        subCatImg.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                        // subCatId = model.getResult().get(spinnerCatagory.getSelectedItemPosition() - 1).getSubcategories().get(position).getId();
                        Log.e("SubCat_id", "onItemSelected: " + subCatId);

                    } else {
                        AddProductDatabase.subcatid  = "";
                        ((TextView) spinnerSubCatagory.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));
                        subCatImg.setImageDrawable(getResources().getDrawable(R.drawable.down_grey));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


    }




    @OnClick({R.id.cat_rl, R.id.sub_cat_rl, R.id.fixed_rl, R.id.condition_rl, R.id.addnewinfo_back, R.id.add_tags, R.id.add_info_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cat_rl:
                remove_focus();
                break;
            case R.id.sub_cat_rl:
                remove_focus();
                break;
            case R.id.fixed_rl:
                remove_focus();
                break;
            case R.id.condition_rl:
                break;

            case R.id.addnewinfo_back:
                    onBackPressed();
                break;
            case R.id.add_tags:
                if (tagList.size() != 3) {
                    Global.addTag(this, (dialog, input) -> {
                        if (TextUtils.isEmpty(input)) {
                            dialog.dismiss();

                        } else {
                            tag = input.toString().trim();
                            tagList.add(tag);
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    Snackbar.make(rootTag, "Only three tags are allowed per item.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }

                break;
            case R.id.add_info_post:
                adddata();
                boolean validations = new Validations().productinfo_validate(AddNewInfo.this,edtProductName.getText().toString(),spinnerCatagory.getSelectedItem().toString(),spinnerSubCatagory.getSelectedItem().toString(),edtPrice.getText().toString(),spinFixedPrice.getSelectedItem().toString(),spinCondition.getSelectedItem().toString(),edtQuantity.getText().toString());
                if (validations) {


                    Intent intent = new Intent(AddNewInfo.this, AddNewTransaction.class);
                    startActivity(intent);

                }

                break;
        }
    }

    private void setUpTagLayoutAdapter() {

        //tags set Adapter
        addProductTagsAdapter = new AddProductTagsAdapter(tagList, this, this);
        LinearLayoutManager tagsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        addProductTagsRecycler.setLayoutManager(tagsLayoutManager);
        addProductTagsRecycler.setAdapter(addProductTagsAdapter);
        addProductTagsAdapter.notifyDataSetChanged();
//        add_product_cars_adapter.notifyDataSetChanged();

    }

    @Override
    public void setTagVisiblity(boolean visible) {
        if (visible) {
            addTags.setVisibility(View.VISIBLE);
            horizontalInfoview.setBackgroundResource(R.drawable.live_product_detail_background);

        } else {
            addTags.setVisibility(View.GONE);
            horizontalInfoview.setBackgroundResource(R.drawable.live_product_detail_grey_background);

        }
    }

    @Override
    public void setImageCaptureVisiblty(boolean visible) {
        if (visible) {
            addPhoto.setVisibility(View.VISIBLE);
        } else {
            addPhoto.setVisibility(View.GONE);
        }
    }


    public void remove_focus() {
        edtDescription.clearFocus();
        edtQuantity.clearFocus();
        edtPrice.clearFocus();
        edtProductName.clearFocus();
        if (TextUtils.isEmpty(edtDescription.getText().toString())) {
            edtDescription.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }

        if (TextUtils.isEmpty(edtQuantity.getText().toString())) {
            edtQuantity.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }

        if (TextUtils.isEmpty(edtPrice.getText().toString())) {
            edtPrice.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }

        if (TextUtils.isEmpty(edtProductName.getText().toString())) {
            edtProductName.setBackgroundResource(R.drawable.live_product_detail_grey_background);
        }
    }

    private void focuslisteneres() {

        edtTags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Add_New_Product_tutorial_secondDialog dialog = new Add_New_Product_tutorial_secondDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("x", (int) edtTags.getX());
                    bundle.putInt("y", (int) edtTags.getY());
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "");
                    edtTags.setVisibility(View.GONE);
                    addTags.setVisibility(View.VISIBLE);

                }
            }
        });


        edtProductName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edtProductName.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    edtProductName.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });

        edtQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edtQuantity.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    edtQuantity.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });

        edtPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edtPrice.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    edtPrice.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });


        edtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edtDescription.setBackgroundResource(R.drawable.live_product_detail_red_background);


                } else {
                    edtDescription.setBackgroundResource(R.drawable.live_product_detail_background);
                }
            }
        });

    }


    private void adddata() {



                 AddProductDatabase data = new AddProductDatabase();

                 data.tagListG.clear();
                 data.tagListG.addAll(tagList);
                 if (!edtProductName.getText().toString().trim().isEmpty())

                data.name = edtProductName.getText().toString().trim();


                if (!spinnerCatagory.getSelectedItem().toString().isEmpty())
                    data.category = spinnerCatagory.getSelectedItemPosition();

        if (!spinnerSubCatagory.getSelectedItem().toString().isEmpty())
            data.sub_category = spinnerSubCatagory.getSelectedItemPosition();


                if (!edtPrice.getText().toString().isEmpty())
                    data.price = edtPrice.getText().toString();

                if (!spinFixedPrice.getSelectedItem().toString().isEmpty())
                    data.type = spinFixedPrice.getSelectedItemPosition();

                if (!spinCondition.getSelectedItem().toString().isEmpty())
                    data.condition = spinCondition.getSelectedItemPosition();

                if (!edtQuantity.getText().toString().isEmpty())
                    data.quantity = edtQuantity.getText().toString();

                if (!tag.isEmpty())
                    data.tags = tag;
                if (!edtDescription.getText().toString().isEmpty())
                    data.description = edtDescription.getText().toString();


    }


    private void getdata() {




        if (!AddProductDatabase.name.isEmpty()) {
            edtProductName.setText(AddProductDatabase.name);


            if (AddProductDatabase.category>=0)
                spinnerCatagory.setSelection(AddProductDatabase.category);


            if (AddProductDatabase.sub_category>=0)
                spinnerSubCatagory.setSelection(AddProductDatabase.sub_category);

            if (!AddProductDatabase.price.isEmpty())
                 edtPrice.setText(AddProductDatabase.price);

            if (AddProductDatabase.type>=0)
                 spinFixedPrice.setSelection(AddProductDatabase.type);

            if (AddProductDatabase.condition>=0)
                 spinCondition.setSelection(AddProductDatabase.condition);

            if (!AddProductDatabase.quantity.isEmpty())
                 edtQuantity.setText(AddProductDatabase.quantity);

            if (!AddProductDatabase.tags.isEmpty())
                 tag=AddProductDatabase.tags;
            if (!AddProductDatabase.description.isEmpty())
                 edtDescription.setText(AddProductDatabase.description);


        }



    }

    private void setTextWacher() {

        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = edtPrice.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 3, 2);

                if (!str2.equals(str)) {
                    edtPrice.setText(str2);
                    int pos = edtPrice.getText().length();
                    edtPrice.setSelection(pos);
                }

            }
        });


           /* checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isPromotingProduct = "Y";
                    } else {
                        isPromotingProduct = "N";
                    }
                }
            });*/
    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
    }



}
