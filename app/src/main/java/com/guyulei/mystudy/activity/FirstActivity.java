package com.guyulei.mystudy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.guyulei.mystudy.R;
import com.guyulei.mystudy.base.BaseActivity;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public static void startActivity(Context context, String data1, String data2) {
        Intent intent = new Intent(context, FirstActivity.class);
        intent.putExtra("data1", data1);
        intent.putExtra("data2", data2);
        context.startActivity(intent);
    }
}
