package com.cc.software.calendar.view;

import hut.cc.software.calendar.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ContactPhotoView extends ImageView {

    public final int defaultPhotoId;
    public long currentPhotoId = -1;
    private boolean isShowingDefault;

    /**
     * @param context
     * @param attrs
     */
    public ContactPhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContactPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);       
        defaultPhotoId = R.drawable.icon;
        currentPhotoId = defaultPhotoId;
        isShowingDefault = true;
    }

    public void showDefault() {
        if (isShowingDefault == false) {
            isShowingDefault = true;
            setImageResource(defaultPhotoId);
            currentPhotoId = -1;
        }
    }

    public void setImageBitmap(Bitmap bitMap, long id) {
        if (currentPhotoId != id) {
            isShowingDefault = false;
            currentPhotoId = id;
            setImageBitmap(bitMap);
        }
    }

    public int getDefaultPhotoId() {
        return defaultPhotoId;
    }

    public long getCurrentPhotoId() {
        return currentPhotoId;
    }

    public boolean isShowingDefaultPhoto() {
        return isShowingDefault;
    }

}
