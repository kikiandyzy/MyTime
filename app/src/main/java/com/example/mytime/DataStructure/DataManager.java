package com.example.mytime.DataStructure;

import android.app.Application;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mytime.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager extends Application {
    private String[] ln = {"生日","学习","工作","节假日","长按删除标签"};
    public ArrayList<String> labelName = new ArrayList<String>(Arrays.asList(ln));
    public ConstraintLayout constraintLayout;
    private List<CountDownItem> countDownItemList = new ArrayList<>();
    public List<CountDownItem> initCountDownItemList() throws ParseException {

        int[] times = new int[]{2019,12,18,4,4,4};
        CountDownItem countDownItem1 = new CountDownItem(times,"标题1","第一个", R.drawable.user);

        times = new int[]{2019,12,16,4,4,4};

        CountDownItem countDownItem2 = new CountDownItem(times,"标题2","第二个",R.drawable.user);


        countDownItemList.add(countDownItem1);
        countDownItemList.add(countDownItem2);
        return countDownItemList;
    }

    public final static int REQUEST_CODE_NEW = 1;
    public final static int REQUEST_CODE_SHOW = 2;
    public final static int REQUEST_CODE_EDIT = 2;

    public final static int RESULT_CODE_DELETE = 4;
    public final static int RESULT_CODE_EDIT = 5;
    public final static String COUNTDOWNITEM = "countDownItem";
    public final static String POSITION = "position";


}
