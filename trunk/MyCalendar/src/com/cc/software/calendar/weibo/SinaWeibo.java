package com.cc.software.calendar.weibo;

import hut.cc.software.calendar.R;

import java.util.ArrayList;
import java.util.List;

import weibo4android.Comment;
import weibo4android.Count;
import weibo4android.Emotion;
import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cc.software.calendar.util.CommonUtil;

public class SinaWeibo {

    public static final String TAG = "SinaWeibo";
    final int TIMEOUT = 30 * 1000;

    private static SinaWeibo mInstance;
    public static final String WEIBO_USERID = "userid";
    public static final String WEIBO_TOKEN = "access_token";
    public static final String WEIBO_TOKEN_SECRET = "access_token_secret";

    public static final int SUCCEED = 0;
    public static final int FAILED = 1;
    public static final int RETRY_403 = 2;
    public static final int RETRY_402 = 3;
    public static long userid;
    public static String token;
    public static String token_secret;

    public static final int PAGE_COUNT = 20;

    public static Weibo mWeibo;
    private User mUser;

    public static SinaWeibo getInstance() {
        if (null == mInstance) {
            mInstance = new SinaWeibo();
        }
        return mInstance;
    }

    public void init(Context ctx) {
        mWeibo = null;
        mUser = null;
    }


    public int login(String username, String password) {
        int result = doXAuth(username, password);
        if (SUCCEED != result) {
            return result;
        }
        try {
            Log.e("ozl", "success!======================================================");
            mUser = mWeibo.verifyCredentials();
            return SUCCEED;
        } catch (WeiboException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mWeibo = null;
            mUser = null;
            try {
                WeiboException weiboEx = (WeiboException) e;
                if (weiboEx.getStatusCode() == 403) {
                    return RETRY_403;
                } else if (402 == weiboEx.getStatusCode()) {
                    return RETRY_402;
                } else {
                    return FAILED;
                }
            } catch (Exception ex) {
                Log.e("ozl", "fail!======================================================");
                return FAILED;
            }
        }
    }

    private int doXAuth(String username, String password) {
        //Weibo weibo = new Weibo(username, password);
        Weibo weibo = new Weibo();
        try {
            //weibo.setHttpConnectionTimeout(5000);
            AccessToken accessToken = weibo.getXAuthAccessToken(username, password, "client_auth");
            userid = accessToken.getUserId();

            token = accessToken.getToken();
            token_secret = accessToken.getTokenSecret();
            weibo.setToken(accessToken);
            mWeibo = weibo;
            Log.e("ozl ================================1", mWeibo.toString());
            return SUCCEED;
        } catch (WeiboException e) {
            e.printStackTrace();
            try {
                WeiboException weiboEx = (WeiboException) e;
                if (weiboEx.getStatusCode() == 403) {
                    return RETRY_403;
                } else if (402 == weiboEx.getStatusCode()) {
                    return RETRY_402;
                } else {
                    return FAILED;
                }
            } catch (Exception ex) {
                return FAILED;
            }
        }
    }

    public List<Status> getFriendsTimeline(Context context, int page, int count) {
        if (!CommonUtil.isNetAvailable(context)) {
            return null;
        }
        Paging paging = new Paging(page, count);
        try {
            return mWeibo.getFriendsTimeline(paging);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
        return new ArrayList<Status>();
    }
    
    public List<Status> getFriendsTimeline(Context context, long maxId, int count) {
        if (!CommonUtil.isNetAvailable(context)) {
            return null;
        }
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        paging.setCount(count);
        try {
            return mWeibo.getFriendsTimeline(paging);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser() {
        return mUser;
    }

    private static final String CONSUMER_KEY = "664345808";
    private static final String CONSUMER_SECRET = "d7461b3e710556b3f6c660c0f2fb1f7c";
    static {
        System.setProperty("weibo4j.oauth.consumerKey", CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", CONSUMER_SECRET);
    }

    public List<Comment> getCommentsById(Context context, String id, int page, int count) {
        if (!CommonUtil.isNetAvailable(context)) {
            return null;
        }
        Paging paging = new Paging(page, count);
        try {
            return mWeibo.getComments(id, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Comment>();
    }

    public List<Count> getCount(Context context, String ids) {
        if (!CommonUtil.isNetAvailable(context)) {
            return null;
        }
        try {
            return mWeibo.getCounts(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateComment(Context context, String comment, String id, String cid) {
        if (!CommonUtil.isNetAvailable(context)) {
            return false;
        }
        try {
            Comment comment2 = mWeibo.updateComment(comment, id, cid);
            if (comment2 != null) {
                return true;
            }
        } catch (Exception e) {
            CommonUtil.showToastMessage(R.string.weibo_comment_failed, context);
            e.printStackTrace();
        }
        return false;
    }

    public boolean rePost(Context context, String sid, String status, int isComment) {
        if (!CommonUtil.isNetAvailable(context)) {
            return false;
        }
        try {
            Log.d("calendar", sid + "------------------");
            Status status2 = mWeibo.repost(sid, status, isComment);
            if (status2 != null) {
                return true;
            }
        } catch (Exception e) {
            CommonUtil.showToastMessage(R.string.weibo_repost_failed, context);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(Context context, String status) {
        if (!CommonUtil.isNetAvailable(context)) {
            return false;
        }
        try {
            Status cStatus = mWeibo.updateStatus(status);
            return (null != cStatus);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Emotion> getEmotions() {
        try {
            return mWeibo.getEmotions();
        } catch (WeiboException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createFavorite(Context context, long id) {
        if (!CommonUtil.isNetAvailable(context)) {
            return false;
        }
        try {
            Status status = mWeibo.createFavorite(id);
            if (status != null) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }
}
