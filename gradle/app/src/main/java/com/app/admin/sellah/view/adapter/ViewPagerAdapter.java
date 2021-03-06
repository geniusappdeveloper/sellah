package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.bumptech.glide.Glide;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //    private Integer [] images = {R.drawable.car_icon,R.drawable.car_icon,R.drawable.car_icon};
    private List<String> bannerList;

    public ViewPagerAdapter(Context context, List<String> bannerList) {
        this.context = context;
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
        if (bannerList != null && bannerList.size() > 0) {
            return bannerList.size();
        } else {
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setPadding(0,0,0,0);
//        imageView.setImageResource(images[position]);

        if (bannerList != null && bannerList.size() > 0) {
            Glide.with(context)
                    .load(bannerList.get(position))
                    .apply(Global.getGlideOptions()/*.placeholder(R.drawable.logo_new).error(R.drawable.logo_new)*/)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.logo_new);
            imageView.setPadding((int) context.getResources().getDimension(R.dimen._10sdp)
                    ,(int) context.getResources().getDimension(R.dimen._10sdp)
                    ,(int) context.getResources().getDimension(R.dimen._10sdp)
                    ,(int) context.getResources().getDimension(R.dimen._10sdp));
//            imageView.setImageResource(R.drawable.app_dummy_logo);
        }


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
    @Override
    public float getPageWidth(int position) {
        return(0.60f);
    }

}
