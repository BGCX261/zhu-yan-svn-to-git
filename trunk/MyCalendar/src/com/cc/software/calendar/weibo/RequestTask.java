package com.cc.software.calendar.weibo;

import hut.cc.software.calendar.R;

import java.util.List;

import weibo4android.User;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.Toast;

import com.cc.software.calendar.helper.PreferenceHelper;
import com.cc.software.calendar.messaging.MessagingService;
import com.cc.software.calendar.messaging.ServiceConstants;
import com.cc.software.calendar.util.CommonUtil;

public class RequestTask extends AsyncTask<String, Integer, Integer> {
    private Context mContext = null;
    private ProgressDialog pd = null;
    private List<weibo4android.Status> mStatus;

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
            if (result == SinaWeibo.SUCCEED) {
                mStatus = SinaWeibo.getInstance().getFriendsTimeline(mContext, 1, 20);
                PreferenceHelper.savePreferenceBooleanValue(mContext, "auto_login", true);
                PreferenceHelper.savePreferenceStringValue(mContext, "username", userName);
                PreferenceHelper.savePreferenceStringValue(mContext, "password", passWord);
            } else {
                MessagingService.getInstance().publish(ServiceConstants.MESSAGE_ID_WEI_LOGIN_FAILED);
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
        if (pd != null) {
            try {
                pd.dismiss();
            } catch (Exception e) {

            }
        }
        switch (result) {
        case SinaWeibo.SUCCEED:
            saveLoginPref();
            showFriendStatus();
            break;
        case SinaWeibo.FAILED:
            CommonUtil.showToastMessage(R.string.net_cannot_work, mContext);
            break;
        case SinaWeibo.RETRY_402:
            CommonUtil.showToastMessage(R.string.no_avaiable_weibo_please_ensure_avaiable, mContext);
            break;
        case SinaWeibo.RETRY_403:
            CommonUtil.showToastMessage(R.string.accout_or_password_error, mContext);
            break;
        }
    }

    private void showFriendStatus() {
        User user = SinaWeibo.getInstance().getUser();
        PreferenceHelper.savePreferenceStringValue(mContext, "nickname", user.getName());
        Toast.makeText(mContext, user.getName() + mContext.getString(R.string.login_success), Toast.LENGTH_LONG).show();
        MessagingService.getInstance().publish(
                        Message.obtain(null, ServiceConstants.MESSAGE_ID_WEIBO_LOGIN_SUCCEED, mStatus));
        //Intent intent = new Intent(WeiboLoginActivity.this, WeiboInfoActivity.class);
        //intent.putExtra("username", user.getName());
        //intent.putExtra("URl", value)
        //startActivity(intent);
        /*MessageListView mBlogUserInfoView = new MessageListView(mContext);

        if (mBlogUserInfoView.init(user)) {
            ViewManager.getInstance().Push(this, mBlogUserInfoView);
        } else {
            if (mBlogUserInfoView != null) {
                mBlogUserInfoView.release();
                mBlogUserInfoView = null;
            }
        }*/
    }

    private void saveLoginPref() {
        /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WeiboLoginActivity.this);
        sp.edit().putLong(SinaWeiboAuthConstant.WEIBO_USERID, SinaWeibo.userid);
        sp.edit().putString(SinaWeiboAuthConstant.WEIBO_TOKEN, SinaWeibo.token);
        sp.edit().putString(SinaWeiboAuthConstant.WEIBO_TOKEN_SECRET, SinaWeibo.token_secret);
        sp.edit().commit();*/
    }
}
