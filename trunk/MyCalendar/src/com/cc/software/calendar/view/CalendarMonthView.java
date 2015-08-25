package com.cc.software.calendar.view;

import hut.cc.software.calendar.R;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cc.software.calendar.activity.CalendarInfoActivity;
import com.cc.software.calendar.activity.NotePadActivity;
import com.cc.software.calendar.activity.RecordActivity;
import com.cc.software.calendar.activity.YearMonthActivity;
import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.dialog.ContextDialog;
import com.cc.software.calendar.dialog.ContextDialogListener;
import com.cc.software.calendar.util.CalendarManager;
import com.cc.software.calendar.util.CalendarUtil;
import com.cc.software.calendar.util.DateUtil;
import com.cc.software.calendar.util.NoteManager;

public class CalendarMonthView extends ScrollView implements OnClickListener, ContextDialogListener {

    public static int mDay = 1;
    private Context mActivity;
    private ImageView btnPrevious, btnNext;
    private TextView tvDate, tvCuttentMonth;
    private LinearLayout mRoot;
    private static final int COLOR_NORMAL = Color.WHITE, COLOR_HOLIDAY = Color.GREEN;
    private static final int[] WEEK_DAY = new int[] { R.string.sunday, R.string.monday, R.string.tuesday,
                    R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday };
    int mDirection = 0;
    private LinearLayout.LayoutParams parentParams;

    private LinearLayout.LayoutParams childParams;
    private LinearLayout weekLayout;

    public static final int daysOfAlmanac[] = { R.string.one, R.string.two, R.string.three, R.string.four,
                    R.string.five, R.string.six, R.string.seven, R.string.eight, R.string.nine, R.string.ten,
                    R.string.eleven, R.string.twelve, R.string.thirteen, R.string.fourteen, R.string.fifteen,
                    R.string.sixteen, R.string.seventeen, R.string.eighteen, R.string.nineteen, R.string.twenty,
                    R.string.twenty_one, R.string.twenty_two, R.string.twenty_three, R.string.twenty_four,
                    R.string.twenty_five, R.string.twenty_six, R.string.twenty_seven, R.string.twenty_eight,
                    R.string.twenty_nine, R.string.thirty };

    public static final int[] MONTH_OF_ALMANAC = { R.string.january, R.string.february, R.string.march, R.string.april,
                    R.string.may, R.string.june, R.string.july, R.string.auguest, R.string.september, R.string.october,
                    R.string.november, R.string.december };

    public CalendarMonthView(Context context) {
        super(context);
        mActivity = context;
        init();
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mActivity).inflate(R.layout.month_view, this);
        parentParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        childParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        childParams.weight = 1;

        tvCuttentMonth = (TextView) findViewById(R.id.current_btn);
        tvCuttentMonth.setText(R.string.current_month);
        tvDate = (TextView) findViewById(R.id.date_tv);
        btnPrevious = (ImageView) findViewById(R.id.previous);
        btnNext = (ImageView) findViewById(R.id.next);

