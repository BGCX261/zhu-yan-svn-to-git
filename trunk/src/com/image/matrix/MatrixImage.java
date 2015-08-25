
package com.image.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class MatrixImage extends ImageView {

    public MatrixImage(Context context) {
        super(context);
    }

    public MatrixImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Matrix getMatrix() {
        return super.getMatrix();
    }

    public Matrix getBitmapMatrix(Bitmap bitmap, int rotate, float rateX, float rateY) {
        Log.d("w.w", "bitmap width=" + bitmap.getWidth());
        Log.d("w.w", "bitmap height=" + bitmap.getHeight());
        Matrix matrix = new Matrix();
        int cx = bitmap.getWidth() / 2;
        int cy = bitmap.getHeight() / 2;
        matrix.preTranslate(-cx, -cy);
        matrix.postRotate(rotate);
        matrix.postScale(rateX, rateY);
        matrix.postTranslate(getWidth() / 2, getHeight() / 2);
        Log.d("w.w", "view width=" + getWidth());
        Log.d("w.w", "view height=" + getHeight());
        return matrix;
    }
}
