package com.app.admin.sellah.view.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.OnSwipeTouchListener;
import com.app.admin.sellah.model.extra.ChatHeadermodel.ChattedListModel;
import com.app.admin.sellah.model.extra.ChatHeadermodel.Record;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.model.extra.SwipeHelper;
import com.app.admin.sellah.view.CustomDialogs.ReportUserDialog;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.adapter.MessageListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_ACCEPT_REJECT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_CHAT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_COMMENT_ADDED;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_FOLLOW;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_OFFER_MAKE;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PAYMENT;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_PRODUCT_ADDED;

public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //Mess recycler
    TextView txtNewMessage, txtMessage;

    RecyclerView messRecycler, ongoingrecycler;

    //New Mess Recycler
    RecyclerView newMessRecycler;
    TextWatcher searchWatcher;
    @BindView(R.id.rel_root_noti)
    RelativeLayout relRootNoti;
    Unbinder unbinder;
    private View view;
    private MessageListAdapter oldMsgAdapter;
    private MessageListAdapter newMsgAdapter;
    private MessageListAdapter ongoingadapter;
    private ChattedListModel oldMsgList;
    private String TAG = MessageFragment.class.getSimpleName();
    CardView actionButton;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        messRecycler = view.findViewById(R.id.message_recycler);
        messRecycler = view.findViewById(R.id.message_recycler);
        newMessRecycler = view.findViewById(R.id.new_message_recycler);
        ongoingrecycler = view.findViewById(R.id.ongoing_transactionrecyclerview);
        txtMessage = view.findViewById(R.id.txt_message);
        txtNewMessage = view.findViewById(R.id.txt_new_message);
        actionButton = view.findViewById(R.id.floating_action_button);
        onCickListners();
        //Notification New Message List
       /* notificationNewMessList();
        notificationFollowListAdapter = new NotificationFollowListAdapter(notificationNewMessList, getActivity(), 0);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        newMessRecycler.setLayoutManager(horizontalLayoutManager1);
        newMessRecycler.setAdapter(notificationFollowListAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_new_mess_adapter, 10);
        new SwipeHelper(getActivity(), newMessRecycler) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#E35252"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Report",
                        0,
                        Color.parseColor("#EEC12D"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));
            }
        };*/
        //Notification Message List
//        notificationMessList();


        searchWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    boolean isNewMessage = filterNewMessages(s.toString());
//                Global.getTotalHeightofLinearRecyclerView(getActivity(), rVNewFollow, R.layout.notification_new_mess_adapter, 10);
                    boolean isMessage = filterMessages(s.toString());
