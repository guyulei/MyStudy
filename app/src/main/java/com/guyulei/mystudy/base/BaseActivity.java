package com.guyulei.mystudy.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.guyulei.mystudy.utils.ActivityCollector;

/**
 * Created by A on 2017/8/23.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public Context mContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "onCreate: " + getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        mContent = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
