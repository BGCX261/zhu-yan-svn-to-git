package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import com.cc.software.calendar.weibo.LoginView;
import com.cc.software.calendar.weibo.ViewManager;

public class SinaBlogActivity extends Activity implements OnClickListener {
    private final String TAG = "TestActivity";
    private LoginView mMicroBlogLoginView = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (savedInstanceState == null)
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        initViews();
        try {
            if (!ViewManager.getInstance().initialize(this, mMicroBlogLoginView))
                throw new Exception(
                                "com.qihoo360.microblog.TestActivity: MicroBlogViewManager initialize initChild is null...");
            setContentView(ViewManager.getInstance().getRootView());
        } catch (Exception e) {
            e.printStackTrace();
            ViewManager.getInstance().destoryRootView();
            onCreate(new Bundle());

        } finally {
            Log.i(TAG, "**** TestActivity onCreate ****");
        }
    }

    public void initViews() {
        mMicroBlogLoginView = new LoginView(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            /*if (ViewManager.getInstance().Pop())
                return true;*/
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_cancel:
            //ViewManager.getInstance().destoryRootView();
            finish();
            break;

        default:
            break;
        }
    }
}