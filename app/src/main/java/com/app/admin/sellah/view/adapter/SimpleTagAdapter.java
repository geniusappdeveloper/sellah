package com.app.admin.sellah.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.getProductsModel.Tag;

import java.util.List;

public class SimpleTagAdapter extends RecyclerView.Adapter<SimpleTagAdapter.MyViewHolder> {


    TextView titleTextView;

    private Context context;

    List<com.app.admin.sellah.model.extra.ProductDetails.Tag> title;

    public SimpleTagAdapter(Context context, List<com.app.admin.sellah.model.extra.ProductDetails.Tag> title) {
        this.context = context;
        this.title = title;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

            titleTextView = view.findViewById(R.id.tag_text);
        }
    }

    @Override
    public SimpleTagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_tag_adapter_view, parent, false);

        return new SimpleTagAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleTagAdapter.MyViewHolder holder, int position) {
        titleTextView.setText(title.get(position).getTagName());
    }

    @Override
    public int getItemCount() {
        return title.size();

    }
}