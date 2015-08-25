package com.cc.software.calendar.weibo;

import hut.cc.software.calendar.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cc.software.calendar.activity.WeiboInfoActivity;
import com.cc.software.calendar.util.CommonUtil;

public class LoginView extends LinearLayout implements OnClickListener {
    private String TAG = "MicroBlogLogin";
    private String mUsername = "";
    private String mPwd = "";
    private Context mContext = null;
    private CheckBox mCheckBox;

    public LoginView(Context context) {
        this(context, null);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.weibo_login_layout, this);
        init();
    }

    private void init() {
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.login_cancel).setOnClickListener((WeiboInfoActivity) mContext);
        mCheckBox = (CheckBox) findViewById(R.id.auto_login);
        findViewById(R.id.auto_line).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login:
            EditText et = (EditText) findViewById(R.id.username);
            mUsername = et.getText().toString();
            et = (EditText) findViewById(R.id.pwd);
            mPwd = et.getText().toString();

            if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPwd)) {
                CommonUtil.showToastMessage(R.string.accout_and_password_cannot_null, mContext);
                return;
            }

            if (CommonUtil.isNetAvailable(getContext())) {
                RequestTask requestTask = new RequestTask(mContext);
                requestTask.execute(mUsername, mPwd);
            }
            Log.d(TAG, "==================Try end!");
            break;
        case R.id.auto_line:
            if (mCheckBox.isChecked()) {
                mCheckBox.setChecked(false);
            } else {
                mCheckBox.setChecked(true);
            }
            break;

        }
    }

    /*class RequestTask extends AsyncTask<String, Integer, Integer> {
        private Context mContext = null;

        public RequestTask(Context c) {
            mContext = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setTitle("正在登陆新浪微博");
            pd.setMessage("请稍候..");
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String userName = params[0];
                String passWord = params[1];

                int result = SinaWeibo.getInstance().login(userName, passWord);
                if (result == SinaWeibo.SUCCEED && mCheckBox.isChecked()) {
                    PreferenceHelper.savePreferenceBooleanValue(mContext, "auto_login", true);
                    PreferenceHelper.savePreferenceStringValue(mContext, "username", userName);
                    PreferenceHelper.savePreferenceStringValue(mContext, "password", passWord);
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return SinaWeibo.FAILED;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(Integer result) {
            pd.dismiss();
            switch (result) {
            case SinaWeibo.SUCCEED:
                Log.d(TAG, "----------------------------------------------- 1   RESULT_OK!");
                saveLoginPref();
                showFriendStatus();
                break;
            case SinaWeibo.FAILED:
                Log.d(TAG, "----------------------------------------------- 2   RESULT_CANCELED!");
                Toast.makeText(mContext, "网络异常,请确认网络是否连接正常.", Toast.LENGTH_LONG).show();
                break;
            case SinaWeibo.RETRY_402:
                Toast.makeText(mContext, "没有开通微博,请开通后进行授权.", Toast.LENGTH_LONG).show();
                break;
            case SinaWeibo.RETRY_403:
                Toast.makeText(mContext, "账号或密码错误,请重新输入.", Toast.LENGTH_LONG).show();
                break;
            }
        }

        private void showFriendStatus() {
            User user = SinaWeibo.getInstance().getUser();
            Toast.makeText(mContext, "[" + user.getName() + "] 登录成功！", Toast.LENGTH_LONG).show();
            //MessageListView mBlogUserInfoView = new MessageListView(mContext);

            if (mBlogUserInfoView.init(user)) {
                ViewManager.getInstance().Push(LoginView.this, mBlogUserInfoView);
            } else {
                if (mBlogUserInfoView != null) {
                    mBlogUserInfoView.release();
                    mBlogUserInfoView = null;
                }
            }
        }

        private void saveLoginPref() {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginView.this.mContext);
            sp.edit().putLong(SinaWeiboAuthConstant.WEIBO_USERID, SinaWeibo.userid);
            sp.edit().putString(SinaWeiboAuthConstant.WEIBO_TOKEN, SinaWeibo.token);
            sp.edit().putString(SinaWeiboAuthConstant.WEIBO_TOKEN_SECRET, SinaWeibo.token_secret);
            sp.edit().commit();
        }
    }*/
}
