package com.example.mytime.UserDefined;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytime.DataStructure.CountDownItem;
import com.example.mytime.R;
import com.freegeek.android.materialbanner.holder.Holder;

public class MyHolder implements Holder<CountDownItem> {
    TextView title;
    TextView targetData;
    TextView countdown;
    ImageView imageView;
    Context context;

    CountDownItem countDownItem;
    //int i = 0;


    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.material_banner_item,null);
        title = view.findViewById(R.id.material_banner_item_title);
        targetData = view.findViewById(R.id.material_banner_item_targetdata);
        countdown = view.findViewById(R.id.material_banner_item_countdown);
        imageView = view.findViewById(R.id.material_banner_item_imageview);
        //i+=1;
        //Log.d("createView", "createView: "+i);
        return view;
    }

    @Override
    public void updateUI(Context context, int i, CountDownItem countDownItem) {
        title.setText(countDownItem.getTitle());
        targetData.setText(countDownItem.getTargetDateSimple());
        countdown.setText(countDownItem.getCountDownString());
        imageView.setImageResource(countDownItem.getImageId());
        this.context =context;
        this.countDownItem = countDownItem;

    }

    public void update(){

        countdown.setText(countDownItem.getCountDownString());
    }



}

