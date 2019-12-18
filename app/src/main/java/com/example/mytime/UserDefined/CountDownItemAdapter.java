package com.example.mytime.UserDefined;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytime.DataStructure.CountDownItem;
import com.example.mytime.R;


import java.util.List;

public class CountDownItemAdapter extends RecyclerView.Adapter<CountDownItemAdapter.ViewHolder>{

    private Context context;
    private List<CountDownItem> countdownItemList;

    public CountDownItemAdapter(List<CountDownItem> countdownItemList){
        this.countdownItemList = countdownItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.count_down_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CountDownItem countDownItem = countdownItemList.get(position);
        holder.countDownDescribe.setText(countDownItem.getDescribe());
        holder.title.setText(countDownItem.getTitle());
        holder.targetDate.setText(countDownItem.getTargetDate());
        holder.describe.setText(countDownItem.getDescribe());
        holder.imageView.setImageResource(countDownItem.getImageId());
        holder.linearLayout.setBackgroundResource(countDownItem.getImageId());
    }

    @Override
    public int getItemCount() {
        return countdownItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        LinearLayout linearLayout;
        TextView countDownDescribe;
        TextView title;
        TextView targetDate;
        TextView describe;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            linearLayout = cardView.findViewById(R.id.count_down_item_linearlayout);
            countDownDescribe = cardView.findViewById(R.id.count_down_item_countdown_describe);
            title = cardView.findViewById(R.id.count_down_item_title);
            targetDate = cardView.findViewById(R.id.count_down_item_targetdata);
            describe = cardView.findViewById(R.id.rcount_down_item_describe);
            imageView = cardView.findViewById(R.id.count_down_item_image);
            linearLayout = cardView.findViewById(R.id.count_down_item_linearlayout);
        }

    }


}
