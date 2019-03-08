package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.Categories.Result;
import com.app.admin.sellah.model.extra.NotificationList.ArrFollow;
import com.app.admin.sellah.model.extra.NotificationList.NotificationListModel;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.SubCategoryFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.admin.sellah.controller.utils.Global.BackstackConstants.HOMETAG;

public class NewnotificationAdapter extends RecyclerView.Adapter<NewnotificationAdapter.AllCategoryViewHolder> {

    List<ArrFollow>  newMessList;
    Context context;
    NotificationListModel notificationListModel;



    public NewnotificationAdapter(List<ArrFollow> results,NotificationListModel notificationListModel, Context activity) {

        this.newMessList = results;
        context = activity;
        this.notificationListModel = notificationListModel;


    }

    @Override
    public AllCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapterview_new, parent, false);
        AllCategoryViewHolder cvh = new AllCategoryViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final AllCategoryViewHolder holder, final int position) {

        holder.txtName.setText(newMessList.get(position).getMessage());
        holder.time.setText(newMessList.get(position).getMsgArr().getDatetime());
        if (newMessList.get(position).getReadStatus().equals("0"))
        {
            holder.read.setText("Mark as Read");
            holder.notification_rootlayout.setBackgroundColor(Color.parseColor("#fffaec"));
        }
        else
        {
            holder.read.setVisibility(View.GONE);
            holder.notification_rootlayout.setBackgroundColor(Color.parseColor("#ffffff"));

        }

        holder.read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApisHelper().readNotificationApi(context, newMessList.get(position).getNotiId(),"one", new ApisHelper.ReadNotificationCallback() {
                    @Override
                    public void onReadNotificationSuccess(String msg) {
                        notificationListModel.getArrFollow().get(position).setReadStatus("1");
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onReadNotificationFailure() {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {

        return newMessList.size();
    }

    public class AllCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtName,read,time;
        RelativeLayout notification_rootlayout;

        public AllCategoryViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txt_name_noti);
            read = view.findViewById(R.id.mark_asread_noti);
            time= view.findViewById(R.id.time_noti);
            notification_rootlayout= view.findViewById(R.id.notification_root_layout);
        }
    }

    public interface OnItemClickedListner{
        void onItemClicked();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void reloadItems(List<ArrFollow> items) {
        this.newMessList = items;
        notifyDataSetChanged();
    }

}