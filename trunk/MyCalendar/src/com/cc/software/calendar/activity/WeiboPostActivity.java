package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.view.EmotionEditText;
import com.cc.software.calendar.weibo.SinaWeibo;

public class WeiboPostActivity extends Activity implements OnClickListener {

    private EmotionEditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_edit_post);
        init();
    }

    private void init() {
        mEdit = (EmotionEditText) findViewById(R.id.weibo_content);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.post_new_weibo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.back:
            finish();
            break;
        case R.id.post_new_weibo:
            String weiboString = mEdit.getText().toString().trim();
            if (TextUtils.isEmpty(weiboString)) {
                CommonUtil.showToastMessage(R.string.weibo_input_content, this);
                return;
            }
            new PostWeibo(weiboString).execute();
            break;

        default:
            break;
        }
    }

    public class PostWeibo extends AsyncTask<Void, Void, Integer> {
        private String weibo;

        PostWeibo(String weiboContent) {
            this.weibo = weiboContent;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            boolean b = SinaWeibo.getInstance().updateStatus(WeiboPostActivity.this, weibo);
            if (b) {
                return R.string.weibo_publish_successed;
            }
            return R.string.weibo_publish_failed;
        }

        @Override
        protected void onPostExecute(Integer result) {
            CommonUtil.showToastMessage(result, WeiboPostActivity.this);
            if (result == R.string.weibo_repost_successed)
                finish();
        }
    }
}
