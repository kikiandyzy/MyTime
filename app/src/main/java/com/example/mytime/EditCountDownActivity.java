package com.example.mytime;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mytime.DataStructure.CountDownItem;
import com.example.mytime.DataStructure.DataManager;
import com.example.mytime.UserDefined.MyDialog;
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
    private TextView showLabel;



    //日期和时间选择器
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int[] times = new int[]{1,2,3,4,5,6};
    private boolean ifTime = false;
    private boolean ifNew = false;
    private CountDownItem countDownItem;

    //照片
    private Bitmap bitmap = null;


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
        showLabel = findViewById(R.id.textview_show_label);


        initOptions();

        //接下来查看是哪个活动来的
        Intent intent = getIntent();
        countDownItem = (CountDownItem) intent.getSerializableExtra("countDownItem");
        if (countDownItem != null){
            title.setText(countDownItem.getTitle());
            describe.setText(countDownItem.getDescribe());
            showDate.setText(countDownItem.getTargetDateParticular());
            showLabel.setText(countDownItem.getLabel());
        }else {
            countDownItem = new CountDownItem();
            ifNew = true;
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
                    e.printStackTrace();
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
        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAddLabelDialog();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先动态申请危险权限
                //检查是否有读写sd卡的权限
                if(ContextCompat.checkSelfPermission(EditCountDownActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    //没有就申请
                    ActivityCompat.requestPermissions(EditCountDownActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
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
                CountDownItem temp = new CountDownItem(times,title.getText().toString(),describe.getText().toString(),bitmap);
                temp.setLabel(showLabel.getText().toString());
                Intent intent;
                if(ifNew){//mainActivity
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
                if(ifNew == false){//用原来的日期
                    temp = new CountDownItem(countDownItem.getTimes(),title.getText().toString(),describe.getText().toString(),bitmap);
                    temp.setLabel(showLabel.getText().toString());
                    intent = new Intent(this,ShowCountDownActivity.class);
                }else {//如果是新建，就用现在的时间
                    Calendar now = Calendar.getInstance();
                    times[0] = now.get(Calendar.YEAR);
                    times[1] = now.get(Calendar.MONTH)+1;
                    times[2] = now.get(Calendar.DAY_OF_MONTH);
                    times[3] = now.get(Calendar.HOUR_OF_DAY);
                    times[4] = now.get(Calendar.MINUTE);
                    times[5] = now.get(Calendar.SECOND);
                    temp = new CountDownItem(times,title.getText().toString(),describe.getText().toString(),bitmap);
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

    //弹出一个确认添加标签的对话框
    private void buildAddLabelDialog(){
        View view1 = getLayoutInflater().inflate(R.layout.dialog_set_label, null);
        LinearLayout linearLayout = findViewById(R.id.linearlayout_edit_acivity);
        int width = linearLayout.getWidth();//获取最外层布局的宽度
        //宽度和高度即设置的对话框的大小
        final MyDialog myDialog = new MyDialog(EditCountDownActivity.this, width-150, 800, view1);
        myDialog.setCancelable(true);
        myDialog.show();
        ListView listView = myDialog.getWindow().findViewById(R.id.listview_set_label_dialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditCountDownActivity.this,android.R.layout.simple_list_item_1,dataManager.labelName);
        listView.setAdapter(adapter);
        final String[] label = {""};
        TextView chooseLabel = myDialog.getWindow().findViewById(R.id.choose_label_dialog_set_label);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                label[0] = dataManager.labelName.get(i);

                chooseLabel.setText("选择标签："+label[0]);
            }
        });

        myDialog.getWindow().findViewById(R.id.set_label_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLabel.setText("");
                countDownItem.setLabel("");
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().findViewById(R.id.set_label_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().findViewById(R.id.set_label_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLabel.setText(label[0]);
                myDialog.dismiss();
            }
        });






    }

    //接下来的函数是从文件中获取图片
    //打开相册
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,DataManager.REQUEST_CHOOSE_PHOTO);

    }

    //无论动态权限申请成不成功，都会调用该方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                else {
                    Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    @TargetApi(19)//4.4以上版本就是不再返回图片真实的URI了，所以要进行解析
    private void  handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath =getImagePath(contentUri,null);
            }else if("content".equals(uri.getScheme())){
                imagePath = getImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath = uri.getPath();
            }
        }
        displayImage(imagePath);//根据路径显示图片
    }



    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            bitmap = BitmapFactory.decodeFile(imagePath);
            //picture.setImageBitmap(bitmap);

        }else {
            Toast.makeText(this, "failed get image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case DataManager.REQUEST_CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
                default:
                    break;
        }
    }
}
