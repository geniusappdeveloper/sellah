package com.app.admin.sellah.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.stripe.StripeApp;
import com.app.admin.sellah.controller.stripe.StripeButton;
import com.app.admin.sellah.controller.stripe.StripeConnectListener;
import com.app.admin.sellah.controller.utils.RecyclerViewClickListener;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.model.extra.ChatModel.ChatMessageModel;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.model.extra.MakeOffer.MakeOfferModel;
import com.app.admin.sellah.view.CustomDialogs.PaymentDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.ChatActivity;
import com.app.admin.sellah.view.activities.ImageViewerActivity;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.fragments.ChatDetailFragment;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.stripe.StripeSession.API_ACCESS_TOKEN;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.Chat_User_Data;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ChatAdapter extends RecyclerView.Adapter {


    List<ChatMessageModel> messagelist;
    Context context;
    private RecyclerViewClickListener onBottomReachedListener;
    private WebService service;
    private StripeApp StripeAppmApp;

    public static class SendMessageViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView txtMessage, txtMsgTime;

        public SendMessageViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.txtMessage = itemView.findViewById(R.id.txt_message);
            this.txtMsgTime = itemView.findViewById(R.id.txt_msg_time);

        }
    }


    public static class RecievedMessageViewHolder extends RecyclerView.ViewHolder {


        CircleImageView userImage;
        TextView txtMessage, txtMsgTime;

        public RecievedMessageViewHolder(View itemView) {
            super(itemView);

            this.userImage = itemView.findViewById(R.id.img_user);
            this.txtMessage = itemView.findViewById(R.id.txt_message);
            this.txtMsgTime = itemView.findViewById(R.id.txt_msg_time);

        }
    }

    public static class ImageSentViewHolder extends RecyclerView.ViewHolder {


        CircleImageView userImage;
        ImageView imgSent;

        public ImageSentViewHolder(View itemView) {
            super(itemView);

            this.userImage = itemView.findViewById(R.id.img_user);
            this.imgSent = itemView.findViewById(R.id.img_sent_image);

        }
    }

    public static class ImageReceivedViewHolder extends RecyclerView.ViewHolder {


        CircleImageView userImage;
        ImageView imgSent;

        public ImageReceivedViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.img_user);
            this.imgSent = itemView.findViewById(R.id.img_received_image);

        }
    }

    public static class MakeOfferViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemQuantity, txtItemCost, txtCanceled;
        Button btnAccept, btnCanceld, btnPay;
        LinearLayout liOffer, liPay, liOfferStatus;


        public MakeOfferViewHolder(View itemView) {
            super(itemView);

            this.txtItemName = (TextView) itemView.findViewById(R.id.txt_item_name);
            this.txtItemQuantity = (TextView) itemView.findViewById(R.id.txt_item_quantity);
            this.txtItemCost = (TextView) itemView.findViewById(R.id.txt_item_cost);
            this.txtCanceled = (TextView) itemView.findViewById(R.id.txt_offer_status);
            this.btnAccept = itemView.findViewById(R.id.btn_item_accept);
            this.btnPay = itemView.findViewById(R.id.btn_pay);
            this.btnCanceld = itemView.findViewById(R.id.btn_item_cancel);
            this.liOffer = itemView.findViewById(R.id.li_confirm_order);
            this.liPay = itemView.findViewById(R.id.li_pay_order);
            this.liOfferStatus = itemView.findViewById(R.id.li_offer_status);

        }
    }

    private TextView txtReview;
    public ChatAdapter(List<ChatMessageModel> messageList, Context context, TextView txtReview) {

        this.messagelist = messageList;
        this.context = context;
        this.txtReview=txtReview;
        onBottomReachedListener = new ChatActivity();
        service = Global.WebServiceConstants.getRetrofitinstance();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SAConstants.Keys.TYPE_SEND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_sent, parent, false);
                return new SendMessageViewHolder(view);
            case SAConstants.Keys.TYPE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_recieved, parent, false);
                return new RecievedMessageViewHolder(view);
            case SAConstants.Keys.TYPE_MAKEOFFER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_make_offer, parent, false);
                return new MakeOfferViewHolder(view);
            case SAConstants.Keys.TYPE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_send_image, parent, false);
                return new ImageSentViewHolder(view);
            case SAConstants.Keys.TYPE_RECIEVED_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_received_image, parent, false);
                return new ImageReceivedViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (messagelist.get(position).getSenderId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
            if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                handleOfferData(holder, position);

            } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {

                handleSentImageData(holder, position);

            } else {
                handleSentMessageData(holder, position);
            }
        } else {
            if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                handleOfferData(holder, position);

            } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {

                handleRecievedImageData(holder, position);

            } else {
                handleRecievMessageData(holder, position);
            }
        }
       /* if (messagelist.get(position).isSender()) {


            if (messagelist.get(position).isOffer()) {

            } else if (messagelist.get(position).isSent()) {
                if (messagelist.get(position).isImage()) {

                } else {

                }
            } else {
                if (messagelist.get(position).isImage()) {
                    handleSentImageData(holder, position);
                } else {
                    handleRecievMessageData(holder, position);
                }

            }
        }
       */
        if (position == messagelist.size() - 1) {

            onBottomReachedListener.onBottomReached(true);

        } else {

            onBottomReachedListener.onBottomReached(false);

        }
