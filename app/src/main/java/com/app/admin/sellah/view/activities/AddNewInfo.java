package com.app.admin.sellah.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SellProductInterface;
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
    @BindView(R.id.rl6)
    RelativeLayout rl6;
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
    @BindView(R.id.quantity_rl)
    RelativeLayout quantityRl;
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
    private ArrayList<String> subCategory= new ArrayList<>();;
    @BindView(R.id.cat_img)
    ImageView catImg;
    @BindView(R.id.spinner_catagory)
    Spinner spinnerCatagory;
    @BindView(R.id.cat_rl)
    RelativeLayout catRl;
    String subCatId,tag;
    ArrayList<String> tagList = new ArrayList<>();
    AddProductTagsAdapter addProductTagsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // makeTransperantStatusBar(this, false);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_product_info);
        ButterKnife.bind(this);

        spinneradapters();
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


    }


    public void spinneradapters() {


        GetCategoriesModel model = ExpandableListData.getData();
        List<String> categoryList = new ArrayList<>();
        HashMap<String, ArrayList<String>> subCategoryList = new HashMap<>();
        categoryList.add("Select Category");
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
                Log.e( "spinneradapters: ",""+ categoryList);
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

                Log.e("onItemSelected: ", "1");
                ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.GRAY);
                if (!item.toString().equalsIgnoreCase("Select Category")) {
                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.BLACK);
                    try {
                        subCategory = new ArrayList<>();
                        subCategory.addAll(subCategoryList.get(item));
                        subCategory.remove(1);
                    } catch (Exception e) {

                    }

                } else {
                    Log.e("onItemSelected: ", "2");
                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.GRAY);

                  //  subCategory = new ArrayList<>();
                  //  subCategory.add("Select Sub-Category");
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Log.e("spinneradapters: ","f"+subCategory );

//      *************************************Sub-Category Spinner adapter*******************************
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_dropdown,
                        subCategory);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        if (!subCategory.isEmpty())
        {
            spinnerSubCatagory.setAdapter(spinnerArrayAdapter);

            spinnerSubCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    Log.e("Subcategory", item + " ");
                    if (!item.toString().equalsIgnoreCase("Select Sub-Category")) {

                        ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.BLACK);
                        subCatId = model.getResult().get(spinnerCatagory.getSelectedItemPosition() - 1).getSubcategories().get(position).getId();
                        Log.e("SubCat_id", "onItemSelected: " + subCatId);

                    } else {
                        subCatId = "";
                        ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }






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

                if (!item.toString().equalsIgnoreCase("Select Type")) {

                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.BLACK);

                } else {

                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));

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

                if (!item.toString().equalsIgnoreCase("Select Type")) {

                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.BLACK);

                } else {

                    ((TextView) spinnerCatagory.getSelectedView()).setTextColor(Color.parseColor("#c9c9c9"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @OnClick({R.id.addnewinfo_back, R.id.add_tags, R.id.add_info_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                Intent intent = new Intent(AddNewInfo.this, AddNewTransaction.class);
                startActivity(intent);
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
        } else {
            addTags.setVisibility(View.GONE);
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
}
