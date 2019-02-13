package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.admin.sellah.controller.utils.Prodctfragment_click;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.view.activities.ImageSlideShowActivity;

import java.util.ArrayList;

public class GolfAdapter  extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    Prodctfragment_click click;
    public GolfAdapter(Context context, ArrayList<String> images,Prodctfragment_click click) {
        this.context = context;
        this.images=images;
        this.click = click;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getCount() {
        return images.size();
    }
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.product_detail_adapter_view, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image12);
        ImageView playimg = (ImageView) myImageLayout
                .findViewById(R.id.playimg);
        Log.e("image_array", "MyGolfAdapter: "+images);
       /* RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.centerInside();
        requestOptions.placeholder(R.drawable.glide_error);
        requestOptions.error(R.drawable.glide_error);*/



       if (images.get(position).contains("android.resource://com.app.admin.sellah/2131230895"))
       {
           playimg.setVisibility(View.VISIBLE);
       }
       else
       {
           playimg.setVisibility(View.GONE);
       }
        playimg.setOnClickListener(view1 -> click.onclick(true));
        RequestOptions requestOptions=Global.getGlideOptions();
        Glide.with(context)
                .load(images.get(position))
                .apply(requestOptions)
                .into(myImage);
//        myImage.setBackgroundResource(images.get(position).t);
        view.addView(myImageLayout);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ImageViewerActivity.start(context,images.get(position));
                ImageSlideShowActivity.start(context,images);
                /*Intent intent = new Intent(context, ImageSlideShowActivity.class);
                intent.putIntegerArrayListExtra("images", images);
                context.startActivity(intent);*/
            }
        });
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public float getPageWidth(int position) {
        return(1);
    }
}