//        notifyDataSetChanged();
    }

    private void handleRecievMessageData(RecyclerView.ViewHolder holder, int position) {
        ((RecievedMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).getMessage());
        ((RecievedMessageViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultingIntent = new Intent(context, MainActivity.class);
                resultingIntent.putExtra(Chat_User_Data, messagelist.get(position).getSenderId());
                ((ChatActivity) context).startActivity(resultingIntent);
            }
        });

//        ((RecievedMessageViewHolder) holder).userImage.setImageResource(messagelist.get(position).getImageId());
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((RecievedMessageViewHolder) holder).userImage);
        ((RecievedMessageViewHolder) holder).txtMsgTime.setText(formateDate(Global.convertUTCToLocal(messagelist.get(position).getCreatedAt())));
       /* ((RecievedMessageViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(SAConstants.Keys.OTHER_USER_ID, messagelist.get(position).getSenderId());
                PersonalProfileFragment fragment=new PersonalProfileFragment();
                fragment.setArguments(bundle);
                ((ChatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(PROFILETAG).commit();
            }
        });*/

    }

    private void handleSentMessageData(RecyclerView.ViewHolder holder, int position) {

        ((SendMessageViewHolder) holder).txtMessage.setText(messagelist.get(position).getMessage());
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((SendMessageViewHolder) holder).userImage);
//        ((SendMessageViewHolder) holder).userImage.setImageResource(messagelist.get(position).getImageId());
        ((SendMessageViewHolder) holder).txtMsgTime.setText(formateDate(Global.convertUTCToLocal(messagelist.get(position).getCreatedAt())));

    }

    private void handleSentImageData(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(messagelist.get(position).getImageUrl()).apply(Global.getGlideOptions())
                .into(((ImageSentViewHolder) holder).imgSent);
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((ImageSentViewHolder) holder).userImage);
        ((ImageSentViewHolder) holder).imgSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewerActivity.start(context, messagelist.get(position).getImageUrl());
            }
        });
    }

    private void handleRecievedImageData(RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(messagelist.get(position).getImageUrl()).apply(Global.getGlideOptions())
                .into(((ImageReceivedViewHolder) holder).imgSent);
        Glide.with(context).load(messagelist.get(position).getSenderImage()).apply(Global.getGlideOptions())
                .into(((ImageReceivedViewHolder) holder).userImage);
        ((ImageReceivedViewHolder) holder).imgSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewerActivity.start(context, messagelist.get(position).getImageUrl());
            }
        });
        ((ImageReceivedViewHolder) holder).userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultingIntent = new Intent(context, MainActivity.class);
                resultingIntent.putExtra(Chat_User_Data, messagelist.get(position).getSenderId());
                ((ChatActivity) context).startActivity(resultingIntent);
            }
        });
    }

    private void StripConnect(MakeOfferViewHolder holder, int position) {
        StripeAppmApp = new StripeApp(context, "geniusAppDeveloper", "ca_9bmLYpWBQDumLtFp2KZ7bE90kHjXS5le",
                "sk_test_QW9KCbQ08S6BSGogNk3XKDTa", "https://developer.android.com", "read_write");
//        mStripeButton = (StripeButton) findViewById(R.id.btnStripeConnect);
        StripeButton btnStripeConnect = new StripeButton(context);
        btnStripeConnect.setStripeApp(StripeAppmApp);
        btnStripeConnect.setConnectMode(StripeApp.CONNECT_MODE.DIALOG);
        btnStripeConnect.addStripeConnectListener(new StripeConnectListener() {

            @Override
            public void onConnected() {
                Log.e("Connected_as", "onConnected: " + StripeAppmApp.getUserId());
                if (StripeAppmApp.getUserId() != null) {

                    new ApisHelper().linkStripApi(context, StripeAppmApp.getUserId(), new ApisHelper.StripeConnectCallback() {
                        @Override
                        public void onStripeConnectSuccess(String msg) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            HelperPreferences.get(context).saveString(API_ACCESS_TOKEN, StripeAppmApp.getUserId());
                            acceptDeclineApi((holder), messagelist.get(position).getOfferId(), "a", position);
//                            txtConnectionStatus.setVisibility(View.GONE);
                        }

                        @Override
                        public void onStripeConnectFailure() {
                            Toast.makeText(context, "Unable to link account with stripe at this movements", Toast.LENGTH_SHORT).show();
            /*                Snackbar.make(context.getWindow().getDecorView(), , Snackbar.LENGTH_SHORT)
                                    .setAction("", null).show();*/

                        }
                    });
                }
//                tvSummary.setText("Connected as " + StripeAppmApp.getAccessToken());
            }

            @Override
            public void onDisconnected() {
                Log.e("Disconnected", "Disconnected: ");
//                tvSummary.setText("Disconnected");
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }

        });
        btnStripeConnect.performClick();

    }

    private void handleOfferData(RecyclerView.ViewHolder holder, int position) {

        ((MakeOfferViewHolder) holder).txtItemName.setText(messagelist.get(position).getProductName());
        try {
            if (!TextUtils.isEmpty(messagelist.get(position).getQuantity())) {
                ((MakeOfferViewHolder) holder).txtItemQuantity.setText(messagelist.get(position).getQuantity());
            } else {
                ((MakeOfferViewHolder) holder).txtItemQuantity.setText("1");
            }
        } catch (Exception e) {
            ((MakeOfferViewHolder) holder).txtItemQuantity.setText("1");
        }
        ((MakeOfferViewHolder) holder).txtItemCost.setText(messagelist.get(position).getProductCost());
        if (messagelist.get(position).getSenderId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
            showOfferStatusSenderSide(((MakeOfferViewHolder) holder), messagelist.get(position).getStatus(), position);
            ((MakeOfferViewHolder) holder).btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PaymentDialog.create(context, messagelist.get(position).getOfferId(), messagelist.get(position).getReceiverId(), messagelist.get(position).getProductId(), "", "", new PaymentDialog.PaymentCallBack() {
                        @Override
                        public void onPaymentSuccess() {
                            messagelist.get(position).setStatus("s");
                            notifyDataSetChanged();
                            if(txtReview!=null){
                                txtReview.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onPaymentFail(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelDialog() {

                        }
                    }).show();
                }
            });

        } else {
            showOfferStatusReceiverSide(((MakeOfferViewHolder) holder), messagelist.get(position).getStatus(), position);
            ((MakeOfferViewHolder) holder).btnCanceld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptDeclineApi(((MakeOfferViewHolder) holder), messagelist.get(position).getOfferId(), "r", position);
                }
            });
            ((MakeOfferViewHolder) holder).btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    HelperPreferences.get(context).remove(API_ACCESS_TOKEN);
                    if (!TextUtils.isEmpty(HelperPreferences.get(context).getString(API_ACCESS_TOKEN))) {
                        acceptDeclineApi(((MakeOfferViewHolder) holder), messagelist.get(position).getOfferId(), "a", position);
                    } else {
                        S_Dialogs.getStipeConnectDialog(context, ((dialog, which) -> {
                            StripConnect((MakeOfferViewHolder) holder, position);
                        })).show();
                    }
                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {

        if (messagelist.get(position).getSenderId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
            if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                return SAConstants.Keys.TYPE_MAKEOFFER;

            } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {

                return SAConstants.Keys.TYPE_IMAGE;

            } else {
                return SAConstants.Keys.TYPE_SEND;
            }
        } else {
            if (messagelist.get(position).getType().equalsIgnoreCase("o")) {

                return SAConstants.Keys.TYPE_MAKEOFFER;

            } else if (messagelist.get(position).getType().equalsIgnoreCase("img")) {

                return SAConstants.Keys.TYPE_RECIEVED_IMAGE;

            } else {
                return SAConstants.Keys.TYPE_RECEIVED;
            }
        }

    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    private String formateDate(String dateToChange) {

        String formattedDate = "";
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat writeFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = readFormat.parse(dateToChange);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }
        return formattedDate;
    }

    public void showAcceptRejectoption(MakeOfferViewHolder holder) {

        (holder).txtCanceled.setVisibility(View.GONE);
        (holder).liOffer.setVisibility(View.VISIBLE);
        (holder).liPay.setVisibility(View.GONE);

    }

    public void showStatus(MakeOfferViewHolder holder, String status, int colorId) {
        (holder).txtCanceled.setVisibility(View.VISIBLE);
        (holder).txtCanceled.setText(status);
        (holder).txtCanceled.setTextColor(context.getResources().getColor(colorId));
        (holder).liOffer.setVisibility(View.GONE);
        (holder).liPay.setVisibility(View.GONE);
    }

    public void showPayOptions(MakeOfferViewHolder holder) {
        (holder).txtCanceled.setVisibility(View.GONE);
        (holder).liOffer.setVisibility(View.GONE);
        (holder).liPay.setVisibility(View.VISIBLE);
    }

    public void showOfferStatusSenderSide(MakeOfferViewHolder holder, String status, int pos) {
        if (status.equalsIgnoreCase("p")) {
            showStatus(holder, "Pending", R.color.colorGrey);
        } else if (status.equalsIgnoreCase("a")) {
            showPayOptions(holder);
        } else if (status.equalsIgnoreCase("s")) {
            showStatus(holder, "Purchased", R.color.colorRed);
        } else {
            showStatus(holder, "Rejected", R.color.colorGrey);
        }
    }

    public void showOfferStatusReceiverSide(MakeOfferViewHolder holder, String status, int pos) {
        if (status.equalsIgnoreCase("p")) {
            showAcceptRejectoption(holder);
        } else if (status.equalsIgnoreCase("a")) {
            showStatus(holder, "Accepted", R.color.colorRed);
        } else if (status.equalsIgnoreCase("s")) {
            showStatus(holder, "Sold", R.color.colorRed);
        } else {
            showStatus(holder, "Rejected", R.color.colorGrey);
        }
    }

    private void acceptDeclineApi(MakeOfferViewHolder holder, String offerId, String status, int pos) {
        Log.e("OfferId", "acceptDeclineApi: " + offerId + " : ");
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<MakeOfferModel> acceptDeclineCall = service.acceptDeclineOfferApi(HelperPreferences.get(context).getString(UID), offerId, status);
        acceptDeclineCall.enqueue(new Callback<MakeOfferModel>() {
            @Override
            public void onResponse(Call<MakeOfferModel> call, Response<MakeOfferModel> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    Log.e("AcceptDecline", "onResponse: " + response.body().getResult().getStatus());
                    messagelist.get(pos).setStatus(status);
                    notifyDataSetChanged();
//                    showOfferStatusReceiverSide(holder, response.body().getResult().getStatus(), pos);
                } else {
                    try {
                        Log.e("AcceptDecline", "onResponsefaild: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeOfferModel> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("AcceptDecline", "onResponsefaild: " + t.getMessage());
            }
        });
    }
}