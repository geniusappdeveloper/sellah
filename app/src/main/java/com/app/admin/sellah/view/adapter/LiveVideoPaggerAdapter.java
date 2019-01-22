package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.model.extra.LiveVideoModel.VideoList;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;
import com.bumptech.glide.Glide;

import java.util.List;

public class LiveVideoPaggerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.car_icon,R.drawable.car_icon,R.drawable.car_icon};
    private List<VideoList> videoList;

    public LiveVideoPaggerAdapter(Context context, List<VideoList> videoLists) {
        this.context = context;
        this.videoList = videoLists;
    }

    @Override
    public int getCount() {
        if (videoList != null && videoList.size() > 0) {
            if(videoList.size() > 3){
               return 3;
            }else {
            return videoList.size();}
        } else {
            return 0;
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_live_pagger_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setPadding(0, 0, 0, 0);
//        imageView.setImageResource(images[position]);


        if (videoList != null && videoList.size() > 0) {
            Glide.with(context)
                    .load(videoList.get(position).getCoverImage())
                    .apply(Global.getGlideOptions()/*.placeholder(R.drawable.logo_new).error(R.drawable.logo_new)*/)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.logo_new);
            imageView.setPadding((int) context.getResources().getDimension(R.dimen._10sdp)
                    , (int) context.getResources().getDimension(R.dimen._10sdp)
                    , (int) context.getResources().getDimension(R.dimen._10sdp)
                    , (int) context.getResources().getDimension(R.dimen._10sdp));
//            imageView.setImageResource(R.drawable.app_dummy_logo);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(context, MainActivityLiveStream.class));
                intent.putExtra("value", "noLiveStream");
                intent.putExtra("id",videoList.get(position).getVideoId());
                intent.putExtra("group_id",videoList.get(position).getGroupId());
                intent.putExtra("product_id",videoList.get(position).getProductId());
                intent.putExtra("product_name",videoList.get(position).getProductName());
                intent.putExtra("seller_id",videoList.get(position).getSellerId());
                intent.putExtra("start_time",videoList.get(position).getStartTime());
                intent.putExtra("views",videoList.get(position).getViews());
                context.startActivity(intent);
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

}