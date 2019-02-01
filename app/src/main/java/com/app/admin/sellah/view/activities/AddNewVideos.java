package com.app.admin.sellah.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.CustomDialogs.NewVideo_Sell_Better_bottom_Dialog;
import com.app.admin.sellah.view.adapter.NewProductAddvideo_Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.admin.sellah.controller.utils.Global.StatusBarLightMode;
import static com.app.admin.sellah.controller.utils.Global.makeTransperantStatusBar;

public class AddNewVideos extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // makeTransperantStatusBar(this, false);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_videos);
        ButterKnife.bind(this);
        settingAdapter();

        new NewVideo_Sell_Better_bottom_Dialog(this).show();

    }


    private void settingAdapter() {
        list = new ArrayList<>();
        list.add(R.drawable.image);
        list.add(R.drawable.image);
        list.add(R.drawable.image);
        list.add(R.drawable.image);
        NewProductAddvideo_Adapter newProductAdapter = new NewProductAddvideo_Adapter(list);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setAdapter(newProductAdapter);
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

                break;
            case R.id.addmore_photos_btn:

                break;
            case R.id.addnewvideo_btn_nxt:

                Intent intent = new Intent(AddNewVideos.this,AddNewInfo.class);
                startActivity(intent);
                break;
        }
    }
}
