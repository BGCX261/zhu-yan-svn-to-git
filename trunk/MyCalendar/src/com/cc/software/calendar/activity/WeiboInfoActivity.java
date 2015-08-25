package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import java.util.List;

import weibo4android.Status;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.cc.software.calendar.helper.PreferenceHelper;
import com.cc.software.calendar.messaging.MessagingService;
import com.cc.software.calendar.messaging.ServiceConstants;
import com.cc.software.calendar.weibo.LoginView;
import com.cc.software.calendar.weibo.MessageDetailGallery;
import com.cc.software.calendar.weibo.MessageDetailView;
import com.cc.software.calendar.weibo.MessageListView;
import com.cc.software.calendar.weibo.RequestTask;

public class WeiboInfoActivity extends Activity implements OnClickListener, Callback {

    private MessageListView mWeiboList;
    private MessageDetailGallery mWeiboGallery;
    private LoginView mLoginView;
    private MessageDetailView mDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_container);
        init();

    }

    private void init() {
        MessagingService.getInstance().register(this, ServiceConstants.MESSAGE_ID_WEIBO_LOGIN_SUCCEED,
                        ServiceConstants.MESSAGE_ID_WEI_LOGIN_FAILED);

        mWeiboGallery = (MessageDetailGallery) findViewById(R.id.weibo_fling);
        mWeiboList = (MessageListView) findViewById(R.id.weibo_list);
        mDetailView = (MessageDetailView) findViewById(R.id.weibo_detail);

        mWeiboGallery.setVisibility(View.GONE);
        mLoginView = (LoginView) findViewById(R.id.weibo_login);

        int infoSize = MessageListView.mLatestInfo.size();
        if (infoSize > 0) {
            mLoginView.setVisibility(View.GONE);
            if (infoSize > 20) {
                int count =0;
                for (int i = 20; i < infoSize; i++) {
                    MessageListView.mLatestInfo.remove(i-count);
                    count++;
                }
            }
            mWeiboList.init(MessageListView.mLatestInfo);
        } else {
            if (PreferenceHelper.getPreferencePreferenceBooleanValue(this, "auto_login", false)) {
                RequestTask requestTask = new RequestTask(this);
                requestTask.execute(PreferenceHelper.getPreferenceStringValue(this, "username", ""),
                                PreferenceHelper.getPreferenceStringValue(this, "password", ""));
            } else {
                mLoginView.setVisibility(View.VISIBLE);
            }
        }

    }

    public void showList(boolean isShow) {
        if (isShow) {
            mWeiboList.setVisibility(View.VISIBLE);
            mWeiboGallery.setVisibility(View.GONE);
            mDetailView.setVisibility(View.GONE);
        } else {
            mWeiboList.setVisibility(View.GONE);
            mDetailView.setVisibility(View.VISIBLE);
            //mWeiboGallery.setVisibility(View.VISIBLE);
        }
    }

    public MessageDetailGallery getMessageDetailGallery() {
        return mWeiboGallery;
    }

    public MessageDetailView getMessageDetailView() {
        return mDetailView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            if (mDetailView.getVisibility() == View.VISIBLE) {
                showList(true);
                return true;
            }
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_cancel:
            finish();
            break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case ServiceConstants.MESSAGE_ID_WEIBO_LOGIN_SUCCEED:
            mLoginView.setVisibility(View.GONE);
            @SuppressWarnings("unchecked")
            List<Status> status = (List<Status>) msg.obj;
            mWeiboList.init(status);
            break;
        case ServiceConstants.MESSAGE_ID_WEI_LOGIN_FAILED:
            mWeiboList.setVisibility(View.GONE);
            mLoginView.setVisibility(View.VISIBLE);
        default:
            break;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
