
package com.image.matrix;

import com.view.wheelview.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyImage1 extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;
    private float[] array = new float[9];

    public MyImage1(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.navigation_header_news);
        invalidate();
    }

    public void setValues(float[] a) {
        for (int i = 0; i < 9; i++) {
            array[i] = a[i];
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = mPaint;
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        //new 一个坐标变换矩阵  
        Matrix cm = new Matrix();
        //为坐标变换矩阵设置响应的值  
        cm.setValues(array);
        //按照坐标变换矩阵的描述绘图  
        canvas.drawBitmap(mBitmap, cm, paint);
        Log.i("CMatrix", "--------->onDraw");

    }
}
