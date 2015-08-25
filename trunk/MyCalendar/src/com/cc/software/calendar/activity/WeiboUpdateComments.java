package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.view.EmotionEditText;
import com.cc.software.calendar.weibo.SinaWeibo;

public class WeiboUpdateComments extends Activity implements OnClickListener {

    private EmotionEditText et;
    private CheckBox mCheckBox;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_update);
        ((TextView) findViewById(R.id.name)).setText(R.string.weibo_comment);
        ((TextView) findViewById(R.id.memo)).setText(R.string.post_weibo_at_same_time);
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
        case R.id.back:
            finish();
            break;
        case R.id.bottom_bar:
            if (mCheckBox.isChecked()) {
                mCheckBox.setChecked(false);
            } else {
                mCheckBox.setChecked(true);
            }
            break;
        case R.id.update:
            String comments = et.getText().toString();
            if (TextUtils.isEmpty(comments)) {
                CommonUtil.showToastMessage(R.string.weibo_input_comments_content, this);
                return;
            }
            if (mCheckBox.isChecked()) {
                new UpdateComments(comments, id, 3).execute();

            } else {
                new UpdateComments(comments, id, -1).execute();
            }

            break;

        default:
            break;
        }
    }

    private class UpdateComments extends AsyncTask<Void, Void, Integer> {
        private String comments;
        private String id;
        private int isComment = -1;

        UpdateComments(String comments, String id, int isComment) {
            this.comments = comments;
            this.id = id;
            this.isComment = isComment;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            boolean b = false;
            if (isComment == -1) {
                b = SinaWeibo.getInstance().updateComment(WeiboUpdateComments.this, comments, id, null);
            } else {
                b = SinaWeibo.getInstance().rePost(WeiboUpdateComments.this, id, comments, 3);
            }
            if (b) {
                return R.string.weibo_comment_successed;
            }
            return R.string.weibo_comment_failed;
        }

        @Override
        protected void onPostExecute(Integer result) {
            CommonUtil.showToastMessage(result, WeiboUpdateComments.this);
            if (result == R.string.weibo_comment_successed)
                finish();
        }
    }
}
