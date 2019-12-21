package com.example.mytime;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mytime.DataStructure.CountDownItem;
import com.example.mytime.DataStructure.DataManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;

public class EditCountDownActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    private DataManager dataManager;
    private Toolbar toolbar;
    private LinearLayout date;
    private LinearLayout repetition;
    private LinearLayout image;
    private LinearLayout addLabel;
    private EditText title;
    private EditText describe;
    private TextView showDate;



    //日期和时间选择器
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int[] times = new int[]{1,2,3,4,5,6};
    private boolean ifTime = false;
    CountDownItem countDownItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_count_down);
        dataManager = (DataManager)getApplication();

        toolbar = findViewById(R.id.toolbar_activit_edit);
        toolbar.setTitle("");
        if(dataManager.themeColor != -1){
            toolbar.setBackgroundColor(dataManager.themeColor);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title_edittext_activity_edit);
        describe = findViewById(R.id.describe_edittext_activity_edit);
        showDate = findViewById(R.id.show_date);


        initOptions();

        //接下来查看是哪个活动来的
        Intent intent = getIntent();
        countDownItem = (CountDownItem) intent.getSerializableExtra("countDownItem");
        if (countDownItem != null){
            title.setText(countDownItem.getTitle());
            describe.setText(countDownItem.getDescribe());
            showDate.setText(countDownItem.getTargetDateParticular());
        }
        buildDatePicker();
        buildTimePicker();




    }
    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //传入菜单
        getMenuInflater().inflate(R.menu.menu_toolbar_activity_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_ok:
                try {
                    judgeInputReasonable();
                } catch (ParseException e) {


                }
                break;
                default:
                    break;
        }
        return  true;
    }


    private void initOptions(){
        //四个选项
        date = findViewById(R.id.date_lin_activity_edit);
        repetition = findViewById(R.id.repetition_lin_activity_edit);
        image = findViewById(R.id.image_lin_activity_edit);
        addLabel = findViewById(R.id.add_label_lin_activity_edit);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });

    }

    private void buildDatePicker(){
        Calendar now = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(
                EditCountDownActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection

        );
        //dpd.setVersion(DatePickerDialog.Version.VERSION_1);

        if(dataManager.themeColor != -1){
            datePickerDialog.setAccentColor(dataManager.themeColor);
        }else {
            datePickerDialog.setAccentColor(getResources().getColor(R.color.themeColor));
        }

        //datePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private void buildTimePicker(){
        Calendar now = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(
                EditCountDownActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.HOUR),false);
        if(dataManager.themeColor != -1){
            timePickerDialog.setAccentColor(dataManager.themeColor);
        }else {
            timePickerDialog.setAccentColor(getResources().getColor(R.color.themeColor));
        }
        //timePickerDialog.show(getSupportFragmentManager(), "Timepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //Toast.makeText(this, "y"+year+"m"+monthOfYear+"d"+dayOfMonth, Toast.LENGTH_SHORT).show();
        times[0] = year;
        times[1] = monthOfYear+1;
        times[2] = dayOfMonth;
        timePickerDialog.show(getSupportFragmentManager(),"Timepickerdialog");

    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        //Toast.makeText(this, "h"+hourOfDay+"m"+minute+"s"+second, Toast.LENGTH_SHORT).show();
        times[3] = hourOfDay;
        times[4] = minute;
        times[5] = second;
        showDate.setText(""+times[0]+"/"+times[1]+"/"+times[2]+"  "+times[3]+"/"+times[4]+"/"+times[5]);
        ifTime = true;
    }

    //判断编辑和新建项目的合理性
    private void judgeInputReasonable() throws ParseException {
        if(!(title.getText().toString().equals(""))){
            if(ifTime){//如果选择了日期就无论是新建还是编辑统一处理
                CountDownItem temp = new CountDownItem(times,title.getText().toString(),describe.getText().toString(),R.drawable.user);
                Intent intent;
                if(countDownItem == null){//mainActivity
                    intent = new Intent(this,MainActivity.class);
                }else {
                    intent = new Intent(this,ShowCountDownActivity.class);
                }
                intent.putExtra(DataManager.COUNTDOWNITEM,temp);
                setResult(RESULT_OK,intent);
                finish();
            }else {//没有选择日期
                CountDownItem temp;
                Intent intent;
                if(countDownItem != null){//用原来的日期
                    temp = new CountDownItem(countDownItem.getTimes(),title.getText().toString(),describe.getText().toString(),R.drawable.user);
                    intent = new Intent(this,ShowCountDownActivity.class);
                }else {//如果是新建，就用现在的时间
                    Calendar now = Calendar.getInstance();
                    times[0] = now.get(Calendar.YEAR);
                    times[1] = now.get(Calendar.MONTH)+1;
                    times[2] = now.get(Calendar.DAY_OF_MONTH);
                    times[3] = now.get(Calendar.HOUR_OF_DAY);
                    times[4] = now.get(Calendar.MINUTE);
                    times[5] = now.get(Calendar.SECOND);
                    temp = new CountDownItem(times,title.getText().toString(),describe.getText().toString(),R.drawable.user);
                    intent = new Intent(this,MainActivity.class);

                }
                intent.putExtra(DataManager.COUNTDOWNITEM,temp);
                setResult(RESULT_OK,intent);
                finish();
            }
        }else {
            Toast.makeText(this, "标题栏不能为空", Toast.LENGTH_SHORT).show();
        }
    }


}
/*
*
* Calendar now = Calendar.getInstance();
                times[0] = now.get(Calendar.YEAR);
                times[1] = now.get(Calendar.MONTH)+1;
                times[2] = now.get(Calendar.DAY_OF_MONTH);
                times[3] = now.get(Calendar.HOUR_OF_DAY);
                times[4] = now.get(Calendar.MINUTE);
                times[5] = now.get(Calendar.SECOND);*/