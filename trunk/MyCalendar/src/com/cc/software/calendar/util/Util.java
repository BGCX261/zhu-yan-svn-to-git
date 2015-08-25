package com.cc.software.calendar.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

public class Util {
	private final String Tag = Util.class.getSimpleName();
	private Util mUtil = null;

	private Util() {
	}

	public Util getInstance() {
		if (mUtil == null)
			mUtil = new Util();
		return mUtil;
	}

	private Bitmap getIamge(String aurl) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			URL url = new URL(aurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * <p>asynchronous get remote resource with URL</p>
	 * @param url the resource URL
	 * @param handler receive get resource success message
	 * @param index <optional> Tag this special with index
	 */
	public void AsynchGetImage(final String url, final Handler handler,final int index) {
		new AsyncTask<Object, Void, Bitmap>() {
			Handler handler = null;

			@Override
			protected Bitmap doInBackground(Object... params) {
				handler = (Handler) params[1];
				return getIamge((String) params[0]);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (handler == null){
					Log.e(Tag, "******* handler is null!");
					return;
				}
				
				Message msg = handler.obtainMessage();
				Pair<Boolean, Bitmap> pair = new Pair<Boolean, Bitmap>(
						(result == null) ? false : true, result);
				msg.obj = new Pair<Pair<Boolean, Bitmap>, Integer>(pair, index);
				handler.sendMessage(msg);
			}
		}.execute(url, handler, index);
	}
}
