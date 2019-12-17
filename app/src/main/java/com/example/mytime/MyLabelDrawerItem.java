package com.example.mytime;

import androidx.annotation.LayoutRes;

import com.mikepenz.materialdrawer.model.AbstractBadgeableDrawerItem;

public class MyLabelDrawerItem extends AbstractBadgeableDrawerItem<MyLabelDrawerItem> {


    @Override
    public int getType() {
        return R.id.mylabeldraweritem;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {

        return R.layout.mylabeldraweritem;
    }


}
