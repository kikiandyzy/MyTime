package com.example.mytime;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytime.DataStructure.CountDownItem;
import com.example.mytime.DataStructure.DataManager;
import com.example.mytime.UserDefined.CountDownItemAdapter;
import com.example.mytime.UserDefined.MyDataFormat;
import com.example.mytime.UserDefined.MyDialog;
import com.example.mytime.UserDefined.MyLabelDrawerItem;
import com.example.mytime.UserDefined.MySegmentationDrawerItem;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //全局变量
    private DataManager dataManager;
    //侧滑菜单
    private Drawer drawer;
    //recyclerview
    private RecyclerView recyclerView;
    private List<CountDownItem> countDownItemList;
    private CountDownItemAdapter countDownItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取后台数据
        dataManager = (DataManager)getApplication();
        //初始化侧滑抽屉菜单
        initSildingMeun();

        //initTextView();
        //countDownThread = new CountDownThread();
        //new Thread(countDownThread).start();

        //初始化recyclerview
        try {
            initRecyclerView();
        } catch (ParseException e) {
            e.printStackTrace();
        }



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
        //先获取设备屏幕大小
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;



        //宽度和高度即设置的对话框的大小
        final MyDialog myDialog = new MyDialog(MainActivity.this, y-800, 500, view1);
        myDialog.setCancelable(true);
        myDialog.show();
        /*
        //设置大小
         final WindowManager.LayoutParams params = myDialog.getWindow().getAttributes();
        params.width = 1000;
        params.height = 1000;
        myDialog.getWindow().setAttributes(params);
        * */

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
        //final ColorStateList colorStateList = floatingActionButton.getBackgroundTintList();
        //final Drawable drawable = collapsingToolbarLayout.getContentScrim();

        //先获取设备屏幕大小
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;
        View view1 = getLayoutInflater().inflate(R.layout.dialog_choose_color, null);
        final MyDialog myDialog = new MyDialog(MainActivity.this, y-800, 500, view1);
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

                //collapsingToolbarLayout.setContentScrimColor(color);
                //floatingActionButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                //floatingActionButton.setBackgroundTintList(createColorStateList(color,color,color,color));

            }
        });

        //设置取消和确定的点击事件
        { myDialog.getWindow().findViewById(R.id.choose_color_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collapsingToolbarLayout.setContentScrim(drawable);
                //floatingActionButton.setBackgroundTintList(colorStateList);
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
        recyclerView = findViewById(R.id.recyclerview);
        countDownItemList = new ArrayList<>();
        long timeInMillis = MyDataFormat.setCoountDownItemTime(2019,12,18,4,4,4);
        CountDownItem countDownItem1 = new CountDownItem(timeInMillis,"标题","18号4点","第一个",R.drawable.user);

        timeInMillis = MyDataFormat.setCoountDownItemTime(2019,12,16,4,4,4);
        CountDownItem countDownItem2 = new CountDownItem(timeInMillis,"标题","16号4点","第二个",R.drawable.user);

        countDownItemList.add(countDownItem1);
        countDownItemList.add(countDownItem2);

        countDownItemAdapter = new CountDownItemAdapter(countDownItemList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(countDownItemAdapter);
    }


    //*****************************************************************************************************子线程的配置
    /*
    private TextView textView;
    private void initTextView(){
      textView = findViewById(R.id.textview);
    }
    private class CountDownThread extends Thread{

        private int i = 0;
        private boolean beAlive = true;
        @Override
        public void run() {
            while (beAlive){
                try{
                    Message message = new Message();
                    message.what = 1;
                    i+=1;
                    message.arg1 = i;
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    textView.setText(""+msg.arg1);
                    break;
                    default:
                        break;
            }
        }
    };
    private CountDownThread countDownThread;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownThread != null){
            countDownThread.stopThread();
        }

    }
    */





   





}