//                Global.getTotalHeightofLinearRecyclerView(getActivity(), rVAnnouncments, R.layout.notification_new_mess_adapter, 10);
                } catch (Exception e) {
                }
            }
        };

       /* actionButton.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Log.e( "onSwipeLeft: ","w" );
                getActivity().onBackPressed();
            }
        });*/


        ongoingadapter = new MessageListAdapter(getActivity(), 3);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ongoingrecycler.setLayoutManager(horizontalLayoutManager);
        ongoingrecycler.setAdapter(ongoingadapter);

        getChatedListApi();



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            getChatedListApi();
        }
    }

    private boolean filter(String text) {
        //new array list that will hold the filtered data
        /*ChattedListModel filterModel=new ChattedListModel();
        List<Record> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationNewMessList) {

            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        notificationFollowListAdapter.filterList(filteredList);
        if (notificationFollowListAdapter.getItemCount() != 0) {
            txtNewMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtNewMessage.setVisibility(View.GONE);
            return true;
        }
        return false;*/
        return false;
    }

    private boolean filterNewMessages(String text) {

        ChattedListModel filterModel = new ChattedListModel();
        List<Record> filteredList = new ArrayList<>();
        for (Record row : oldMsgList.getRecord()) {
            // name match condition. this might differ depending on your requirement
            if (row.getFriendName().toLowerCase().contains(text.toLowerCase()) && !row.getIsRead().equalsIgnoreCase("Y")) {
                filteredList.add(row);
                Log.e(TAG, "filterMessages: found");
            } else {
                Log.e(TAG, "filterMessages: Not Found");
            }
        }
//        filterModel.setRecord(filteredList);

        if (text.length() > 0) {
            newMsgAdapter.filterList(filteredList);
        } else {
            if (oldMsgList != null) {
                filteredList.clear();
                for (Record row : oldMsgList.getRecord()) {
                    // name match condition. this might differ depending on your requirement
                    if (!row.getIsRead().equalsIgnoreCase("Y")) {
                        filteredList.add(row);
                        Log.e(TAG, "filterMessages: found");
                    } else {
                        Log.e(TAG, "filterMessages: Not Found");
                    }
                }
                newMsgAdapter.filterList(filteredList);
            }

        }
        //calling a method of the adapter class and passing the filtered list
        if (newMsgAdapter.getItemCount() != 0) {
            txtNewMessage.setVisibility(View.VISIBLE);
            ((MainActivity)getActivity()).notificationImg.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtNewMessage.setVisibility(View.GONE);
            ((MainActivity)getActivity()).notificationImg.setVisibility(View.GONE);
            return true;
        }
        //new array list that will hold the filtered data
       /* List<NotificationMessModel> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationMessList) {

            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        notificationMessAdapter.filterList(filteredList);
        if (notificationMessAdapter.getItemCount() != 0) {
            txtMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtMessage.setVisibility(View.GONE);
            return true;
        }*/
//       return false;
    }

    private boolean filterMessages(String text) {

        ChattedListModel filterModel = new ChattedListModel();
        List<Record> filteredList = new ArrayList<>();
        for (Record row : oldMsgList.getRecord()) {
            // name match condition. this might differ depending on your requirement
            if (row.getFriendName().toLowerCase().contains(text.toLowerCase()) && row.getIsRead().equalsIgnoreCase("Y")) {
                filteredList.add(row);
                Log.e(TAG, "filterMessages: found");
            } else {
                Log.e(TAG, "filterMessages: Not Found");
            }
        }
//        filterModel.setRecord(filteredList);

        if (text.length() > 0) {
            oldMsgAdapter.filterList(filteredList);
        } else {
            if (oldMsgList != null) {
                filteredList.clear();
                for (Record row : oldMsgList.getRecord()) {
                    // name match condition. this might differ depending on your requirement
                    if (row.getIsRead().equalsIgnoreCase("Y")) {
                        filteredList.add(row);
                        Log.e(TAG, "filterMessages: found");
                    } else {
                        Log.e(TAG, "filterMessages: Not Found");
                    }
                }
                oldMsgAdapter.filterList(filteredList);
            }

        }
        //calling a method of the adapter class and passing the filtered list
        if (oldMsgAdapter.getItemCount() != 0) {
            txtMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtMessage.setVisibility(View.GONE);
            return true;
        }
        //new array list that will hold the filtered data
       /* List<NotificationMessModel> filteredList = new ArrayList<>();
        for (NotificationMessModel row : notificationMessList) {

            // name match condition. this might differ depending on your requirement
            if (row.getHeading().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(row);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        notificationMessAdapter.filterList(filteredList);
        if (notificationMessAdapter.getItemCount() != 0) {
            txtMessage.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtMessage.setVisibility(View.GONE);
            return true;
        }*/
//       return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       /* ((MainActivity) getActivity()).searchEditText.removeTextChangedListener(searchWatcher);
        ((MainActivity) getActivity()).searchEditText.setText("");*/
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && view != null) {
//            if (((MainActivity) getActivity()).searchEditText != null) {
            ((MainActivity) getActivity()).searchEditText.addTextChangedListener(searchWatcher);
//            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(PUSH_NOTIFICATION));

            Log.e("message_Frag", "visible");
// }

        } else {
            if (view != null) {
//                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
                ((MainActivity) getActivity()).searchEditText.setText("");
                ((MainActivity) getActivity()).searchEditText.removeTextChangedListener(searchWatcher);
              /*  boolean isNewMessage = filter("");
                Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_new_mess_adapter, 10);
                boolean isMessage = filterMessages("");
                Global.getTotalHeightofLinearRecyclerView(getActivity(), messRecycler, R.layout.notification_new_mess_adapter, 10);
*/
                Log.e("message_Frag", "not-visible");

            }
//            Log.e("message_Frag", "not-visible");
        }
    }

    private void onCickListners() {
        actionButton.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StreamedVideosFragment()).addToBackStack(null).commit();
        });
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                NotificationModel message = intent.getParcelableExtra(NT_DATA);
                if (message != null) {
                    switch (message.getNotiType()) {
                        case NT_ACCEPT_REJECT:
                            break;
                        case NT_FOLLOW:
                            break;
                        case NT_COMMENT_ADDED:
                            break;
                        case NT_PRODUCT_ADDED:
                            break;
                        case NT_CHAT:
                            getChatedListApi();
                            break;
                        case NT_PAYMENT:
                            break;
                        case NT_OFFER_MAKE:
                            break;
                        default:

                            break;
                    }
                }
                Log.e("receiver", "Got message: Message " + message.getMessage());
            } catch (Exception e) {
                Log.e("receiver_failure", "onReceive: " + e.getMessage());
            }
        }
    };

    private void getChatedListApi() {
        new ApisHelper().getChattedUsersListApi(getActivity(), new ApisHelper.OnGetChatedListDataListners() {
            @Override
            public void onGetChattedListSuccess(ChattedListModel body, Dialog dialog) {
                try {
                    setupOldMessageList(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetChattedListFailure() {

            }
        });
    }

    private void setupOldMessageList(ChattedListModel body) throws Exception {

        oldMsgList = body;
        List<Record> oldMsgList1 = new ArrayList<>();
        List<Record> newMsgList = new ArrayList<>();
        for (Record row : body.getRecord()) {
            // name match condition. this might differ depending on your requirement
            if (row.getIsRead().equalsIgnoreCase("Y")) {
                oldMsgList1.add(row);
                Log.e(TAG, "filterMessages: old");
            } else {
                newMsgList.add(row);
                Log.e(TAG, "filterMessages: Not old");
            }
        }
        if (newMsgList != null && newMsgList.size() > 0) {
            ((NotificationFragment) getParentFragment()).messageImage.setVisibility(View.VISIBLE);
        } else {
            ((NotificationFragment) getParentFragment()).messageImage.setVisibility(View.GONE);
        }
        if (oldMsgList1 != null && oldMsgList1.size() > 0) {
            txtMessage.setVisibility(View.VISIBLE);
        } else {
            txtMessage.setVisibility(View.GONE);
        }
        oldMsgAdapter = new MessageListAdapter(oldMsgList1, getActivity(), 0, getActivity().getSupportFragmentManager());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        messRecycler.setLayoutManager(horizontalLayoutManager);
        messRecycler.setAdapter(oldMsgAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), messRecycler, R.layout.notification_mess_adapter, 10);
        new SwipeHelper(getActivity(), messRecycler) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#666666"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                Log.e(TAG, "onClick: Delete");
                            }
                        }
                ));
                underlayButtons.add(new UnderlayButton(
                        "Report",
                        0,
                        Color.parseColor("#ffc53e"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                Log.e(TAG, "onClick: Report");
                                ReportUserDialog.create(getActivity(), oldMsgList1.get(pos).getFriendId(), new ReportUserDialog.ReportUserCallback() {
                                    @Override
                                    public void onReportUserSuccess(String msg) {
                                        Snackbar.make(getActivity().getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();
                                    }

                                    @Override
                                    public void onReportUserFailure() {
                                        Snackbar.make(getActivity().getWindow().getDecorView(), "Something went's wrong", Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();
                                    }
                                }).show();
                            }
                        }
                ));

            }
        };

        newMsgAdapter = new MessageListAdapter(newMsgList, getActivity(), 0, getActivity().getSupportFragmentManager());
