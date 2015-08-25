package com.cc.software.calendar.view;

import hut.cc.software.calendar.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cc.software.calendar.activity.MainActivity;
import com.cc.software.calendar.activity.NotePadActivity;
import com.cc.software.calendar.activity.RecordActivity;
import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.comp.NoteAdapter;
import com.cc.software.calendar.util.DateUtil;
import com.cc.software.calendar.util.NoteManager;

public class NoteView extends LinearLayout implements OnClickListener {

    private static final String INIT_NOTE_DB = "init_note_db";
    private Context mActivity;
    private ListView mListView;

    private NoteAdapter mAdapter;

    public NoteView(Context context) {
        super(context);
        mActivity = context;
        initComponent();
    }

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = context;
        initComponent();
    }

    private void initComponent() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(mActivity).inflate(R.layout.note_view, this);
        mListView = (ListView) findViewById(R.id.list);

        findViewById(R.id.btn_note).setOnClickListener(this);
        findViewById(R.id.btn_record).setOnClickListener(this);
        findViewById(R.id.btn_take_photo).setOnClickListener(this);
        findViewById(R.id.btn_vedio).setOnClickListener(this);

        if (isFirstUse()) {
            NoteManager.initNoteDb(mActivity);
            mActivity.getSharedPreferences(INIT_NOTE_DB, 0).edit().putBoolean(INIT_NOTE_DB, false).commit();
        }
        mAdapter = new NoteAdapter(mActivity);
        mAdapter.bindList(mListView);
    }

    private boolean isFirstUse() {
        SharedPreferences preferences = mActivity.getSharedPreferences(INIT_NOTE_DB, 0);
        return preferences.getBoolean(INIT_NOTE_DB, true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.btn_note:
            intent.setClass(mActivity, NotePadActivity.class);
            intent.putExtra("date", DateUtil.getDateSimpleString());
            mActivity.startActivity(intent);
            break;
        case R.id.btn_record:
            intent.setClass(mActivity, RecordActivity.class);
            mActivity.startActivity(intent);
            break;
        case R.id.btn_take_photo:
            intent.setAction("android.media.action.IMAGE_CAPTURE");
            ((MainActivity) mActivity).startActivityForResult(intent, Constants.TAKE_PHOTO);
            break;
        case R.id.btn_vedio:
            intent.setAction("android.media.action.VIDEO_CAPTURE");
            ((MainActivity) mActivity).startActivityForResult(intent, Constants.VEDIO);
            break;
        default:
            break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
        case Constants.RECORD:
            break;
        case Constants.VEDIO:
            //          Bitmap bitmap2 = CommonUtil.getBitmap(imgPath2);
            //            bitmap2 = ThumbnailUtils.createVideoThumbnail(imgPath2,
            //                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            //            mActivity.popUpMediaView(bitmap2);
            NoteManager.addMediaNote(mActivity, requestCode);
            break;
        case Constants.TAKE_PHOTO:
            //            Bitmap bitmap = CommonUtil.getBitmap(imgPath);
            //            bitmap = (Bitmap) data.getExtras().get("data");
            //            mActivity.popUpMediaView(bitmap);
            NoteManager.addMediaNote(mActivity, requestCode);
            break;
        default:
            break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            if (mAdapter.canGoBack()) {
                mAdapter.goBack();
                return true;
            }
            break;

        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void notifyNoteChanged() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

}
