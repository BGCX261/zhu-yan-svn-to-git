package com.cc.software.calendar.activity;

import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.view.EmotionEditText;
import com.cc.software.calendar.weibo.SinaWeibo;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class WeiboRepostActivity extends Activity implements OnClickListener {

    private EmotionEditText et;
    private CheckBox mCheckBox;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_update);
        ((TextView) findViewById(R.id.name)).setText(R.string.repost_weibo);
        ((TextView) findViewById(R.id.memo)).setText(R.string.publish_comments_at_same_time);
        et = (EmotionEditText) findViewById(R.id.weibo_content);
        et.setHint(R.string.say_something);
        mCheckBox = (CheckBox) findViewById(R.id.check);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.bottom_bar).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bottom_bar:
            if (mCheckBox.isChecked()) {
                mCheckBox.setChecked(false);
            } else {
                mCheckBox.setChecked(true);
            }
            break;
        case R.id.update:
            String content = et.getText().toString();
            if (TextUtils.isEmpty(content)) {
                CommonUtil.showToastMessage(R.string.weibo_input_content, this);
                return;
            }
            int isComment = 0;
            if (mCheckBox.isChecked()) {
                isComment = 3;
            }
            new RepostWeibo(content, id, isComment).execute();
            break;

        default:
            break;
        }
    }

    public class RepostWeibo extends AsyncTask<Void, Void, Integer> {
        private String weibo;
        private String id;
        private int isComment = -1;

        RepostWeibo(String weiboContent, String id, int isComment) {
            this.weibo = weiboContent;
            this.id = id;
            this.isComment = isComment;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            boolean b = SinaWeibo.getInstance().rePost(WeiboRepostActivity.this, id, weibo, isComment);
            if (b) {
                return R.string.weibo_repost_successed;
            }
            return R.string.weibo_repost_failed;
        }

        @Override
        protected void onPostExecute(Integer result) {
            CommonUtil.showToastMessage(result, WeiboRepostActivity.this);
            if (result == R.string.weibo_repost_successed)
                finish();
        }
    }

}
