package com.app.admin.sellah.view.CustomDialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.NotificationList.ArrFollow;
import com.app.admin.sellah.model.extra.NotificationList.NotificationListModel;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.NewnotificationAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class Notification_dialog extends DialogFragment {

    NotificationListModel notificationListModel;
    NotificationListModel notificationListModel1;
    List<ArrFollow> newMessList;
    List<ArrFollow> global_newwmesslist;
    WebService service;
    View view;
    @BindView(R.id.notifcation_cancel)
    ImageView notifcationCancel;
    @BindView(R.id.downArrow)
    ImageView downArrow;
    @BindView(R.id.notification_rv)
    RecyclerView notificationRv;
    @BindView(R.id.filter_dialog_root)
    LinearLayout filterDialogRoot;
    Unbinder unbinder;
    Dialog dialog;
    NewnotificationAdapter notificationFollowListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_new_notification_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        service = Global.WebServiceConstants.getRetrofitinstance();


        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle!=null)
        {
            notificationListModel = bundle.getParcelable(SAConstants.Keys.NOTI_KEY);
            global_newwmesslist = notificationListModel.getArrFollow();
             notificationFollowListAdapter = new NewnotificationAdapter(global_newwmesslist,notificationListModel, getActivity() );
            LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            notificationRv.setLayoutManager(horizontalLayoutManager1);
            notificationRv.setAdapter(notificationFollowListAdapter);
        }

        else
        {
            getNotificationList();
        }





        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
       // dialog = S_Dialogs.getLoadingDialog(getActivity());
      //  dialog.show();
    }

    @OnClick(R.id.notifcation_cancel)
    public void onViewClicked() {
        dismiss();
    }

    private void getNotificationList() {

        Call<NotificationListModel> notificationListCall = service.getNotificationList(HelperPreferences.get(getActivity()).getString(UID));
        notificationListCall.enqueue(new Callback<NotificationListModel>() {
            @Override
            public void onResponse(Call<NotificationListModel> call, Response<NotificationListModel> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {

                    Log.e("GetNotificationList", "onResponse: " + response.body().getStatus());
                    notificationListModel1 = response.body();

                    newMessList = notificationListModel1.getArrFollow();
                    global_newwmesslist.addAll(notificationListModel1.getArrFollow());
                    notificationFollowListAdapter = new NewnotificationAdapter(global_newwmesslist,notificationListModel1, getActivity() );
                    LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    notificationRv.setLayoutManager(horizontalLayoutManager1);
                    notificationRv.setAdapter(notificationFollowListAdapter);
                    if (notificationListModel.getListReadStatus().equals("0"))
                    {
                        ((MainActivity)getActivity()).findViewById(R.id.home_notidot).setVisibility(View.GONE);
                    }
                    else
                    {
                        ((MainActivity)getActivity()).findViewById(R.id.home_notidot).setVisibility(View.VISIBLE);
                    }



                } else {
                    try {
                        Log.e("GetNotificationList", "onResponse: errorBody" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationListModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("GetNotificationList", "onFailure: " + t.getMessage());
            }
        });
    }

}