        tvDate.setOnClickListener(this);
        tvCuttentMonth.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);

        mRoot = (LinearLayout) findViewById(R.id.root);

        weekLayout = new LinearLayout(mActivity);
        for (int i = 0; i < 7; i++) {
            weekLayout.setPadding(0, 2, 0, 2);
            //            weekLayout.setBackgroundResource(R.drawable.body_bg);
            TextView tView = new TextView(mActivity);
            tView.setGravity(Gravity.CENTER);
            tView.setText(WEEK_DAY[i]);
            weekLayout.addView(tView, childParams);
        }

        updateLayout(DateUtil.getYear(), DateUtil.getMonth());
    }

    public void updateLayout(int mYear, int mMonth) {
        mRoot.removeAllViews();
        mDay = 1;
        mRoot.addView(weekLayout, parentParams);
        tvDate.setText(DateUtil.getDateString(mYear, mMonth));

        CalendarUtil instance = CalendarUtil.getInstance();

        int weekday = CalendarManager.getDayOfWeek(mActivity, mYear, mMonth, 1); //Sunday is the first day.
        int monthDays = CalendarManager.getMonthDays(mYear, mMonth);

        for (int i = 1; i <= 6; i++) {
            LinearLayout parentLayout = new LinearLayout(mActivity);
            parentLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 1; j <= 7; j++) {
                View childLayout = LayoutInflater.from(mActivity).inflate(R.layout.day_block, null);
                //                childLayout.setBackgroundResource(R.drawable.day_bg);
                TextView dayInMonth = (TextView) childLayout.findViewById(R.id.day_in_month);
                TextView dayInMonthChina = (TextView) childLayout.findViewById(R.id.day_in_month_china);
                dayInMonth.setTextColor(COLOR_HOLIDAY);
                dayInMonthChina.setTextColor(COLOR_HOLIDAY);
                if (i == 1 && weekday > j) { //if the first day is not sunday, show draw but not write content.

                } else if (monthDays < mDay) {
                    if (i == 6 && j == 1) { //if five line cun show current month,six line not need to draw.
                        return;
                    }
                } else {
                    instance.setDate(mYear, DateUtil.getMonth(), mDay);
                    int chineseDate = instance.getChineseDate();
                    int chineseMonth = instance.getChineseMonth();
                    int gregorianHoliday = CalendarUtil.getGregorianCalendarHoliday(mYear, DateUtil.getMonth(), mDay); //锟斤拷锟斤拷锟斤拷锟�
                    int tradionHoliday = CalendarUtil.getTraditionHoliday(chineseMonth, chineseDate);

                    if (gregorianHoliday != -1 && tradionHoliday != -1) {
                        dayInMonth.setText(gregorianHoliday);
                        dayInMonthChina.setText(tradionHoliday);
                    } else if (gregorianHoliday != -1) {
                        dayInMonth.setText(mDay + "");
                        dayInMonthChina.setText(gregorianHoliday);
                    } else if (tradionHoliday != -1) {
                        dayInMonth.setText(mDay + "");
                        dayInMonthChina.setText(tradionHoliday);
                    } else {
                        dayInMonth.setTextColor(COLOR_NORMAL);
                        dayInMonthChina.setTextColor(COLOR_NORMAL);
                        dayInMonth.setText(mDay + "");
                        if (chineseDate == 1) {
                            dayInMonthChina.setText(MONTH_OF_ALMANAC[chineseMonth - 1]);
                        } else {
                            dayInMonthChina.setText(daysOfAlmanac[chineseDate - 1]);
                        }
                    }
                    if (isToday(mYear, DateUtil.getMonth(), mDay)) {
                        childLayout.setBackgroundResource(R.drawable.today_bg);
                        dayInMonth.setTextColor(Color.BLACK);
                        dayInMonthChina.setTextColor(Color.BLACK);
                    }
                    childLayout.setTag(DateUtil.getDateSimpleString(mYear, mMonth, mDay));
                    childLayout.setOnClickListener(dayClickListener);
                    mDay++;
                }
                parentLayout.addView(childLayout, childParams);
            }
            mRoot.addView(parentLayout, parentParams);
        }
        mRoot.requestLayout();
    }

    private OnClickListener dayClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Object tagNote = v.getTag();
            ContextDialog dialog = new ContextDialog(mActivity);
            dialog.setTitle((String) tagNote);
            dialog.addDialogEntry(R.string.look_up_day_info, tagNote);
            dialog.addDialogEntry(R.string.add_text_note, tagNote);
            dialog.addDialogEntry(R.string.add_image_note, tagNote);
            dialog.addDialogEntry(R.string.add_record_note, tagNote);
            dialog.addDialogEntry(R.string.add_video_note, tagNote);
            dialog.setOnItemSelectListener(CalendarMonthView.this);
            dialog.show();
        }
    };

    private void updateMonth() {
        int month = -1;
        int year = DateUtil.getYear();
        if (mDirection == -1) {
            if (DateUtil.getMonth() != 1) {
                month = DateUtil.getMonth() - 1;
            } else {
                year -= 1;
                month = 12;
            }
        } else if (mDirection == 1) {
            if (DateUtil.getMonth() < 12) {
                month = DateUtil.getMonth() + 1;
            } else {
                year += 1;
                month = 1;
            }
        }
        if (month != -1) {
            DateUtil.setMonth(month);
            DateUtil.setYear(year);
        }
    }

    private boolean isToday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DATE);
        if (year == currentYear && month == currentMonth && day == currentDay) {
            return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
        case Constants.RECORD:
            break;
        case Constants.VEDIO:
            NoteManager.addMediaNote(mActivity, requestCode);
            break;
        case Constants.TAKE_PHOTO:
            NoteManager.addMediaNote(mActivity, requestCode);
            break;
        case Constants.YEAR_OR_MONT:
            if (resultCode == Constants.RESUSTCODE_YEAR_MONTH) {
                int year = data.getIntExtra("year", DateUtil.getYear());
                int month = data.getIntExtra("month", DateUtil.getMonth());
                updateLayout(year, month);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.previous:
            mDirection = -1;
            updateMonth();
            updateLayout(DateUtil.getYear(), DateUtil.getMonth());
            break;
        case R.id.next:
            mDirection = 1;
            updateMonth();
            updateLayout(DateUtil.getYear(), DateUtil.getMonth());
            break;
        case R.id.current_btn:
            DateUtil.initCurrentTime();
            updateLayout(DateUtil.getYear(), DateUtil.getMonth());
            break;
        case R.id.date_tv:
            ((Activity) mActivity).startActivityForResult(new Intent(mActivity, YearMonthActivity.class),
                            Constants.YEAR_OR_MONT);
        default:
            break;
        }

    }

    @Override
    public boolean onDialogItemClicked(int tag, Object tagNote) {
        Intent intent = new Intent();
        intent.putExtra("date", (String) tagNote);
        switch (tag) {
        case R.string.look_up_day_info:
            intent.setClass(mActivity, CalendarInfoActivity.class);
            mActivity.startActivity(intent);
            break;
        case R.string.add_image_note:
            intent.setAction("android.media.action.IMAGE_CAPTURE");
            ((Activity) mActivity).startActivityForResult(intent, Constants.TAKE_PHOTO);
            break;
        case R.string.add_record_note:
            intent.setClass(mActivity, RecordActivity.class);
            mActivity.startActivity(intent);
            break;
        case R.string.add_text_note:
            intent.setClass(mActivity, NotePadActivity.class);
            mActivity.startActivity(intent);
            break;
        case R.string.add_video_note:
            intent.setAction("android.media.action.VIDEO_CAPTURE");
            ((Activity) mActivity).startActivityForResult(intent, Constants.VEDIO);
            break;

        default:
            break;
        }
        return false;
    }

}