//        notificationMessAdapter = new NotificationActivityAdapter(notificationMessList, getActivity(), 0);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        newMessRecycler.setLayoutManager(horizontalLayoutManager1);
        newMessRecycler.setAdapter(newMsgAdapter);
        Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_mess_adapter, 0);
        if (newMsgList != null && newMsgList.size() > 0) {
            txtNewMessage.setVisibility(View.VISIBLE);
            ((MainActivity)getActivity()).notificationImg.setVisibility(View.VISIBLE);
        } else {
            Global.getTotalHeightofLinearRecyclerView(getActivity(), newMessRecycler, R.layout.notification_mess_adapter, 0);
            txtNewMessage.setVisibility(View.GONE);
            ((MainActivity)getActivity()).notificationImg.setVisibility(View.GONE);
        }
        new SwipeHelper(getActivity(), newMessRecycler) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#E35252"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                                Log.e(TAG, "onClick: delete");
                            }
                        }
                ));
                underlayButtons.add(new UnderlayButton(
                        "Report",
                        0,
                        Color.parseColor("#EEC12D"),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Log.e(TAG, "onClick: Report");
                                ReportUserDialog.create(getActivity(), newMsgList.get(pos).getFriendId(), new ReportUserDialog.ReportUserCallback() {
                                    @Override
                                    public void onReportUserSuccess(String msg) {
                                        Snackbar.make(getActivity().getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();
                                    }

                                    @Override
                                    public void onReportUserFailure() {
                                        Snackbar.make(getActivity().getWindow().getDecorView(), "Something went's wrong", Snackbar.LENGTH_SHORT)
                                                .setAction("", null).show();
                                    }
                                }).show();
                            }
                        }
                ));

            }
        };
    }
}
