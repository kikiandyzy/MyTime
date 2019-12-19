package com.example.mytime.UserDefined;

import com.freegeek.android.materialbanner.holder.ViewHolderCreator;

import java.util.ArrayList;
import java.util.List;

public class MyHolderCreator implements ViewHolderCreator {

    List<MyHolder> myHolderList = new ArrayList<>();
    @Override
    public MyHolder createHolder() {
        MyHolder myHolder = new MyHolder();
        myHolderList.add(myHolder);
        return myHolder;
    }

    public void update(){

        for(int i = 0;i<myHolderList.size();i++){
            myHolderList.get(i).update();
        }
    }

    public void removeMyHolder(int i){
        myHolderList.remove(i);
    }
}
