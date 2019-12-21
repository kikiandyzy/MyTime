package com.example.mytime;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytime.DataStructure.CountDownItem;
import com.example.mytime.DataStructure.DataManager;
import com.example.mytime.UserDefined.CountDownItemAdapter;
import com.example.mytime.UserDefined.MyDialog;
import com.example.mytime.UserDefined.MyHolderCreator;
import com.example.mytime.UserDefined.MyLabelDrawerItem;
import com.example.mytime.UserDefined.MySegmentationDrawerItem;
import com.freegeek.android.materialbanner.MaterialBanner;
import com.freegeek.android.materialbanner.view.indicator.CirclePageIndicator;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import java.text.ParseException;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    //全局变量
    private DataManager dataManager;
    //主页面最外层布局
    private ConstraintLayout constraintLayout;
    //侧滑菜单
    private Drawer drawer;
    //recyclerview以及配适器
    private RecyclerView recyclerView;
    private List<CountDownItem> countDownItemList;
    private CountDownItemAdapter countDownItemAdapter;

    //主页面上的AppBarLayout
    //toolbar的父布局，可以实现收缩
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    //悬浮按钮
    FloatingActionButton floatingActionButton;

    //首页的展示横幅
    MaterialBanner materialBanner;
    MyHolderCreator myHolderCreator;

    //子线程和异步消息处理
    private CountDownThread countDownThread;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                   countDown();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.constraintlayout_activity_main);
        //获取后台数据
        dataManager = (DataManager)getApplication();
        dataManager.constraintLayout = constraintLayout;
        //初始化主页面的AppBar
        initAppBarLayout();
        //初始化侧滑抽屉菜单
        initSildingMeun();





        //初始化recyclerview
        try {
            initRecyclerView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //初始化悬浮按钮
        initFloatingActionButton();

        //初始化横幅
        initMaterialBanner();


        //开始计时线程
        countDownThread = new CountDownThread();
        new Thread(countDownThread).start();

    }

    //点击
    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://弹出滑动菜单
                drawer.openDrawer();
                break;
        }
        return true;
    }


    //*****************************************************************************************************侧滑菜单的配置
    //创建滑动菜单以及相关配置
    private void  initSildingMeun(){


        //创建登录对象
        IProfile profile = new ProfileDrawerItem()
                .withName("kikiandyzy")
                .withEmail("874432636@qq.com")
                .withIcon(R.drawable.user)
                .withIdentifier(100);


        //头部的布局
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)//用来设置是否启用沉浸式状态栏；
                .withHeaderBackground(R.color.colorPrimary)//用来设置背景图片，注意这里无论是用户头像还是背景图片都可以使用来自网络上的图片；
                .addProfiles(profile)//用于添加用户对象，可以添加多个，使用逗号进行隔开；
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {//将意外被杀掉的Activity的状态设置回来。
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        switch ((int)profile.getIdentifier()) {
                            case 100:
                                Toast.makeText(MainActivity.this,"the icon is clicked",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                })
                .withSelectionListEnabled(false)//关闭头部上用户信息的小三角按钮

                //.withSavedInstance(savedInstanceState)
                .build();


        //标签子菜单




        //创建一个伸缩式item，标签的主菜单
        final ExpandableBadgeDrawerItem expandableBadgeDrawerItem_laber = new ExpandableBadgeDrawerItem()
                .withName("标签")
                .withIcon(R.drawable.drawer_item_labels)
                .withIdentifier(2);
        for(int i=0;i<dataManager.labelName.size();i++){
            expandableBadgeDrawerItem_laber.withSubItems(new MyLabelDrawerItem().withIcon(R.drawable.drawer_item_label)
                    .withName(dataManager.labelName.get(i))
                    .withIdentifier(10)
                    );
        }
        expandableBadgeDrawerItem_laber.withSubItems(new MyLabelDrawerItem()
                .withIcon(R.drawable.drawer_item_add_label)
                .withName("点击添加")
                .withIdentifier(9));





        //创建滑动菜单
        //new DrawerBuilder().withActivity(this).withAccountHeader(headerResult).build();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("计时")
                                //.withDescription("This is a user")
                                .withIcon(R.drawable.drawer_item_countdown)
                                .withIdentifier(1)
                                .withSelectable(false),
                       expandableBadgeDrawerItem_laber,
                        new PrimaryDrawerItem()
                                .withName("小部件")
                                //.withDescription("This is a user")
                                .withIcon(R.drawable.drawer_item_component)
                                .withIdentifier(3)
                                .withSelectable(false),
                        new PrimaryDrawerItem()
                                .withName("主题色")
                                //.withDescription("This is a user")
                                .withIcon(R.drawable.drawer_item_color)
                                .withIdentifier(4)
                                .withSelectable(false),
                        new PrimaryDrawerItem()
                                .withName("高级版")
                                //.withDescription("This is a user")
                                .withIcon(R.drawable.drawer_item_high_level)
                                .withIdentifier(5)
                                .withSelectable(false),
                        new MySegmentationDrawerItem(),//.withName("menu组"),  //分组item，类似于group标签，无点击效果
                        new PrimaryDrawerItem()
                                .withName("设置")
                                //.withDescription("This is a user")
                                .withIcon(R.drawable.drawer_item_settings)
                                .withIdentifier(6)
                                .withSelectable(false),
                        new PrimaryDrawerItem()
                                .withName("关于")
                                //.withDescription("This is a user")
                                .withIcon(R.drawable.drawer_item_about)
                                .withIdentifier(7)
                                .withSelectable(false),
                        new PrimaryDrawerItem()
                                .withName("帮助与反馈")
                                //.withDescription("This is a user")
                                .withIcon(R.drawable.drawer_item_feedback)
                                .withIdentifier(8)
                                .withSelectable(false)
                        /*new SwitchDrawerItem()  //添加带有switch开关的item
                                .withName("开关item1")
                                .withIcon(R.drawable.ic_android_black_24dp)
                                .withIdentifier(3)
                                .withCheckable(false)
                                .withOnCheckedChangeListener(checkedChangeListener),
                        new SwitchDrawerItem()
                                .withName("开关item2")
                                .withIcon(R.drawable.ic_android_black_24dp)
                                .withIdentifier(4)
                                .withOnCheckedChangeListener(checkedChangeListener)
                                .withChecked(true)  //设置默认为ON状态*/
                )
                //.withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)  //设置为默认启动抽屉菜单
                .build();

        //////////设计点击事件
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if(9 == drawerItem.getIdentifier()){

                    buildAddLabelDialog(position-1);
                }else if(4 == drawerItem.getIdentifier()){
                    buildChooseColorDialog();
                }
                return false;
            }
        });
        drawer.setOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                if(10 == drawerItem.getIdentifier()){
                    MyLabelDrawerItem m = (MyLabelDrawerItem)drawerItem;
                    buildDeleteLabelDialog(m.getName(),position);

                }
                return false;
            }
        });
    }
    //添加一个标签
    private MyLabelDrawerItem addLabel(String name){
        return new MyLabelDrawerItem().withName(name).withIcon(R.drawable.drawer_item_label).withIdentifier(10);
    }
    //弹出一个确认删除标签的对话框
    private void buildDeleteLabelDialog(StringHolder name, final int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("是否删除["+name+"]标签");
        dialog.setMessage("删除标签不会删除所包含的倒计时");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawer.removeItemByPosition(position);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
    //弹出一个确认添加标签的对话框
    private void buildAddLabelDialog(final int position){
        View view1 = getLayoutInflater().inflate(R.layout.dialog_add_label, null);
        int width = constraintLayout.getWidth();//获取最外层布局的宽度
        //宽度和高度即设置的对话框的大小
        final MyDialog myDialog = new MyDialog(MainActivity.this, width-150, 500, view1);
        myDialog.setCancelable(true);
        myDialog.show();

        //注册点击事件
        myDialog.getWindow().findViewById(R.id.add_label_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().findViewById(R.id.add_label_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = myDialog.getWindow().findViewById(R.id.add_label_edittext);
                String label = "";
                label = editText.getText().toString();
                if(!label.equals("")){
                    int exist = 0;
                    for (String s :dataManager.labelName) {
                        if(label.equals(s)){
                            exist = 1;
                            break;
                        }
                    }
                    if(1 == exist){
                        Toast.makeText(MainActivity.this, "标签已存在", Toast.LENGTH_SHORT).show();
                    }else {
                        dataManager.labelName.add(label);
                        drawer.addItemAtPosition(new MyLabelDrawerItem().withName(label).withIcon(R.drawable.drawer_item_label).withIdentifier(10),position);
                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    }

                    myDialog.dismiss();
                }else {
                    Toast.makeText(MainActivity.this, "标签名称不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //弹出一个选择颜色的对话框
    private void buildChooseColorDialog(){
        //先保存原有的颜色
        final ColorStateList colorStateList = floatingActionButton.getBackgroundTintList();
        final Drawable drawable = collapsingToolbarLayout.getContentScrim();

        View view1 = getLayoutInflater().inflate(R.layout.dialog_choose_color, null);
        int width = constraintLayout.getWidth();//获取最外层布局的宽度
        //宽度和高度即设置的对话框的大小
        final MyDialog myDialog = new MyDialog(MainActivity.this, width-150, 500, view1);
        myDialog.setCancelable(true);
        myDialog.show();

        //设置一下颜色的样式
        ColorSeekBar colorSeekBar = myDialog.getWindow().findViewById(R.id.colorSlider);
        colorSeekBar.setMaxPosition(100);
        colorSeekBar.setColorSeeds(R.array.material_colors); // material_colors is defalut included in res/color,just use it.
        colorSeekBar.setColorBarPosition(10); //0 - maxValue
        //colorSeekBar.setAlphaBarPosition(10); //0 - 255
        colorSeekBar.setShowAlphaBar(false);
        colorSeekBar.setBarHeight(5); //5dpi
        colorSeekBar.setThumbHeight(30); //30dpi
        colorSeekBar.setBarMargin(10);
        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                //textView.setTextColor(color);
                //colorSeekBar.getAlphaValue();

                dataManager.themeColor = color;
                collapsingToolbarLayout.setContentScrimColor(color);
                floatingActionButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                floatingActionButton.setBackgroundTintList(createColorStateList(color,color,color,color));

            }
        });

        //设置取消和确定的点击事件
        { myDialog.getWindow().findViewById(R.id.choose_color_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsingToolbarLayout.setContentScrim(drawable);
                floatingActionButton.setBackgroundTintList(colorStateList);
                myDialog.dismiss();
            }
        });
            myDialog.getWindow().findViewById(R.id.choose_color_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    myDialog.dismiss();
                }
            });}
    }
    //为悬浮按钮设置颜色
    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    //*****************************************************************************************************recyclerview的配置
    private void initRecyclerView() throws ParseException {
        recyclerView = findViewById(R.id.recyclerview_activity_mian);
        countDownItemList = dataManager.initCountDownItemList();
        countDownItemAdapter = new CountDownItemAdapter(countDownItemList);
        countDownItemAdapter.setmOnItemClickListener(new CountDownItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this,ShowCountDownActivity.class);
                intent.putExtra(DataManager.COUNTDOWNITEM,countDownItemList.get(position));
                intent.putExtra(DataManager.POSITION,position);
                startActivityForResult(intent,DataManager.REQUEST_CODE_SHOW);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(countDownItemAdapter);

    }

    //*****************************************************************************************************AppBarLayout的配置
    private void initAppBarLayout(){
        //toolbar加载
        toolbar = findViewById(R.id.toolbar_activity_mian);
        //toolbar外面的布局
        collapsingToolbarLayout =  findViewById(R.id.layout_collapsing_toolbar_activity_mian);
        //折叠前后的颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));
        //绑定
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //加上了一张图片作为弹出菜单的按钮
        actionBar.setHomeAsUpIndicator(R.drawable.menu_activity_main);

    }

    //*****************************************************************************************************悬浮按钮的配置
    private void initFloatingActionButton(){
        floatingActionButton = findViewById(R.id.floating_action_button_activity_mian);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditCountDownActivity.class);
                startActivityForResult(intent, DataManager.REQUEST_CODE_NEW);

            }
        });
    }

    //*****************************************************************************************************横幅的配置
    private void initMaterialBanner(){
        materialBanner = findViewById(R.id.material_banner_activity_main);
        myHolderCreator = new MyHolderCreator();
        materialBanner.setPages(myHolderCreator,countDownItemList).setIndicator(new CirclePageIndicator(this));
        materialBanner.setOnItemClickListener(new MaterialBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                //启动对应项
                Intent intent = new Intent(MainActivity.this,ShowCountDownActivity.class);
                intent.putExtra(DataManager.COUNTDOWNITEM,countDownItemList.get(i));
                intent.putExtra(DataManager.POSITION,i);
                startActivityForResult(intent,DataManager.REQUEST_CODE_SHOW);
            }
        });

    }

    //*****************************************************************************************************子线程的配置

    private class CountDownThread extends Thread{


        private boolean beAlive = true;
        @Override
        public void run() {
            while (beAlive){
                try{
                    Message message = new Message();
                    message.what = 1;
                    for(int i=0;i<countDownItemList.size();i++){
                        countDownItemList.get(i).updateCountDownTime();
                    }
                    handler.sendMessage(message);
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }

        public void stopThread(){
            beAlive =false;
            while (true){
                try{
                    this.join();
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownThread != null){
            countDownThread.stopThread();
        }
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case DataManager.REQUEST_CODE_NEW:
                if(resultCode == RESULT_OK){
                    CountDownItem countDownItem = (CountDownItem) data.getSerializableExtra(DataManager.COUNTDOWNITEM);
                    countDownItemList.add(countDownItem);
                    notifyDataSetChanged();
                }
                break;
                case DataManager.REQUEST_CODE_SHOW:
                    if(resultCode == DataManager.RESULT_CODE_DELETE){
                        //Toast.makeText(this, ""+data.getIntExtra(DataManager.POSITION,0), Toast.LENGTH_SHORT).show();
                        countDownItemList.remove(data.getIntExtra(DataManager.POSITION,0));
                        notifyDataSetChanged();
                    }else if(resultCode == DataManager.RESULT_CODE_EDIT){
                        try {
                            countDownItemList.get(data.getIntExtra(DataManager.POSITION,0)).update((CountDownItem) data.getSerializableExtra(DataManager.COUNTDOWNITEM));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    }
                default:
                    break;
        }
    }


    //计时更新
    private void countDown(){
        countDownItemAdapter.notifyDataSetChanged();
        myHolderCreator.update();
    }

    //countdownitem更新操作
    private void notifyDataSetChanged(){
        countDownItemAdapter.notifyDataSetChanged();

        myHolderCreator = new MyHolderCreator();
        materialBanner.setPages(myHolderCreator,countDownItemList).setIndicator(new CirclePageIndicator(this));
    }

}



