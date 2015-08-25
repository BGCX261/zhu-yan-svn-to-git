package com.cc.software.calendar.view;

import hut.cc.software.calendar.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cc.software.calendar.activity.MainActivity;
import com.cc.software.calendar.activity.YearMonthDayActivity;
import com.cc.software.calendar.bean.CalendarInfo;
import com.cc.software.calendar.bean.HolidayInfo;
import com.cc.software.calendar.bean.NoteInfo;
import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.dialog.ContextDialog;
import com.cc.software.calendar.dialog.ContextDialogListener;
import com.cc.software.calendar.util.CalendarManager;
import com.cc.software.calendar.util.CalendarUtil;
import com.cc.software.calendar.util.DateUtil;
import com.cc.software.calendar.util.NoteManager;

public class CalendarDayView extends LinearLayout implements OnClickListener, OnItemClickListener,
                OnItemLongClickListener, ContextDialogListener {

    private Context mContext;
    private TextView mDate, mCurrentDayBtn, mTraditonDate, mDayInweek, mHoliday, mTradionYearInfo, mFitting, mForbid;
    private ImageView btnPrevious, btnNext, mFirstNum, mSecondNumber;
    private int mDirection;
    private ListView mDayNotelist;
    private DayNoteAdater mDayNoteAdater;
    private String simpDat = null;

    public CalendarDayView(Context context) {
        super(context, null);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.day_view, this);
        initComponent();
    }

    public CalendarDayView(Context context, AttributeSet attribute) {
        super(context, attribute);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.day_view, this);
        initComponent();
    }

    private void initComponent() {
        setOrientation(LinearLayout.VERTICAL);
        mCurrentDayBtn = (TextView) findViewById(R.id.current_btn);
        mCurrentDayBtn.setText(R.string.today);
        mDate = (TextView) findViewById(R.id.date_tv);
        btnPrevious = (ImageView) findViewById(R.id.previous);
        btnNext = (ImageView) findViewById(R.id.next);
        mFirstNum = (ImageView) findViewById(R.id.first_number);
        mSecondNumber = (ImageView) findViewById(R.id.second_number);
        //        mCurrentDay = (TextView) findViewById(R.id.day_tv);
        mTradionYearInfo = (TextView) findViewById(R.id.tv1);
        mTraditonDate = (TextView) findViewById(R.id.tv2);
        mDayInweek = (TextView) findViewById(R.id.tv3);
        mHoliday = (TextView) findViewById(R.id.tv4);
        mFitting = (TextView) findViewById(R.id.fitting);
        mForbid = (TextView) findViewById(R.id.forbid);
        mDayNotelist = (ListView) findViewById(R.id.today_note_list);

        mDayNotelist.setOnItemClickListener(this);
        mDayNotelist.setOnItemLongClickListener(this);
        mCurrentDayBtn.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        mDate.setOnClickListener(this);

        mDayNoteAdater = new DayNoteAdater();
        
        updateContent(DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getDay());
        
    }

    private int getDayDrawableId(int number) {
        int resId = -1;
        switch (number) {
        case 0:
            resId = R.drawable.n0;
            break;
        case 1:
            resId = R.drawable.n1;
            break;
        case 2:
            resId = R.drawable.n2;
            break;
        case 3:
            resId = R.drawable.n3;
            break;
        case 4:
            resId = R.drawable.n4;
            break;
        case 5:
            resId = R.drawable.n5;
            break;
        case 6:
            resId = R.drawable.n6;
            break;
        case 7:
            resId = R.drawable.n7;
            break;
        case 8:
            resId = R.drawable.n8;
            break;
        case 9:
            resId = R.drawable.n9;
            break;

        default:
            break;
        }
        return resId;
    }

    public void updateContent(int year, int month, int day) {
        mDate.setText(DateUtil.getDateString(year, month, day));
        if (day < 10) {
            mFirstNum.setImageResource(getDayDrawableId(0));
            mSecondNumber.setImageResource(getDayDrawableId(day));
        } else {
            int first = day / 10;
            int second = day % 10;
            mFirstNum.setImageResource(getDayDrawableId(first));
            mSecondNumber.setImageResource(getDayDrawableId(second));
        }
        //        mCurrentDay.setText(dayString);
        CalendarInfo info = CalendarManager.getCalendarInfoByDate(mContext, year, month, day);
        if (info == null) {
            mTraditonDate.setText(CalendarUtil.getTraditionDate(mContext, year, month, day));            
            mDayInweek.setText(CalendarUtil.getDayofWeekAndConstellation(mContext, year, month, day));
            mTradionYearInfo.setText(CalendarUtil.getTradionYearGanzhi(year));
            mFitting.setText("无数据");
            mForbid.setText("无数据");
        } else {
            String tradtionInfo = info.getTraCalendar();
            mTraditonDate.setText(tradtionInfo.substring(0, 7));
            mDayInweek.setText(tradtionInfo.substring(8));
            mTradionYearInfo.setText(info.getGanZhi());
            
            mFitting.setText(info.getFitting());
            mForbid.setText(info.getForbid());
        }
        mHoliday.setText(getHolidayStrings(year, month, day));
        simpDat = DateUtil.getDateSimpleString(year, month, day);
        mDayNoteAdater.setNoteDate(getDayNotes(simpDat));
    }

    private String getHolidayStrings(int year, int month, int day) {
        int resId = CalendarUtil.getGregorianCalendarHoliday(year, month, day);
        if (resId != -1) {
            return mContext.getString(resId);
        }
        int chineseMonth = CalendarUtil.getChineseMonth(year, month, day);
        int chineseDay = CalendarUtil.getChineseDay(year, month, day);
        resId = CalendarUtil.getTraditionHoliday(chineseMonth, chineseDay);
        if (resId != -1) {
            return mContext.getString(resId);
        }
        StringBuilder holidays = new StringBuilder();
        ArrayList<HolidayInfo> infos = CalendarUtil.getGregorianCalendarHoliday1(year, month);
        for (int i = 0, count = infos.size(); i < count; i++) {
            if (i != 0 && i != count) {
                holidays.append(";");
            }
            HolidayInfo info = infos.get(i);
            holidays.append(info.getDay() + mContext.getString(R.string.day));
            holidays.append(":" + mContext.getString(info.getHolidayResId()));
        }
        return holidays.toString();
    }

    private void updateDay() {
        int day = -1;
        int month = DateUtil.getMonth();
        int year = DateUtil.getYear();
        int dayCount = CalendarManager.getMonthDays(year, month);
        if (mDirection == -1) {
            if (DateUtil.getDay() != 1) {
                day = DateUtil.getDay() - 1;
            } else {
                day = CalendarManager.getMonthDays(year, month - 1);
                if (month != 1) {
                    month = DateUtil.getMonth() - 1;
                } else {
                    month = 12;
                    year -= 1;
                }
            }
        } else if (mDirection == 1) {
            if (DateUtil.getDay() < dayCount) {
                day = DateUtil.getDay() + 1;
            } else {
                day = 1;
                if (DateUtil.getMonth() < 12) {
                    month = DateUtil.getMonth() + 1;
                } else {
                    year += 1;
                    month = 1;
                }
            }
        }
        if (day != -1) {
            DateUtil.setDay(day);
            DateUtil.setMonth(month);
            DateUtil.setYear(year);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.previous:
            mDirection = -1;
            updateDay();
            break;
        case R.id.next:
            mDirection = 1;
            updateDay();
            break;
        case R.id.current_btn:
            DateUtil.initCurrentTime();
            break;
        case R.id.date_tv:
            ((Activity) mContext).startActivityForResult(new Intent(mContext, YearMonthDayActivity.class),
                            Constants.YEAR_OR_MONT);
            break;
        default:
            break;
        }
        updateContent(DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getDay());
    }

    public ArrayList<NoteInfo> getDayNotes(String simpDate) {
        return NoteManager.getNoteInfosByDate(mContext, simpDate);
    }

    public void onNoteDataChanged() {
        mDayNoteAdater.setNoteDate(NoteManager.getNoteInfosByDate(mContext, simpDat));
    }

    public void notifyNoteDataChange() {
        if (mDayNotelist.getAdapter() == null) {
            mDayNotelist.setAdapter(mDayNoteAdater);
        } else {
            mDayNoteAdater.notifyDataSetChanged();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.YEAR_OR_MONT && resultCode == Constants.RESUSTCODE_YEAR_MONTH) {
            int year = data.getIntExtra("year", DateUtil.getYear());
            int month = data.getIntExtra("month", DateUtil.getMonth());
            int day = data.getIntExtra("day", DateUtil.getDay());
            updateContent(year, month, day);
        }
    }

    private class DayNoteAdater extends BaseAdapter {

        private ArrayList<NoteInfo> noteInfos;

        public void setNoteDate(ArrayList<NoteInfo> infos) {
            noteInfos = infos;
            notifyNoteDataChange();
        }

        @Override
        public int getCount() {
            if (noteInfos == null || noteInfos.size() == 0) {
                return 1;
            }
            return noteInfos.size();
        }

        @Override
        public Object getItem(int position) {
            if (noteInfos != null)
                return noteInfos.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dayview_list_item, null);
            }
            TextView noteView = (TextView) convertView.findViewById(R.id.note_title);
            ImageView imgType = (ImageView) convertView.findViewById(R.id.img_type);
            if (noteInfos == null) {
                noteView.setText(R.string.day_no_note);
                imgType.setBackgroundResource(R.drawable.clock_add_d);
            } else {
                NoteInfo info = (NoteInfo) getItem(position);
                noteView.setText(info.getTitle());
                convertView.setTag(info);
                imgType.setBackgroundResource(NoteManager.getNotePicIdByType(info.getType()));
            }

            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteInfo info = (NoteInfo) view.getTag();
        if (info == null) {
            ((MainActivity) mContext).onNoteViewClick();
            return;
        }
        if (info.getType() == Constants.NOTE) {
            NoteManager.viewTextNote(mContext, info);
        } else if (info.getType() == Constants.TAKE_PHOTO) {
            NoteManager.viewImageNote(mContext, info);
        } else if (info.getType() == Constants.VEDIO) {
            NoteManager.viewVedioNote(mContext, info);
        } else if (info.getType() == Constants.RECORD) {
            NoteManager.viewAudioNote(mContext, info);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getTag() == null) {
            return false;
        }
        NoteInfo tagNote = (NoteInfo) view.getTag();
        ContextDialog dialog = new ContextDialog(mContext);
        dialog.setTitle(tagNote.getTitle());
        dialog.addDialogEntry(R.string.note_viewer, tagNote);
        if (tagNote.getType() == Constants.NOTE) {
            dialog.addDialogEntry(R.string.note_edit, tagNote);
        }
        dialog.addDialogEntry(R.string.note_delete, tagNote);
        dialog.setOnItemSelectListener(this);
        dialog.show();
        return false;
    }

    @Override
    public boolean onDialogItemClicked(int tag, Object tagNote) {

        NoteInfo info = (NoteInfo) tagNote;
        switch (tag) {
        case R.string.note_delete:
            NoteManager.deleteNote(mContext, info.getId());
            break;
        case R.string.note_edit:
            NoteManager.viewTextNote(mContext, info);
            break;
        case R.string.note_viewer:
            if (info.getType() == Constants.NOTE) {
                NoteManager.viewTextNote(mContext, info);
            } else if (info.getType() == Constants.TAKE_PHOTO) {
                NoteManager.viewImageNote(mContext, info);
            } else if (info.getType() == Constants.VEDIO) {
                NoteManager.viewVedioNote(mContext, info);
            } else if (info.getType() == Constants.RECORD) {
                NoteManager.viewAudioNote(mContext, info);
            }
            break;
        }
        return false;
    }
}
