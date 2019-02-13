package com.app.admin.sellah.view.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SellProductInterface;
import com.app.admin.sellah.model.AddProductDatabase;
import com.app.admin.sellah.view.CustomDialogs.NewVideo_Sell_Better_bottom_Dialog;
import com.app.admin.sellah.view.adapter.Add_Product_Cars_Adapter;
import com.app.admin.sellah.view.adapter.NewProductAddvideo_Adapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerActivity;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import static android.os.Build.VERSION_CODES.M;
import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;

public class AddNewVideos extends AppCompatActivity implements SellProductInterface {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    List<Integer> list;
    @BindView(R.id.addnewvide_back)
    ImageView addnewvideBack;
    @BindView(R.id.addnewvideo_toolbar)
    RelativeLayout addnewvideoToolbar;
    @BindView(R.id.dummylive1_text)
    TextView dummylive1Text;
    @BindView(R.id.dummylive2_text)
    TextView dummylive2Text;
    @BindView(R.id.rl_addnewvideo)
    RelativeLayout rlAddnewvideo;
    @BindView(R.id.addmore_photos_btn)
    Button addmorePhotosBtn;
    @BindView(R.id.addnewvideo_btn_nxt)
    Button addnewvideoBtnNxt;
    final int CAMERA_PIC_REQUEST = 1313;
    @BindView(R.id.thumbnail_video)
    ImageView thumbnailVideo;
    @BindView(R.id.dummylive2_text1)
    TextView dummylive2Text1;
    @BindView(R.id.rl_addnewvideo1)
    RelativeLayout rlAddnewvideo1;
    @BindView(R.id.plus)
    TextView plus;
    private int GALLERY = 1212;
    List<String> imageList = new ArrayList<>();
    NewProductAddvideo_Adapter newProductAdapter;
    Add_Product_Cars_Adapter add_product_cars_adapter;
    ArrayList<String> filepaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // makeTransperantStatusBar(this, false);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_videos);
        ButterKnife.bind(this);
        new NewVideo_Sell_Better_bottom_Dialog(this).show();

    }


    @OnClick({R.id.addnewvide_back, R.id.rl_addnewvideo, R.id.addmore_photos_btn, R.id.addnewvideo_btn_nxt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addnewvide_back:
                onBackPressed();
                break;
            case R.id.rl_addnewvideo:
                dummylive1Text.setVisibility(View.GONE);
                dummylive2Text.setVisibility(View.GONE);
                rlAddnewvideo.setBackgroundResource(R.drawable.live_product_detail_background);
                recycler.setVisibility(View.VISIBLE);
                addmorePhotosBtn.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= M) {
                    if (ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        showPictureDialog();

                    } else {

                        ActivityCompat.requestPermissions(AddNewVideos.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                    }
                } else {
                    showPictureDialog();
                }

                break;
            case R.id.addmore_photos_btn:
                if (Build.VERSION.SDK_INT >= M) {
                    if (ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(AddNewVideos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        showPictureDialog();

                    } else {

                        ActivityCompat.requestPermissions(AddNewVideos.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                    }
                } else {
                    showPictureDialog();
                }
                break;
            case R.id.addnewvideo_btn_nxt:
                AddProductDatabase.imageListG.clear();
                AddProductDatabase.imageListG.addAll(filepaths);
                if (filepaths.isEmpty())
                {
                    Toast.makeText(this, "Please select atleast one image!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(AddNewVideos.this, AddNewInfo.class);
                    startActivity(intent

                    );
                }

                break;
        }


    }

    private void showPictureDialog() {


       new FilePickerBuilder().getInstance().setMaxCount(5)
                .setSelectedFiles(filepaths)
                .setActivityTheme(R.style.LibAppTheme)
                .pickPhoto(this);


     /*   AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Add from your gallery",
                "Take a photo"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();

                                break;
                        }
                    }

                });
        pictureDialog.show();*/
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }


    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(galleryIntent, GALLERY);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    filepaths = new ArrayList<>();
                    filepaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                    add_product_cars_adapter = new Add_Product_Cars_Adapter(this, filepaths, this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recycler.setLayoutManager(layoutManager);
                    recycler.setAdapter(add_product_cars_adapter);
                    add_product_cars_adapter.notifyDataSetChanged();
                }
                break;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    Uri tempUri = getImageUri(this, bitmap);
//                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                    imageList.add(getRealPathFromURI(tempUri));


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri tempUri = getImageUri(this, imageBitmap);

//            Toast.makeText(getActivity(), "Here " + getRealPathFromURI(tempUri), Toast.LENGTH_LONG).show();

            imageList.add(getRealPathFromURI(tempUri));


            add_product_cars_adapter = new Add_Product_Cars_Adapter(this, imageList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(add_product_cars_adapter);
            add_product_cars_adapter.notifyDataSetChanged();

        }
    }


    @Override
    public void setTagVisiblity(boolean visible) {

    }

    @Override
    public void setImageCaptureVisiblty(boolean visible) {

    }

    @OnClick(R.id.rl_addnewvideo1)
    public void onViewClicked() {

        Intent intent = new Intent(AddNewVideos.this, Video_capture_activity.class);
        startActivity(intent);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.videopath.equals("no_image")) {

            thumbnailVideo.setVisibility(View.GONE);
            rlAddnewvideo1.setVisibility(View.VISIBLE);
            plus.setVisibility(View.VISIBLE);
            dummylive2Text1.setVisibility(View.VISIBLE);

        }
        else {

            thumbnailVideo.setVisibility(View.VISIBLE);
            rlAddnewvideo1.setVisibility(View.VISIBLE);
            plus.setVisibility(View.GONE);
            dummylive2Text1.setVisibility(View.GONE);
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(Global.videopath, MediaStore.Images.Thumbnails.MICRO_KIND);
            thumbnailVideo.setImageBitmap(thumb);
            thumbnailVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);


        }
    }
}
