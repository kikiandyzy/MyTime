package com.example.mytime.DataStructure;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private Context context;
    private String[] ln = {"生日", "学习", "工作", "节假日", "长按删除标签"};
    public  ArrayList<String> labelName = new ArrayList<String>();
    private List<CountDownItem> countDownItemList = new ArrayList<>();


    private List<CountDownItem> initCountDownItemList() throws ParseException {

        int[] times = new int[]{2019,12,18,4,4,4};
        CountDownItem countDownItem1 = new CountDownItem(times,"标题1","第一个", null);

        times = new int[]{2019,12,16,4,4,4};

        CountDownItem countDownItem2 = new CountDownItem(times,"标题2","第二个",null);


        countDownItemList.add(countDownItem1);
        countDownItemList.add(countDownItem2);
        return countDownItemList;
    }

    public final static int REQUEST_CODE_NEW = 1;
    public final static int REQUEST_CODE_SHOW = 2;
    public final static int REQUEST_CODE_EDIT = 3;
    public final static int REQUEST_CHOOSE_PHOTO = 4;


    public final static int RESULT_CODE_DELETE = 10;
    public final static int RESULT_CODE_EDIT = 11;
    public final static String COUNTDOWNITEM = "countDownItem";
    public final static String POSITION = "position";
    public final static String THEMECOLOR = "themeColor";
    public final static String LABLENAME = "labelName";
    public final static String HEIGHT = "height";


    private int themeColor = -1;

    public Manager(Context context){
        this.context = context;
    }

    public List<CountDownItem> getCountDownItemList() {
        return countDownItemList;
    }

    public List<String> getLabelName(){
        return labelName;
    }

    public int getThemeColor(){
       return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    public void load(){
        //先开始读取存倒计时的文件
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("countDownItemList.txt")
            );
            countDownItemList = (ArrayList<CountDownItem>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            //如果没有该文件
            if(countDownItemList.size() == 0){
                countDownItemList = initCountDownItemList();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //存标签名的文件
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("labelNameList.txt")
            );
            labelName = (ArrayList<String>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

            //如果没有该文件
        if(labelName.size() == 0){
            labelName.add("生日");
            labelName.add("学习");
            labelName.add("工作");
            labelName.add("节假日");
            labelName.add("长按删除标签");
        }

        //存主题色
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("themeColor.txt")
            );
            themeColor = inputStream.readInt();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
    public void save(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("countDownItemList.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(countDownItemList);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("labelNameList.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(labelName);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("themeColor.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeInt(themeColor);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
