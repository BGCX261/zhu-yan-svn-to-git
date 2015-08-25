
package com.surface;

import com.view.wheelview.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Canvas canvas = null;
    // 背景图
    private Bitmap BackgroundImage;
    // 问号图
    private Bitmap QuestionImage;
    private boolean isDraw = true;

    SurfaceHolder Holder;

    public MySurfaceView(Context context) {
        super(context);
        BackgroundImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.rd_ninegrid);
        QuestionImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.navigation_header_news);

        Holder = this.getHolder();// 获取holder
        Holder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub
        System.out.println("TestSurfaceView3.MySurfaceView.surfaceChanged()");

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 启动自定义线程
        System.out.println("TestSurfaceView3.MySurfaceView.surfaceCreated()");
        new Thread(new MyThread()).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("TestSurfaceView3.MySurfaceView.surfaceDestroyed()");
        try {
            Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("TestSurfaceView3.MySurfaceView.onMeasure()");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("TestSurfaceView3.MySurfaceView.onDraw()");
    }

    // 自定义线程类
    class MyThread implements Runnable {
        @Override
        public void run() {
            int rotate = 0;// 旋转角度变量
            
            while (isDraw) {
                try {
                    canvas = Holder.lockCanvas();// 获取画布
                    Paint mPaint = new Paint();
                    mPaint.setColor(Color.GREEN);
                    canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                    // 绘制背景
                    canvas.drawBitmap(BackgroundImage, 0, 0, mPaint);
                    
                    canvas.drawText("abcdefghi", 200, 100, mPaint);
                    // 创建矩阵以控制图片旋转和平移
                    Matrix m = new Matrix();
                    // 设置旋转角度
                    m.postRotate((rotate += 48) % 360,
                            QuestionImage.getWidth() / 2,
                            QuestionImage.getHeight() / 2);
                    // 设置左边距和上边距
                    m.postTranslate(10, 10);
                    // 绘制问号图
                    canvas.drawBitmap(QuestionImage, m, mPaint);
                    // 休眠以控制最大帧频为每秒约30帧
                    Thread.sleep(33);
                } catch (Exception e) {
                } finally {
                    try {
                        Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
                    } catch (Exception e2) {
                        // TODO: handle exception
                    }
                }
                isDraw = false;
            }
        }
    }

}
