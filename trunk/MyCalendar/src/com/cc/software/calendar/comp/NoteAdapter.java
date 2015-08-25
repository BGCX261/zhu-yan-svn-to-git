package com.cc.software.calendar.comp;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cc.software.calendar.bean.NoteInfo;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.NoteManager;

public class NoteAdapter extends CursorAdapter implements OnItemClickListener, OnItemLongClickListener {

    private int mCurrent = 0;
    private Context mContext;

    private ListView mList;

    public NoteAdapter(Context context) {
        super(context, null);
        mContext = context;
    }

    public void bindList(ListView list) {
        mList = list;
        mList.setOnItemClickListener(this);

        loadData(0);
    }

    private void loadData(int parent) {
        mCurrent = parent;
        Cursor cursor = NoteManager.getNoteByParent(mContext, mCurrent);
        if (cursor.getCount() == 0) {            
            CommonUtil.showToastMessage(R.string.no_folder, mContext);
            return;
        }
        Cursor currentCursor = getCursor();
        if (currentCursor != null) {
            ((Activity) mContext).stopManagingCursor(currentCursor);
        }
        changeCursor(cursor);
        ((Activity) mContext).startManagingCursor(cursor);
        if (mList.getAdapter() != null) {
            notifyDataSetChanged();
        } else {
            mList.setAdapter(this);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        NoteInfo info = NoteInfo.getNoteInfoFromAdapterCursot(cursor);
        int resId = NoteManager.getNotePicIdByType(info.getType());
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        if (resId != -1) {
            icon.setImageResource(resId);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView fileCount = (TextView) view.findViewById(R.id.file_count);
        //        TextView lastEdit = (TextView) view.findViewById(R.id.last_edit_time);
        title.setText(info.getTitle());
        if (info.getType() == 0) {
            fileCount.setText(NoteManager.getNoteCountStringByParent(mContext, info.getId()));
            icon.setImageResource(getImageId(info.getId()));
        } else {
            fileCount.setText(NoteManager.getNoteFileSize(mContext, info.getPath()));
        }
        view.setTag(info);
        //        lastEdit.setText(NoteManager.getLastEditTimeString(mContext,
        //                info.getDate()));
    }

    private int getImageId(int id) {
        int resId = -1;
        switch (id) {
        case 1:
            resId = R.drawable.folder_note;
            break;
        case 2:
            resId = R.drawable.folder_image;
            break;
        case 3:
            resId = R.drawable.folder_record;
            break;
        case 4:
            resId = R.drawable.folder_vedio;
            break;
        default:
            break;
        }
        if (resId == -1)
            resId = R.drawable.folder_note;
        return resId;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteInfo info = (NoteInfo) view.getTag();
        if (info.getType() == 0) {
            loadData(info.getId());
        } else if (info.getType() == Constants.NOTE) {
            NoteManager.viewTextNote(mContext, info);
        } else if (info.getType() == Constants.TAKE_PHOTO) {
            NoteManager.viewImageNote(mContext, info);
        } else if (info.getType() == Constants.VEDIO) {
            NoteManager.viewVedioNote(mContext, info);
        } else if (info.getType() == Constants.RECORD) {
            NoteManager.viewAudioNote(mContext, info);
        }
    }

    public boolean canGoBack() {
        if (mCurrent != 0) {
            return true;
        }
        return false;
    }

    public void goBack() {
        loadData(0);
    }
}
