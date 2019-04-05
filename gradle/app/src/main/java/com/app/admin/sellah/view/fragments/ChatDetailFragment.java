package com.app.admin.sellah.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.ImageUploadHelper;
import com.app.admin.sellah.controller.utils.MessageToneManager;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.ChatModel.ChatMessageModel;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.model.extra.MessagesModel.ChatListModel;
import com.app.admin.sellah.model.extra.Notification.NotificationModel;
import com.app.admin.sellah.model.extra.RegisterPojo.Message;
import com.app.admin.sellah.model.extra.UploadChatImage.UploadChatImageModel;
import com.app.admin.sellah.view.CustomDialogs.AddTestoimonialDailog;
import com.app.admin.sellah.view.CustomDialogs.MakeOfferDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.adapter.ChatAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.admin.sellah.controller.utils.Global.getBytesFromBitmap;
import static com.app.admin.sellah.controller.utils.Global.playMessageTone;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_CREATEROOM;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_NEW_MESSAGE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.EVENT_READMESSAGE;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PUSH_NOTIFICATION;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.NotificationKeys.NT_DATA;
import static org.webrtc.ContextUtils.getApplicationContext;

public class ChatDetailFragment extends Fragment/* implements ChatFragmentController */ {

    Unbinder unbinder;
    View view;
    Socket mSocket;
    @BindView(R.id.txt_review)
    public TextView txtReview;
    @BindView(R.id.rec_message)
    RecyclerView recMessage;
    @BindView(R.id.img_send_camera)
    ImageView imgSendCamera;
    @BindView(R.id.img_send_gallery)
    ImageView imgSendGallery;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.btn_send)
    ImageView btnSend;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.card_bottom_view)
    CardView cardBottomView;
    @BindView(R.id.fr_root)
    FrameLayout frRoot;
    private WebService service;
    private boolean isConnected;
    String otherUserId;
    private List<ChatMessageModel> models;
    private ChatAdapter adapter;
    private int GALLERY = 1213;
    private int CAMERA_PIC_REQUEST = 1212;
    String otherUserName = "";
    String otherUserImage = "";
    private String imagePath;
    private MakeOfferModel makeOfferResult;
    private Global app;
    private boolean isOffer;
    String roomName = "";
    //private boolean ActivityPaused = false;
    Call<ChatListModel> chatListCall;
    public ChatDetailFragment() {

    }

    @SuppressLint("ValidFragment")
    public ChatDetailFragment(String otherUserId, MakeOfferModel makeOfferModel) {
        this.otherUserId = otherUserId;
        this.makeOfferResult = makeOfferModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver1,
                new IntentFilter(PUSH_NOTIFICATION));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_chat_dtail, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        app = (Global) getActivity().getApplicationContext();
        mSocket = app.getSocket();
        isOffer = true;
        service = Global.WebServiceConstants.getRetrofitinstance();

        models = new ArrayList<>();
        setUpMessgae(models);
        setUpSendButton();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.e("OnDestroy", "OnDestroy");
        disconnectSocket();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver1);
    }

    @Override
    public void onPause() {
        super.onPause();
        //ActivityPaused  = true;
        Log.e("OnPause", "OnPause");

        disconnectSocket();
        isConnected = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatListCall!=null)
        chatListCall.cancel();
    }



    private void ConnectToSocket() {

        this.mSocket.on(Socket.EVENT_CONNECT, onConnect);
        this.mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        this.mSocket.on(EVENT_CREATEROOM, onRoomCreation);
        this.mSocket.on(EVENT_READMESSAGE, onReadMessage);
        this.mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        this.mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        this.mSocket.on(EVENT_NEW_MESSAGE, onNewMessage);
        this.mSocket.connect();
    }

    public void disconnectSocket() {
        try {
            this.mSocket.disconnect();
            this.mSocket.off(EVENT_CREATEROOM, onRoomCreation);
            this.mSocket.off(Socket.EVENT_CONNECT, onConnect);
            this.mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            this.mSocket.off(EVENT_READMESSAGE, onReadMessage);
            this.mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            this.mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            this.mSocket.off(EVENT_NEW_MESSAGE, onNewMessage);
        } catch (Exception e) {
            Log.e("Exception", "disconnectSocket: " + e.getMessage());
        }
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("sender_id", HelperPreferences.get(getActivity()).getString(UID));
                            jsonObject.put("receiver_id", otherUserId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("CreateRoom_params", jsonObject.toString());
                        mSocket.emit(EVENT_CREATEROOM, jsonObject);
                        Log.e("Connection", "connected");
                        getChathistoryApi(otherUserId);
                        /*Toast.makeText(getApplicationContext(),
                                "connect", Toast.LENGTH_LONG).show();*/
                        isConnected = true;
                    } else {
                        Toast.makeText(getActivity(), "Already connected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("Connection", "disconnected");
                    isConnected = false;
                    /*Toast.makeText(getApplicationContext(),
                            "disconnect", Toast.LENGTH_LONG).show();*/
                }
            });
        }
    };
    private Emitter.Listener onRoomCreation = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("Event_createRoom", "run: created" + args[0].toString());
                    JSONObject data = (JSONObject) args[0];
                    try {
                        roomName = data.getString("room_name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_id", HelperPreferences.get(getActivity()).getString(UID));
                        jsonObject.put("room_name", roomName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Exception_message", "Emit message");
                    }

                    mSocket.emit(EVENT_READMESSAGE, jsonObject);
                }
            });
        }
    };

    private Emitter.Listener onReadMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("Event_ReadMessage", "run: created" + args[0].toString());
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("connection_error", args.toString());
                    Toast.makeText(getActivity(),
                            "error_connect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    Log.e("onmsgReceive", data.toString());
                    playMessageTone(getActivity());
                    JSONObject roomParams = new JSONObject();
                    try {
                        roomParams.put("room_name", roomParams);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSocket.emit(EVENT_READMESSAGE, roomName);
                    String mJsonString = String.valueOf(args[0]);
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(mJsonString);
                    Gson gson = new Gson();
                    ChatMessageModel object = gson.fromJson(mJson, ChatMessageModel.class);
                    Log.e("messageModel", "run: " + object.getType());

                    if (object != null) {
                        models.add(object);
                        adapter.notifyDataSetChanged();
                        recMessage.smoothScrollToPosition(adapter.getItemCount() - 1);
                    } else {
                        Log.e("msg_object", "Error");
                    }
                }
            });
        }
    };

    /* @Override
     public void setUserVisibleHint(final boolean visible) {
         super.setMenuVisibility(visible);
 //        if (this.isVisible()) {
             if (visible && view != null) {
                 Log.e("Fragment", "onHiddenChanged: is visible");
             } else {
                 Log.e("Fragment", "onHiddenChanged: is hidden");
             }
 //        }else{
 //            Log.e("Fragment", "onHiddenChanged: is hidden");
 //        }
       *//*  if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (visible) {
                Log.e("friend_Id", "onCreateView: " + otherUserId);
//                ConnectToSocket();
                onResume();
//                setUpSendButton();
            } else {
                disconnectSocket();
                Log.e("friend_Id", "onCreateView N");
//            disconnectSocket();
            }
        }*//*

    }*/
    private void setUpMessgae(List<ChatMessageModel> models) {

        try {

            adapter = new ChatAdapter(models, getActivity(),txtReview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
            recMessage.setLayoutManager(linearLayoutManager);
            recMessage.setItemAnimator(new DefaultItemAnimator());
            recMessage.setAdapter(adapter);
            if (models.size() > 0) {
                recMessage.scrollToPosition(models.size() - 1);


            }
            if (Build.VERSION.SDK_INT >= 11) {
                recMessage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v,
                                               int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (bottom < oldBottom) {
                            recMessage.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (models != null && models.size() > 0) {
                                            recMessage.smoothScrollToPosition(recMessage.getAdapter().getItemCount() - 1);
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }, 100);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e("setup_error", "setUpMessgae: ");
        }


    }

    private void getChathistoryApi(String otherUserId) {
      //  Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
//        dialog.show();
       chatListCall = service.getChatDetailApi(HelperPreferences.get(getActivity()).getString(UID), otherUserId);


        chatListCall.enqueue(new Callback<ChatListModel>() {
            @Override
            public void onResponse(Call<ChatListModel> call, Response<ChatListModel> response) {
                if (response.isSuccessful()) {


                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        models.clear();

                         ChatMessageModel chatMessageModel ;

                        for (int i=0;i<response.body().getRecord().size();i++)
                        {
                          chatMessageModel=  new ChatMessageModel();
                            java.text.DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (models.isEmpty())
                            {

                                Log.e("date1: ",response.body().getRecord().get(i).getCreatedAt() );
                                try {
                                    Date strDate = readFormat.parse(response.body().getRecord().get(i).getCreatedAt());
                                    chatMessageModel.setMessage("");
                                    chatMessageModel.setStatus("");
                                    chatMessageModel.setMessage("");
                                    chatMessageModel.setSenderId("");
                                    chatMessageModel.setReceiverId("");
                                    chatMessageModel.setType("");
                                    chatMessageModel.setToday_boolean(true);
                                    chatMessageModel.setToday(datefun(strDate));
                                    models.add(chatMessageModel);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }




                            }
                            else
                            {

                                try
                                {
                                    Date strDate = readFormat.parse(response.body().getRecord().get(i-1).getCreatedAt());
                                    Date strDate2 = readFormat.parse(response.body().getRecord().get(i).getCreatedAt());
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateString = formatter.format(new Date(strDate.getTime()));
                                    String dateString1 = formatter.format(new Date(strDate2.getTime()));

                                    Log.e("date2: ",dateString);

                                    if (!dateString1.equalsIgnoreCase(dateString))
                                    {
                                        String monthName = new SimpleDateFormat("MMMM").format(strDate2.getTime());
                                        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                                        String goal = outFormat.format(strDate2);

                                        Log.e("goal: ",datefun(strDate2));
                                        Log.e("date: ",dateString1 );
                                        chatMessageModel.setMessage("");
                                        chatMessageModel.setStatus("");
                                        chatMessageModel.setSenderId("");
                                        chatMessageModel.setReceiverId("");
                                        chatMessageModel.setType("");
                                        chatMessageModel.setToday_boolean(true);
                                        chatMessageModel.setToday(datefun(strDate2));
                                        models.add(chatMessageModel);
                                    }







                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            response.body().getRecord().get(i).setToday_boolean(false);
                            response.body().getRecord().get(i).setToday(response.body().getRecord().get(i).getCreatedAt());
                            models.add(response.body().getRecord().get(i));




                        }
                       // models.addAll(response.body().getRecord());
                        Log.e("Chat_detailApi", "onResponse: success"+response.body().toString() );

                    /*    if(){

                        }
                       *//* adapter.notifyDataSetChanged();

                        recMessage.scrollToPosition(adapter.getItemCount() - 1);*/
                        if (!TextUtils.isEmpty(response.body().getOnlineStatus()) && response.body().getOnlineStatus().equalsIgnoreCase("Y")) {
                            ((ChatActivity)getActivity()).imgOnline.setVisibility(View.VISIBLE);
                            ((ChatActivity)getActivity()).txtLastSeen.setVisibility(View.GONE);
                        } else {

                             if (((ChatActivity)getActivity()).imgOnline.getVisibility()==View.VISIBLE)
                             {
                                 ((ChatActivity)getActivity()).imgOnline.setVisibility(View.INVISIBLE);
                                 ((ChatActivity)getActivity()).txtLastSeen.setVisibility(View.VISIBLE);
                             }


                            if (!TextUtils.isEmpty(response.body().getLastSeenTime()))
                                ((ChatActivity)getActivity()).txtLastSeen.setText("Last seen " + ": " + Global.getTimeAgo(Global.convertUTCToLocal(response.body().getLastSeenTime())));
                        }
                        setUpMessgae(models);
                        if(response.body().getIs_reviewed()!=null&&response.body().getIs_reviewed().equalsIgnoreCase("N")){
                            txtReview.setVisibility(View.VISIBLE);
                            Log.e("Review_pending", "onResponse: "+response.body().getIs_reviewed() );
                        }else{
                            Log.e("Review_pending", "onResponse: "+"Y");
                            txtReview.setVisibility(View.GONE);
                        }
                        if (makeOfferResult != null) {
                            Log.e("OfferData", "onResponse: ");
                            attemptToSendOfferData(makeOfferResult, makeOfferResult.getResult().getProductName());
                        }

                    }
                }else{
                    try {
                        Log.e("Chat_detailApi", "onResponse: fail"+response.errorBody().string() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatListModel> call, Throwable t) {

                Log.e("Chat_detailApiFailure", "onFailure: "+t.getMessage() );
            }
        });
    }

    private void uploadChatImage(MultipartBody.Part mPart) {
        Dialog dialog = S_Dialogs.getLoadingDialog(getActivity());
        dialog.show();
        Call<UploadChatImageModel> uploadChatImageCall = service.uploadChatimageApi(mPart);
        uploadChatImageCall.enqueue(new Callback<UploadChatImageModel>() {
            @Override
            public void onResponse(Call<UploadChatImageModel> call, Response<UploadChatImageModel> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("sender_id", HelperPreferences.get(getActivity()).getString(UID));
                            jsonObject.put("receiver_id", otherUserId);
                            jsonObject.put("image_name", response.body().getImageName());
                            jsonObject.put("sender_image", "");
                            jsonObject.put("image_url", response.body().getImageUrl());
                            jsonObject.put("type", "img");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Exception_message", "Emit message");
                        }
                        Log.e("msg_params", jsonObject.toString());
                        mSocket.emit(EVENT_NEW_MESSAGE, jsonObject);
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Snackbar.make(frRoot, "Unable to send image at the movement.", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
            }

            @Override
            public void onFailure(Call<UploadChatImageModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("UploadChatImageApi", "onFailure: " + t.getMessage());
                Snackbar.make(frRoot, "Please try again later", Snackbar.LENGTH_SHORT)
                        .setAction("", null).show();
            }
        });
    }

    private void attemptToSendOfferData(MakeOfferModel body, String productName) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender_id", HelperPreferences.get(getActivity()).getString(UID));
            jsonObject.put("receiver_id", otherUserId);
            jsonObject.put("sender_image", "");
            jsonObject.put("type", "o");
            jsonObject.put("offer_id", body.getResult().getOfferId());
            jsonObject.put("product_id", body.getResult().getProductId());
            jsonObject.put("product_name", body.getResult().getProductName());
            jsonObject.put("price_cost", body.getResult().getPriceCost());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("msg_params", jsonObject.toString());
        mSocket.emit(EVENT_NEW_MESSAGE, jsonObject);
    }

    @OnClick(R.id.txt_review)
    public void onReviewClicked() {
        AddTestoimonialDailog dailog = new AddTestoimonialDailog();
        Bundle bundle = new Bundle();
        bundle.putString("other_id",otherUserId);
        dailog.setArguments(bundle);
        dailog.show(getActivity().getFragmentManager(),"testimonial");

    }

    private void setUpBottomView() {
        try {
            MakeOfferDialog.create(getActivity(), new MakeOfferDialog.OfferController() {
                @Override
                public void onMakeOfferButtonClick(MakeOfferModel offerPrice, String itemQuantity) {
                    attemptToSendOfferData(offerPrice, itemQuantity);
                }

                @Override
                public void onMakeOffer(MakeOfferModel body, String productName) {
                    attemptToSendOfferData(body, productName);
                }

                @Override
                public void onErrorSelection() {
                    Toast.makeText(getActivity(), "Please select at-least one item", Toast.LENGTH_SHORT).show();
                }
            }, otherUserId).show();
        } catch (Exception e) {
            Log.e("makeOfferDialog", "Exception: " + e.getMessage());
        }
    }

    private void setUpSendButton() {

        edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnSend.setImageResource(R.drawable.cart_icon_96dp);
                    isOffer = true;
                } else {
                    isOffer = false;
                    btnSend.setImageResource(R.drawable.send_50dp);
                }
            }
        });
    }

    @OnClick({R.id.img_send_camera, R.id.img_send_gallery, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_send_camera:
                takePhotoFromCamera();
                break;
            case R.id.img_send_gallery:
                choosePhotoFromGallary();
                break;
            case R.id.btn_send:
                if (isOffer) {

                    Log.e( "onViewClicked: ","dd" );
                    setUpBottomView();
                } else {
                    attemptSend();
                }
                break;
        }
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }


    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(galleryIntent, GALLERY);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 100:
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (readExternalStorage && camera) {
                   /*if(imageList.size()!=8){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_PIC_REQUEST);
                    }else{
                        Snackbar.make(rootTag, "Only eight images are allowed per item.", Snackbar.LENGTH_SHORT)
                                .setAction("", null).show();
                    }*/
//                  showPictureDialog();
                } else {
                    Snackbar.make(frRoot, "Unable to access camera", Snackbar.LENGTH_SHORT)
                            .setAction("", null).show();
                }
                break;

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    /*String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                            Base64.NO_WRAP);*/
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(tempUri, filePath, null, null, null);
                    cursor.moveToFirst();
                    imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                    uploadChatImage(ImageUploadHelper.convertImageTomultipart(imagePath, "image"));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String imgString = Base64.encodeToString(getBytesFromBitmap(imageBitmap),
                    Base64.NO_WRAP);
            Uri tempUri = getImageUri(getActivity().getApplicationContext(), imageBitmap);

            imagePath = getRealPathFromURI(tempUri);

            uploadChatImage(ImageUploadHelper.convertImageTomultipart(imagePath, "image"));

          /*  MessageModel msg1 = new MessageModel();
            msg1.setSent(true);
            msg1.setImage(true);
            msg1.setImageId(R.drawable.sellah_icon);
            msg1.setCapturedImage(getRealPathFromURI(tempUri));
            models.add(msg1);
            adapter.notifyDataSetChanged();*/

        }

    }

    private void attemptSend() {

        String message = edtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(new Date());
        edtMessage.setText("");

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("sender_id", HelperPreferences.get(getActivity()).getString(UID));
            jsonObject.put("receiver_id", otherUserId);
            jsonObject.put("message", message);
            jsonObject.put("sender_image", "https://ellahppdiag.blob.core.windows.net/chatimages/21112018015235_1542808355.jpg");
            jsonObject.put("created_at", str);
            jsonObject.put("image_url", "");
            jsonObject.put("type", "t");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("msg_params_error", e.getMessage());

        }
        Log.e("msg_params", jsonObject.toString());
        mSocket.emit(EVENT_NEW_MESSAGE, jsonObject);
    }

    public void setVisibilityHint(boolean b) {
//        if(view!=null){
//            ConnectToSocket();
//            Log.e("Fragment", "onHiddenChanged: is visible");
//        }
//        setVisibilityHint(b);
    }

    public void setUnvisibleVisibilityHint(boolean b) {
//        if(view!=null){
//            Log.e("Fragment", "onHiddenChanged: is hidden");
//            disconnectSocket();
//        }
//        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();

       /* if(!getUserVisibleHint()){
            Log.e("Fragment", "onHiddenChanged: is hidden");
            disconnectSocket();
            return;
        }*/

        //if(ActivityPaused) {
        //   ActivityPaused = false;
        ConnectToSocket();
        // }
        Log.e("ResumeFragment", "onHiddenChanged: is visible");
    }

    /*    @Override
        public void onFragmentVisible(int pos)
        {
            Log.e("VisibleFragment"+pos, "  CurrentPos "+ChatActivity.currentPage+"onHiddenChanged: is visible");

            if(pos==ChatActivity.currentPage)
            ConnectToSocket();
        }

        @Override
        public void onFragmentUnVisible(int pos)
        {
            Log.e("InvisibleFragment"+pos, "  CurrentPos "+ChatActivity.currentPage+"onHiddenChanged: is hidden");

            if(pos==ChatActivity.currentPage)
            disconnectSocket();
        }*/
    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            try {
                NotificationModel message = intent.getParcelableExtra(NT_DATA);
                Log.e("receiver", "Got message: ChatFragment" + message.getMessage() + ":" + message.getNotiType());
                if (message.getNotiType().equalsIgnoreCase(SAConstants.NotificationKeys.NT_ACCEPT_REJECT) ||
                        message.getNotiType().equalsIgnoreCase(SAConstants.NotificationKeys.NT_PAYMENT)) {
                    for (int i = 0; i < models.size(); i++) {
                        ChatMessageModel model = models.get(i);
                        Log.e("receiver", "onReceive: msgIds" + message.getMsgId() + " : " + models.get(i).getMsgId());
                        if (model.getMsgId().equalsIgnoreCase(message.getMsgId())) {
                            Log.e("index", "onReceive: " + models.indexOf(model));
                            models.get(models.indexOf(model)).setStatus(message.getStatus());
                            if(message.getStatus().equalsIgnoreCase("s")){
                                txtReview.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                            recMessage.smoothScrollToPosition(models.indexOf(model));
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Receiver", "onReceive: " + e.getMessage());
            }
        }
    };


    public static String getDate(long timestamp) {
        Calendar nowTime = Calendar.getInstance();
        Calendar neededTime = Calendar.getInstance();
        neededTime.setTimeInMillis(timestamp * 1000L);

        if (nowTime.get(Calendar.DATE) == neededTime.get(Calendar.DATE)) {
            //here return like "Today at 12:00"
            return "Today " + DateFormat.format("hh:mm:ss aa", neededTime);

        } else if (nowTime.get(Calendar.DATE) - neededTime.get(Calendar.DATE) == 1) {
            //here return like "Yesterday at 12:00"
            return "Yesterday " + DateFormat.format("hh:mm:ss aa", neededTime);

        } else {
            //here return like "May 31, 12:00"
            return DateFormat.format("dd-MM-yyyy hh:mm:ss aa", neededTime).toString();
        }

    }




    public String datefun(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("MMMM").format(cal.getTime());
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String da = outFormat.format(cal.getTime());
        int day   = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        String overall  = da +", " +day+" "+monthName;
        return overall;
    }

}

