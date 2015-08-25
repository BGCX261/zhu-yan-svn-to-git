
package com.view.crop;

import com.view.main.ActivityBase;
import com.view.wheelview.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;

public class CropActivity extends ActivityBase implements OnClickListener {
    CropImageView mImageView;
    private Bitmap mBitmap;
    Matrix mImageMatrix;
    private CheckedTextView mSameScale, mImgScale, mHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_img);

        mSameScale = (CheckedTextView) findViewById(R.id.same_scale);
        mImgScale = (CheckedTextView) findViewById(R.id.img_scale);
        mHide = (CheckedTextView) findViewById(R.id.hide);

        mSameScale.setOnClickListener(this);
        mImgScale.setOnClickListener(this);
        mHide.setOnClickListener(this);

        mImageView = (CropImageView) findViewById(R.id.crop_img);

        mImageView.post(new Runnable() {

            @Override
            public void run() {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
                mBitmap = changeBitmapToSuitable(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(),
                        500, 500);
                mImageView.setImageRotateBitmapResetBase(new RotateBitmap(mBitmap), mSameScale.isChecked());
                mImageMatrix = mImageView.getImageMatrix();
                makeDefault();
            }
        });
    }

    private void makeDefault() {
        HighLightView hv = new HighLightView(mImageView);

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Rect imageRect = new Rect(0, 0, width, height);

        int cropWidth = Math.min(width, height) * 4 / 5;
        int cropHeight = cropWidth;

        int x = (width - cropWidth) / 2;
        int y = (height - cropHeight) / 2;

        RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
        Log.d("w.w", "width=" + width + "   height=" + height);
        Log.d("w.w", x + "," + y + "," + (x + cropWidth) + "," + (y + cropHeight));
        hv.setUp(mImageMatrix, imageRect, cropRect, true);
        mImageView.setHightLightView(hv);
        mImageView.invalidate();
    }

    public static synchronized Bitmap changeBitmapToSuitable(Bitmap bitmap, int sourceWidht,
            int sourceHeight, int targetWidht, int targetHeight) {
        Matrix matrix = new Matrix();
        matrix.postScale(targetWidht / (float) sourceWidht, targetHeight
                / (float) sourceHeight);
        Bitmap tempBitmap = null;
        try {
            tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, sourceWidht, sourceHeight,
                    matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return tempBitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.same_scale:
                mSameScale.setChecked(!mSameScale.isChecked());
                mImageView.setMaintainAspectRatio(mSameScale.isChecked());
                break;
            case R.id.img_scale:
                mImgScale.setChecked(!mImgScale.isChecked());
                mImageView.setShouldScale(mImgScale.isChecked());
                break;
            case R.id.hide:
                mHide.setChecked(!mHide.isChecked());
                mImageView.setHidden(mHide.isChecked());
                break;

            default:
                break;
        }
    }
}
