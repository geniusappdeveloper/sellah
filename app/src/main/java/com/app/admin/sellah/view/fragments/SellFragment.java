package com.app.admin.sellah.view.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.admin.sellah.R;
import com.app.admin.sellah.view.activities.AddNewVideos;
import com.app.admin.sellah.view.activities.MainActivity;
import com.app.admin.sellah.view.activities.MainActivityLiveStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SellFragment extends Fragment {


    @BindView(R.id.txt_sell)
    TextView txtSell;
    @BindView(R.id.txt_sub)
    TextView txtSub;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.lin2)
    LinearLayout lin2;
    @BindView(R.id.cardView2)
    CardView cardView2;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sell_product, container, false);
        unbinder = ButterKnife.bind(this, view);
        hideSearch();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.cardView, R.id.cardView2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cardView:
                Intent intent= new Intent(getActivity(),AddNewVideos.class);
                startActivity(intent);
                break;
            case R.id.cardView2:
                Intent intent1 = new Intent(new Intent(getActivity(), MainActivityLiveStream.class));
                intent1.putExtra("value", "LiveStream");
                getActivity().startActivity(intent1);
                break;
        }
    }

    public void hideSearch() {

        ((MainActivity) getActivity()).rel_search.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlFilter.setVisibility(View.GONE);
        ((MainActivity) getActivity()).text_sell.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).text_sell.setText("Sell");
        ((MainActivity) getActivity()).rloptions.setVisibility(View.GONE);
        ((MainActivity) getActivity()).rlBack.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).rlMenu.setVisibility(View.GONE);
        ((MainActivity) getActivity()).changeOptionColor(2);
    }

}
