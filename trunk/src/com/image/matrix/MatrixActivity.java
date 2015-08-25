
package com.image.matrix;

import com.view.main.ActivityBase;
import com.view.wheelview.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MatrixActivity extends ActivityBase {
    MatrixImage mMatrixImage;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrix);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.navigation_header_news);

        mMatrixImage = (MatrixImage) findViewById(R.id.img);

        mMatrixImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMatrixImage.setImageMatrix(mMatrixImage.getBitmapMatrix(mBitmap, 100, 1.5f, 1.5f));
            }
        });

        mMatrixImage.setImageBitmap(mBitmap);
        mMatrixImage.postDelayed(new Runnable() {

            @Override
            public void run() {
                //mMatrixImage.setImageMatrix(mMatrixImage.getBitmapMatrix(mBitmap));
            }
        }, 10000);
    }
}
