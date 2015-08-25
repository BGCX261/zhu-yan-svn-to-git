
package com.image.matrix;

import com.view.wheelview.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyImage extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;
    private float[] array = new float[20];
    ColorMatrix cm = new ColorMatrix();
    ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(cm);

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.navigation_header_news);
        invalidate();
    }

    public void setValues(float[] a) {
        for (int i = 0; i < 20; i++) {
            array[i] = a[i];
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = mPaint;

        paint.setColorFilter(null);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        //设置颜色矩阵  
        cm.set(array);
        //颜色滤镜，将颜色矩阵应用于图片  
        paint.setColorFilter(colorMatrixColorFilter);
        //绘图  
        canvas.drawBitmap(mBitmap, 0, 0, paint);
        Log.i("CMatrix", "--------->onDraw");

    }

}
