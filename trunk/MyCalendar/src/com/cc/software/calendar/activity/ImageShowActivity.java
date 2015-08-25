package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.FileUtil;
import com.cc.software.calendar.util.LoadImageAysnc;
import com.cc.software.calendar.util.LoadImageAysnc.ImageCallBack;
import com.cc.software.calendar.view.ImageViewEx;
import com.cc.software.calendar.view.ImageViewEx.SingleClickListener;

public class ImageShowActivity extends Activity implements SingleClickListener, OnClickListener {

    private ImageViewEx ive;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_show);
        ive = (ImageViewEx) findViewById(R.id.image);
        if (CommonUtil.isNetAvailable(this)) {
            loading(getIntent().getStringExtra("url"));
        } else {
            finish();
        }
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_download).setOnClickListener(this);
        ive.setListener(this);
    }

    private void loading(String url) {
        mBitmap = LoadImageAysnc.getInstance().loadImage(url, new ImageCallBack() {
            @Override
            public void imageLoaded(Bitmap bitmap, String iconSrc) {
                if (bitmap != null) {
                    mBitmap = bitmap;
                    findViewById(R.id.progress).setVisibility(View.GONE);
                    ive.setImageBitmap(bitmap);
                    findViewById(R.id.bottom_bar).setVisibility(View.VISIBLE);
                } else {
                    CommonUtil.showToastMessage(R.string.loading_fail, ImageShowActivity.this);
                    finish();
                }
            }
        });
        if (mBitmap != null) {
            findViewById(R.id.bottom_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.progress).setVisibility(View.GONE);
            ive.setImageBitmap(mBitmap);
        }
    }

    @Override
    public void onSinleClick() {
        System.out.println("onSinleClick");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_back:
            finish();
            break;
        case R.id.btn_share:
            CommonUtil.shareImage(this, getIntent().getStringExtra("url"));
            break;
        case R.id.btn_download:
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    boolean b = true;
                    ByteArrayOutputStream out = null;
                    FileOutputStream os = null;
                    try {
                        out = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();

                        os = new FileOutputStream(new File(FileUtil.getFilePathByType(ImageShowActivity.this,
                                        CommonUtil.MD5Encode(getIntent().getStringExtra("url")) + ".jpg",
                                        Constants.TAKE_PHOTO)));
                        os.write(out.toByteArray());
                        os.flush();
                        os.close();
                        out.close();
                    } catch (Exception e) {
                        b = false;
                        e.printStackTrace();
                    }
                    CommonUtil.log_d(Constants.LOG_TAG, FileUtil.getFilePathByType(ImageShowActivity.this,
                                    CommonUtil.MD5Encode(getIntent().getStringExtra("url")) + ".jpg",
                                    Constants.TAKE_PHOTO));
                    if (b) {
                        return getString(R.string.save_photo_success)
                                        + FileUtil.getFilePathByType(ImageShowActivity.this, "media"
                                                        + getIntent().getStringExtra("url") + ".jepg",
                                                        Constants.TAKE_PHOTO);
                    } else {
                        return getString(R.string.save_photo_failed);
                    }
                }

                @Override
                protected void onPostExecute(String result) {
                    CommonUtil.showToastMessage(result, ImageShowActivity.this);
                }
            }.execute();
            break;
        default:
            break;
        }
    }
}
