package com.cc.software.calendar.weibo;

import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SinaWeiboAuthConstant {
	public final static String TAG = "SinaWeiboAuthConstant";
	public static final String CONSUMER_KEY = "664345808";
	public static final String CONSUMER_SECRET = "d7461b3e710556b3f6c660c0f2fb1f7c";
	public static final String SINA_ACCESS_TOKEN = "sina_access_token";

	public static final String WEIBO_USERID = "userid";
	public static final String WEIBO_TOKEN = "access_token";
	public static final String WEIBO_TOKEN_SECRET = "access_token_secret";

	private static SinaWeiboAuthConstant instance = null;
	private static Weibo weibo;
	public static User mLoginUser;

	private Context mContext;
	private RequestToken requestToken;
	private AccessToken accessToken;
	private String token;
	private String tokenSecret;

	private SinaWeiboAuthConstant() {
	};

	public static synchronized SinaWeiboAuthConstant getInstance() {
		if (instance == null)
			instance = new SinaWeiboAuthConstant();
		return instance;
	}

	public void init(Context context) {
		mContext = context;

		SharedPreferences sp = mContext.getApplicationContext()
				.getSharedPreferences(SINA_ACCESS_TOKEN, Context.MODE_PRIVATE);
		this.token = sp.getString(WEIBO_TOKEN, null);
		this.tokenSecret = sp.getString(WEIBO_TOKEN_SECRET, null);
		Log.d(TAG, "init, get AccessToken:" + sp.getString(WEIBO_TOKEN, null));
		Log.d(TAG,
				"init, get AccessTokenSecret:"
						+ sp.getString(WEIBO_TOKEN_SECRET, null));
	}

	public Weibo getWeibo() {
		if (weibo == null)
			weibo = new Weibo();
		return weibo;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void saveAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
		this.token = accessToken.getToken();
		this.tokenSecret = accessToken.getTokenSecret();
	}

	public RequestToken getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
}