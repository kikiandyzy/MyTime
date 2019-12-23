package com.example.mytime.UserDefined;

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

public class CountDownItemAdapter extends RecyclerView.Adapter<CountDownItemAdapter.ViewHolder> implements View.OnClickListener{



    private List<CountDownItem> countdownItemList;
    private OnItemClickListener mOnItemClickListener = null;


    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public CountDownItemAdapter(List<CountDownItem> countdownItemList){
        this.countdownItemList = countdownItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.count_down_item,parent,false);
        view.setOnClickListener(this::onClick);
        final ViewHolder viewHolder = new ViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CountDownItem countDownItem = countdownItemList.get(position);
        holder.countDownDescribe.setText(countDownItem.getRecycleViewItemCountDownDescribe());
        holder.countDownTime.setText(countDownItem.getRecycleViewItemCountDownTime());
        holder.title.setText(countDownItem.getTitle());
        holder.targetDate.setText(countDownItem.getTargetDateSimple());
        holder.describe.setText(countDownItem.getDescribe());
        if(countDownItem.getBitmap() != null){
            holder.imageView.setImageBitmap(countDownItem.getBitmap());
            holder.background.setImageBitmap(countDownItem.getBitmap());

        }else {
            holder.imageView.setImageResource(countDownItem.getImageId());
            holder.background.setImageResource(countDownItem.getImageId());
        }

        holder.cardView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return countdownItemList.size();
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener != null){
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        private LinearLayout linearLayout;
        private TextView countDownDescribe;
        private TextView countDownTime;
        private TextView title;
        private TextView targetDate;
        private TextView describe;
        private ImageView imageView;
        private ImageView background;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            background = cardView.findViewById(R.id.background_count_down_item);
            countDownDescribe = cardView.findViewById(R.id.count_down_item_countdown_describe);
            countDownTime = cardView.findViewById(R.id.count_down_item_countdown_time);
            title = cardView.findViewById(R.id.count_down_item_title);
            targetDate = cardView.findViewById(R.id.count_down_item_targetdata);
            describe = cardView.findViewById(R.id.rcount_down_item_describe);
            imageView = cardView.findViewById(R.id.count_down_item_image);


        }



    }


    //这个接口是为了mainActiivty调用
    public static interface OnItemClickListener{
        void onItemClick(View v , int position);
    }






}
