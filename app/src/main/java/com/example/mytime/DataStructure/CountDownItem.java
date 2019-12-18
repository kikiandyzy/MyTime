package com.example.mytime.DataStructure;

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
}
