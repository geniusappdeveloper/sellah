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

public class AddNewTransaction extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarLightMode(this);
        setContentView(R.layout.activity_add_new_transaction);



    }

}
