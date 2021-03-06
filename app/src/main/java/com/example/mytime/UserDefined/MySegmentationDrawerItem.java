package com.example.mytime.UserDefined;

import androidx.annotation.LayoutRes;

import com.example.mytime.R;
import com.mikepenz.materialdrawer.model.AbstractBadgeableDrawerItem;

public class MySegmentationDrawerItem extends AbstractBadgeableDrawerItem<MySegmentationDrawerItem>{
    @Override
    public int getType() {
        return R.id.mysegmentationdraweritem;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {

        return R.layout.mysegmentationdraweritem;
    }

}
