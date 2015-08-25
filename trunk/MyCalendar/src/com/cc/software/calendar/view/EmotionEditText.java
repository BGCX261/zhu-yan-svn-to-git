package com.cc.software.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.EditText;

public class EmotionEditText extends EditText {
    
    private Context mContext;

	public EmotionEditText(Context context) {
		super(context);		
	}
	public EmotionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }
	@Override
	protected void onDraw(Canvas canvas) {
	    WindowManager wm = (WindowManager) mContext.getSystemService("window");
        int windowWidth = wm.getDefaultDisplay().getWidth();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int scrollY = getScrollY();
        int scrollX = getScrollX() + windowWidth;
        int innerHeight = scrollY + getHeight() - paddingBottom;
        int lineHeight = getLineHeight();
        int baseLine = scrollY
                + (lineHeight - ((scrollY - paddingTop) % lineHeight));

        int x = 8;
        while (baseLine < innerHeight) {
            canvas.drawLine(x, baseLine, scrollX - x, baseLine, paint);
            baseLine += lineHeight;
        }

        super.onDraw(canvas);
	}
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	public void insertIcon(int id){
		SpannableString ss = new SpannableString(getText().toString()+"[smile]");  
        Drawable d = getResources().getDrawable(id);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());  
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);  
        ss.setSpan(span, getText().length(),getText().length()+"[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);  
        setText(ss);
	}
}
