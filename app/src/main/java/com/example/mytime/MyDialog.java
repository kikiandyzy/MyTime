package com.example.mytime;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

//用于所有的自定义对话框
public class MyDialog extends Dialog {

    public MyDialog(@NonNull Context context) {
        super(context);
    }

    //    style引用style样式
    public MyDialog(Context context, int width, int height, View layout, int style) {

        super(context, style);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }

    //    style引用style样式
    public MyDialog(Context context, int width, int height, View layout) {

        super(context);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.width = width;
        params.height = height;

        //params.gravity = Gravity.TOP;
        params.gravity = Gravity.CENTER;

        window.setAttributes(params);


    }

}
