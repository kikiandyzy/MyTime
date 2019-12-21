package com.example.mytime.DataStructure;

import com.example.mytime.UserDefined.MyDataFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CountDownItem implements Serializable {
    private Long time;
    private String title;
    private String describe;
    private int imageId;
    private int[] times;


    public CountDownItem(int[] times,String title,String describe ,int imageId) throws ParseException {
        this.times = times;
        this.title = title;
        this.describe = describe;
        this.imageId = imageId;
        time = MyDataFormat.setCoountDownItemTime(times[0],times[1],times[2],times[3],times[4],times[5]);
    }

    public int[] getTimes() {
        return times;
    }

    public void update(CountDownItem countDownItem) throws ParseException {
        this.times = countDownItem.getTimes();
        this.title = countDownItem.getTitle();
        this.describe = countDownItem.getDescribe();
        this.imageId = countDownItem.getImageId();
        time = MyDataFormat.setCoountDownItemTime(times[0],times[1],times[2],times[3],times[4],times[5]);
    }

    public void setTimes(int[] times) {
        this.times = times;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTargetDateSimple() {
        return ""+times[0]+"-"+times[1]+"-"+times[2];
    }

    public String getTargetDateParticular(){
        return ""+times[0]+"-"+times[1]+"-"+times[2]+" "+times[3]+":"+times[4]+":"+times[5];
    }



    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    //这个刷新时候用到的信息

    private List<String> stringList = new ArrayList<>();

    int i=0;
    public void updateCountDownTime(){
        stringList = MyDataFormat.getCountDownItemCountDownTimeStringList(time);
    }

    public String getRecycleViewItemCountDownDescribe(){
        if(stringList.size() != 0){
            return stringList.get(0);
        }
        return "还剩";
    }

    public String getRecycleViewItemCountDownTime(){
        if(stringList.size() != 0){
            return stringList.get(1);
        }
        return "0秒";
    }

    public String getCountDownString(){
        if(stringList.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=1;i<stringList.size();i++){
                stringBuilder.append(stringList.get(i));
            }
            return stringBuilder.toString();
        }

        return "";
    }
    private String label = "";

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    public CountDownItem(){}
}
