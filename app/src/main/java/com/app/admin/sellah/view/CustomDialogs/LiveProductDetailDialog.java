package com.app.admin.sellah.view.CustomDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.Categories.GetCategoriesModel;
import com.app.admin.sellah.controller.utils.ExpandableListData;
import com.app.admin.sellah.view.CustomViews.NoChangingBackgroundTextInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class LiveProductDetailDialog extends AlertDialog {

    UpdateDetailCallback callback;

    @BindView(R.id.downArrow)
    ImageView downArrow;
    @BindView(R.id.edt_add_name)
    EditText edtAddName;
    @BindView(R.id.il_name)
    NoChangingBackgroundTextInputLayout ilName;
    @BindView(R.id.edt_add_category)
    EditText edtAddCategory;
    @BindView(R.id.il_add_category)
    NoChangingBackgroundTextInputLayout ilAddCategory;
    @BindView(R.id.edt_add_price)
    EditText edtAddPrice;
    @BindView(R.id.il_add_price)
    NoChangingBackgroundTextInputLayout ilAddPrice;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.il_description)
    NoChangingBackgroundTextInputLayout ilDescription;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.filter_dialog_root)
    LinearLayout filterDialogRoot;
    @BindView(R.id.spinner_catagory)
    Spinner spinnerCatagory;
    @BindView(R.id.img_captured)
    ImageView imgCaptured;
    private ArrayList<EditText> allAddressFields;
    private ArrayList<TextInputLayout> allAddressInputLayouts;
    private HashMap<EditText, String> adressfieldsMessages;
    Context context;
    private String catId = "";
    private int CAMERA_PIC_REQUEST = 1212;
    private String imagePath = "";

    public LiveProductDetailDialog(@NonNull Context context, UpdateDetailCallback callback) {
        super(context);
        this.callback = callback;
        this.context = context;
    }

    public static LiveProductDetailDialog create(Context con, UpdateDetailCallback callback) {
        return new LiveProductDetailDialog(con, callback);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_lay_details);
        getWindow().setDimAmount(0.0f);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);

        allAddressFields = new ArrayList<>();
        allAddressFields.add(edtAddName);
//        allAddressFields.add(edtAddCategory);
        allAddressFields.add(edtDescription);

        allAddressInputLayouts = new ArrayList<>();
        allAddressInputLayouts.add(ilName);
        allAddressInputLayouts.add(ilDescription);
//        allAddressInputLayouts.add(ilAddPrice);


        adressfieldsMessages = new HashMap<>();
        adressfieldsMessages.put(edtAddName, "Please enter your product name");
        adressfieldsMessages.put(edtAddCategory, "Please add product description");


        ArrayAdapter<String> conditionSpinAdapter = new ArrayAdapter<String>
                (context, R.layout.spinner_dropdown,
                        getCategoryList());
        conditionSpinAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        spinnerCatagory.setAdapter(conditionSpinAdapter);

        spinnerCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                Log.e("category", item + " ");
                if (!item.toString().equalsIgnoreCase("category")) {
                    catId = ExpandableListData.getCatId(item.toString());
                } else {
                    catId = "";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private List<String> getCategoryList() {
        GetCategoriesModel model = ExpandableListData.getData();
        List<String> catList = new ArrayList<>();
        catList.add("Category");
        for (int i = 0; i < model.getResult().size(); i++) {
            catList.add(model.getResult().get(i).getName());
        }
        return catList;
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        if (isFormValid()) {
            if (!spinnerCatagory.getSelectedItem().toString().equalsIgnoreCase("category")) {
                if(!TextUtils.isEmpty(imagePath)){
                    callback.onUpdateVideoValues(Global.getText(edtAddName), spinnerCatagory.getSelectedItem().toString(), Global.getText(edtAddPrice), Global.getText(edtDescription), catId,imagePath);
                    dismiss();
                }else{
                    Toast.makeText(context, "Capture image for video cover", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected boolean isFormValid() {
        for (EditText editText : allAddressFields) {
            if (editText.getText().toString().trim().isEmpty()) {
                allAddressInputLayouts.get(allAddressFields.indexOf(editText))
                        .setError(adressfieldsMessages.get(editText));
//                requestFocus(editText);
                return false;
            } else {
                allAddressInputLayouts.get(allAddressFields.indexOf(editText)).setErrorEnabled(false);
            }
        }
        return true;
    }

    @OnClick(R.id.cancel_detail)
    public void onCrossClicked() {
        onBackPressed();
    }

    @OnClick(R.id.li_take_picture)
    public void onCaptureViewClicked() {
        takePhotoFromCamera();
    }

    public interface UpdateDetailCallback {
        void onUpdateVideoValues(String productName, String category, String price, String description, String catId, String imagePath);

        void onCancelDetailDialog();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callback.onCancelDetailDialog();
    }

    public void onActivityResult(int requestCode, int code, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && code==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.e("LiveProductDialog", "onActivityResult: " + extras.get("data"));
//            String imgString = Base64.encodeToString(getBytesFromBitmap(imageBitmap),
//                    Base64.NO_WRAP);
//            Log.e("ImageString", "onActivityResult: "+imgString);
            Uri tempUri = getImageUri(context.getApplicationContext(), imageBitmap);

            imagePath = getRealPathFromURI(tempUri);
            Log.e("ImageString", "onActivityResult: "+imagePath);
            imgCaptured.setPadding(0, 0, 0, 0);
            imgCaptured.setImageBitmap(imageBitmap);
//            uploadChatImage(ImageUploadHelper.convertImageTomultipart(imagePath, "image"));
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}
