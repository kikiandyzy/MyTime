package com.example.mytime.DataStructure;

import com.example.mytime.UserDefined.MyDataFormat;

import java.util.ArrayList;
import java.util.List;

public class CountDownItem {
    private Long time;
    private String title;
    private String describe;
    private String targetDate;
    private int imageId;


    public CountDownItem(Long time,String title,String targetDate,String describe ,int imageId){
        this.time = time;
        this.title = title;
        this.describe = describe;
        this.targetDate = targetDate;
        this.imageId = imageId;
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

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
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

    public String getMaterialBannerItemCountDown(){
        if(stringList.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=1;i<stringList.size();i++){
                stringBuilder.append(stringList.get(i));
            }
            return stringBuilder.toString();
        }

        return "";
    }
}
