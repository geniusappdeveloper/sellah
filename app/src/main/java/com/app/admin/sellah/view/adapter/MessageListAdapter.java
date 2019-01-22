package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.ChatHeadermodel.ChattedListModel;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.ChatHeadermodel.Record;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.fragments.PersonalProfileFragment;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessagesViewHolder> {

    private List<Record> chattedListModel;
    Context context;
    FragmentManager manager;
    int state;
//    private ChattedListModel notificationListFilter;

    public MessageListAdapter(List<Record> chattedListModel, Activity activity, int state, FragmentManager manager) {

        this.chattedListModel = chattedListModel;
//        notificationListFilter = chattedListModel;
        context = activity;
        this.state = state;
        this.manager = manager;
        Log.e("adapter_state", "working");

    }

    public void filterList(List<Record> newMessList) {
        this.chattedListModel = newMessList;
        notifyDataSetChanged();
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_mess_adapter, parent, false);
        MessagesViewHolder cvh = new MessagesViewHolder(groceryProductView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final MessagesViewHolder holder, final int position) {
//        holder.imageView.setImageResource(chattedListModel.get(position).getProfileImage());
        holder.imgNew.setVisibility(View.GONE);
        Glide.with(context)
                .load(chattedListModel.get(position).getFriendImage())
                .apply(Global.getGlideOptions()).into(holder.imageView);

        holder.headingText.setText(!chattedListModel.get(position).getFriendName().equalsIgnoreCase("") ?
                chattedListModel.get(position).getFriendName() : "Sellah! user");
        holder.subHeadingText.setText(chattedListModel.get(position).getMessage());
        holder.msgTime.setText(Global.formateDateTo_HHmm(Global.convertUTCToLocal(chattedListModel.get(position).getLastMsgTime())));

        if (state == 0) {
            if (position == 0) {
                holder.order.setVisibility(View.VISIBLE);
            } else {
                holder.order.setVisibility(View.GONE);
            }

        } else {

            holder.order.setVisibility(View.GONE);
        }

        /*if(chattedListModel.get(position).getIsRead().equalsIgnoreCase("Y")){
            holder.imgNew.setVisibility(View.VISIBLE);
        }else{

        }*/
        holder.relDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(chattedListModel.get(position).getIsBlocked())&&chattedListModel.get(position).getIsBlocked().equalsIgnoreCase("y")) {

                    if(!TextUtils.isEmpty(chattedListModel.get(position).getBlockedBy())&&chattedListModel.get(position).getBlockedBy().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))){
//                        you have blocked user
                       S_Dialogs.getYouBlockedUserDialog(context,context.getString(R.string.dialog_title_you_block_user),(dialog,which)->{
                           blockUnBlockUser("unblock",position,chattedListModel.get(position).getFriendId());
                           dialog.dismiss();
                       }).show();
                    }else{
                        S_Dialogs.getUserBlockedYouDialog(context).show();
//                        user has blocked you.
                    }

                }else{
                    Intent chatIntent = new Intent(context, ChatActivity.class);
                    chatIntent.putExtra("otherUserId", chattedListModel.get(position).getFriendId());
                    chatIntent.putExtra("otherUserImage", chattedListModel.get(position).getFriendImage());
                    chatIntent.putExtra("otherUserName", chattedListModel.get(position).getFriendName());
                    context.startActivity(chatIntent);
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(chattedListModel.get(position).getIsBlocked())&&chattedListModel.get(position).getIsBlocked().equalsIgnoreCase("y")) {

                    if(!TextUtils.isEmpty(chattedListModel.get(position).getBlockedBy())&&chattedListModel.get(position).getBlockedBy().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))){
//                        you have blocked user
                        S_Dialogs.getYouBlockedUserDialog(context,context.getString(R.string.dialog_title_you_block_user),(dialog,which)->{
                            blockUnBlockUser("unblock",position,chattedListModel.get(position).getFriendId());
                            dialog.dismiss();
                        }).show();
                    }else{
                        S_Dialogs.getUserBlockedYouDialog(context).show();
//                        user has blocked you.
                    }

                }else{
                    loadFragment(new PersonalProfileFragment(), position);
                }

            }
        });

    }
    private void blockUnBlockUser(String blockStatus, int pos, String userId) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> manageBlockUserCall = Global.WebServiceConstants.getRetrofitinstance().manageBlockListApi(HelperPreferences.get(context).getString(UID), userId, blockStatus);
        manageBlockUserCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        Toast.makeText(context,  response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        chattedListModel.get(pos).setIsBlocked("N");
                        notifyDataSetChanged();
                    }
                } else {

                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("bmanageUser", "onFailure: " + t.getMessage());
            /*    Snackbar.make(rootMbu, "Please try again latter.", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();*/
                Toast.makeText(context,  "Please try again latter.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chattedListModel.size();
    }


    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView headingText, subHeadingText, order, msgTime;
        ImageView imgNew;
        RelativeLayout relDetails;

        public MessagesViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.profileimage);
            headingText = view.findViewById(R.id.personHeading);
            subHeadingText = view.findViewById(R.id.personSubHeading);
            order = view.findViewById(R.id.textOrder);
            msgTime = view.findViewById(R.id.txt_time);
            imgNew = view.findViewById(R.id.img_new_entry);
            relDetails = view.findViewById(R.id.rel_details);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
//                listener.onNotificationSelected(notificationListFilter.get(getAdapterPosition()));
                }
            });

        }
    }

    public boolean loadFragment(Fragment fragment, int pos) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(SAConstants.Keys.OTHER_USER_ID, chattedListModel.get(pos).getFriendId());
            fragment.setArguments(bundle);
            manager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
            return true;
        }
        return false;
    }
}
