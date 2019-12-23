package com.example.mytime.DataStructure;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mytime.R;
import com.example.mytime.UserDefined.MyDataFormat;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CountDownItem implements Serializable {
    private Long time;
    private String title;
    private String describe;
    private int imageId;
    private int[] times;
    private byte[] bitmapBytes = null;



    public CountDownItem(int[] times,String title,String describe,Bitmap bitmap) throws ParseException {
        this.times = times;
        this.title = title;
        this.describe = describe;
        this.bitmapBytes = changeByte(bitmap);
        imageId = getRandomImage();
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
        this.bitmapBytes = countDownItem.getBitmapBytes();
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

    public Bitmap getBitmap() {

        if(bitmapBytes != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            return bitmap;
        }
        else
        {
            return null;
        }

    }

    public void setBitmapBytes(Bitmap bm) {
        if(bm != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            this.bitmapBytes = baos.toByteArray();
        }

    }

    public byte[] getBitmapBytes() {
        return bitmapBytes;
    }

    public static byte[] changeByte(Bitmap bm){
        if(bm != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }
       else {
           return null;
        }
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

    private int getRandomImage(){
        Random random = new Random();
        int id = R.drawable.user;//默认为这张照片
        switch (random.nextInt(6)){
            case 0:
                id = R.drawable.i0;
                break;
            case 1:
                id = R.drawable.i1;
                break;
            case 2:
                id = R.drawable.i2;
                break;
            case 3:
                id = R.drawable.i3;
                break;
            case 4:
                id = R.drawable.i4;
                break;
            case 5:
                id = R.drawable.i5;
                break;
            default:
                break;
        }
        return id;
    }
}
