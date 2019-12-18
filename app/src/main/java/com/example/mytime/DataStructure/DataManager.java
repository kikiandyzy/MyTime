package com.example.mytime.DataStructure;

import android.app.Application;

import java.util.ArrayList;
import java.util.Arrays;

public class DataManager extends Application {
    private String[] ln = {"生日","学习","工作","节假日","长按删除标签"};
    public ArrayList<String> labelName = new ArrayList<String>(Arrays.asList(ln));
}
