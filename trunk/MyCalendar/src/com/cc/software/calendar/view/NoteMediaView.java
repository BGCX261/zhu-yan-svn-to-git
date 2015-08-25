package com.cc.software.calendar.view;

import hut.cc.software.calendar.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class NoteMediaView extends FrameLayout {

    public NoteMediaView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.note_media_view, this);
    }

    public NoteMediaView(Context context, AttributeSet attrs) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.note_media_view, this);
    }

    public NoteMediaView(Context context, Bitmap bitmap) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.note_media_view, this);

    }

    public void popUpNoteMediaView(Bitmap bitmap) {
        if (bitmap != null)
            ((ImageView) findViewById(R.id.photo)).setImageBitmap(bitmap);
        setVisibility(View.VISIBLE);
    }

    public void dismissNoteMediaView() {
        setVisibility(View.GONE);
    }

}
