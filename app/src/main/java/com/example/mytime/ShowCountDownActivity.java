package com.example.mytime;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.example.mytime.DataStructure.CountDownItem;
import com.example.mytime.DataStructure.Manager;

import java.util.ArrayList;

public class ShowCountDownActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private int position;
    private CardView cardView;
    private TextView title;
    private TextView targetDate;
    private TextView countDown;
    private TextView describe;


    private CountDownItem countDownItem;
    private ImageView imageView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    countDown.setText(countDownItem.getCountDownString());
                    break;
                default:
                    break;
            }
        }
    };
    private ShowThread showThread;
    private boolean ifEdit = false;
    private LinearLayout linearLayoutNotification;
    //Intent传过来的配置参数
    private int themeColor;
    private ArrayList<String> labelName = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_count_down_item);


        toolbar = findViewById(R.id.activity_show_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        cardView = findViewById(R.id.cardview_activity_show);

        title = findViewById(R.id.textview_title_activity_show);
        targetDate = findViewById(R.id.textview_target_date_activity_show);
        countDown = findViewById(R.id.textview_count_down_activity_show);
        describe = findViewById(R.id.textview_describe_activity_show);
        imageView = findViewById(R.id.activity_show_imageview);

        Intent intent = getIntent();
        countDownItem = (CountDownItem) intent.getSerializableExtra(Manager.COUNTDOWNITEM);
        position = intent.getIntExtra(Manager.POSITION,0);
        title.setText(countDownItem.getTitle());
        targetDate.setText(countDownItem.getTargetDateParticular());
        countDown.setText(countDownItem.getCountDownString());
        describe.setText(countDownItem.getDescribe());
        if(countDownItem.getBitmap() != null){
            imageView.setImageBitmap(countDownItem.getBitmap());
        }else {
            imageView.setImageResource(countDownItem.getImageId());
        }
        themeColor = intent.getIntExtra(Manager.THEMECOLOR,R.color.themeColor);
        labelName = intent.getStringArrayListExtra(Manager.LABLENAME);

        ViewGroup.LayoutParams lp = cardView.getLayoutParams();
        int height  = intent.getIntExtra(Manager.HEIGHT,1704);
        lp.height = height -900;//900是toolbar的长度
        cardView.setLayoutParams(lp);

        linearLayoutNotification = findViewById(R.id.linearlayout_notification);
        linearLayoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        //开始计时线程
        showThread = new ShowThread();
        new Thread(showThread).start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //传入菜单
        getMenuInflater().inflate(R.menu.menu_toolbar_activity_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(ifEdit){
                    Intent  intent = new Intent(ShowCountDownActivity.this,MainActivity.class);
                    intent.putExtra(Manager.POSITION,position);
                    intent.putExtra(Manager.COUNTDOWNITEM,countDownItem);
                    setResult(Manager.RESULT_CODE_EDIT,intent);
                }
                finish();
                break;
            case R.id.menu_item_delete:
                buildDeleteDialog();

                break;
            case R.id.menu_item_edit:
                Intent  intent1 = new Intent(ShowCountDownActivity.this,EditCountDownActivity.class);
                intent1.putExtra(Manager.COUNTDOWNITEM,countDownItem);
                intent1.putExtra(Manager.THEMECOLOR,themeColor);
                intent1.putExtra(Manager.LABLENAME,labelName);
                startActivityForResult(intent1,Manager.REQUEST_CODE_EDIT);
                break;
        }
        return  true;
    }

    private class ShowThread extends Thread{


        private boolean beAlive = true;
        @Override
        public void run() {
            while (beAlive){
                try{
                    Message message = new Message();
                    message.what = 1;
                    countDownItem.updateCountDownTime();
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
        if(showThread != null){
            showThread.stopThread();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Manager.REQUEST_CODE_EDIT:
                if (resultCode ==RESULT_OK){
                    countDownItem = (CountDownItem) data.getSerializableExtra(Manager.COUNTDOWNITEM);
                    title.setText(countDownItem.getTitle());
                    targetDate.setText(countDownItem.getTargetDateParticular());
                    describe.setText(countDownItem.getDescribe());
                    if(countDownItem.getBitmap() != null){
                        imageView.setImageBitmap(countDownItem.getBitmap());
                    }else {
                        imageView.setImageResource(countDownItem.getImageId());
                    }
                    ifEdit = true;
                }
                break;
        }
    }

    //返回键返回函数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK&&event.getRepeatCount() == 0){
            if(ifEdit){
                Intent  intent = new Intent(ShowCountDownActivity.this,MainActivity.class);
                intent.putExtra(Manager.POSITION,position);
                intent.putExtra(Manager.COUNTDOWNITEM,countDownItem);
                setResult(Manager.RESULT_CODE_EDIT,intent);
            }
            finish();
        }
        return false;
    }

    //创建删除确认对话框
    private void buildDeleteDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ShowCountDownActivity.this);
        dialog.setTitle("是否删除该计时");
        //dialog.setMessage("删除标签不会删除所包含的倒计时");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent  intent = new Intent(ShowCountDownActivity.this,MainActivity.class);
                intent.putExtra(Manager.POSITION,position);
                setResult(Manager.RESULT_CODE_DELETE,intent);
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    //开启通知的相关设置

    private Notification notification;
    private void sendNotification(){
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification = new NotificationCompat.Builder(this)
                .setContentTitle(countDownItem.getTitle())
                .setContentText(countDownItem.getTargetDateSimple()+" timing...")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.date_activity_edit)
                .build();
        manager.notify(1,notification);
    }




}
