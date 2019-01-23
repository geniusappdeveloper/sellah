package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
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

    public NewnotificationAdapter(List<ArrFollow> results, Context activity) {

        this.newMessList = results;
        context = activity;


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
        }
        else
        {
            holder.read.setText("");
        }
    }

    @Override
    public int getItemCount() {

        return newMessList.size();
    }

    public class AllCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtName,read,time;

        public AllCategoryViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txt_name_noti);
            read = view.findViewById(R.id.mark_asread_noti);
            time= view.findViewById(R.id.time_noti);
        }
    }

    public interface OnItemClickedListner{
        void onItemClicked();
    }

}