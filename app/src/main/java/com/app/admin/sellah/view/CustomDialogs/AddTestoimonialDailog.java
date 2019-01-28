package com.app.admin.sellah.view.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.model.extra.commonResults.Common;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.HelperPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class AddTestoimonialDailog extends AlertDialog {


    @BindView(R.id.rate_txt)
    TextView rateTxt;
    @BindView(R.id.rtB_rating)
    RatingBar rtBRating;
    @BindView(R.id.txt_testimonial)
    TextView txtTestimonial;
    @BindView(R.id.edt_testimonials)
    EditText edtTestimonials;
    @BindView(R.id.btn_submit_review)
    Button btnSubmitReview;
    private WebService webService;
    private Dialog dialog;
    private Context context;
    Float givenRating;
    String otherUserId;

    AddtestimonialListner listner;
    protected AddTestoimonialDailog(Context context, String otherUserId,AddtestimonialListner listner) {
        super(context);
        this.listner=listner;
        this.context = context;
        this.otherUserId=otherUserId;
    }

    public static AddTestoimonialDailog create(Context context,String otherUserId,AddtestimonialListner listner) {
        return new AddTestoimonialDailog(context,otherUserId,listner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = Global.WebServiceConstants.getRetrofitinstance();
        dialog = S_Dialogs.getLoadingDialog(context);
        setContentView(R.layout.layout_add_testimonial_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ButterKnife.bind(this);

        rtBRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("Rating_value", rating + "");
                givenRating = rating;
            }
        });


    }



    @OnClick(R.id.btn_submit_review)
    public void onViewClicked() {
        if (!Global.getText(edtTestimonials).equalsIgnoreCase("")) {
            addTestimonialApi(otherUserId,Global.getText(edtTestimonials), String.valueOf(givenRating));
        } else {
            Toast.makeText(context, "Please give some review aboout user.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addTestimonialApi(String otherUserId, String feedBack, String rating) {
        Dialog dialog = S_Dialogs.getLoadingDialog(context);
        dialog.show();
        Call<Common> addTestimonialCall = webService.addTestimonialApi(HelperPreferences.get(context).getString(UID), otherUserId, feedBack, rating);
        addTestimonialCall.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Log.e("AddTestimonialApi", "onResponse: " + response.body());
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        listner.onSuccessListner();
                        S_Dialogs.getInformation(context,response.body().getMessage(),((dialog1, which) -> {
                            dismiss();
                            listner.onSuccessListner();
                        })).show();

                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("AddTestimonialApi", "onFailure: " + t.getMessage());
            }
        });
    }


    public interface AddtestimonialListner{
        void onSuccessListner();
    }



}
