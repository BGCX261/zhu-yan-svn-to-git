package com.cc.software.calendar.clock;

import hut.cc.software.calendar.R;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cc.software.calendar.dialog.ContextDialog;
import com.cc.software.calendar.dialog.ContextDialogListener;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.DialogUtil;

public class AlarmClock extends Activity implements OnClickListener {

    final static String PREFERENCES = "AlarmClock";
    final static int SET_ALARM = 1;
    final static String PREF_CLOCK_FACE = "face";
    final static String PREF_SHOW_CLOCK = "show_clock";

    /** Cap alarm count at this number */
    final static int MAX_ALARM_COUNT = 12;

    /** This must be false for production.  If true, turns on logging,
        test code, etc. */
    final static boolean DEBUG = false;

    private SharedPreferences mPrefs;
    private LayoutInflater mFactory;
    private ViewGroup mClockLayout;
    private View mClock = null;
    private ListView mAlarmsList;
    private Cursor mCursor;
    private TextView mShowHide;

    /**
     * Which clock face to show
     */
    private int mFace = -1;

    /*
     * FIXME: it would be nice for this to live in an xml config file.
     */
    final static int[] CLOCKS = { R.layout.clock_basic_bw, R.layout.clock_googly, R.layout.clock_droid2,
                    R.layout.clock_droids, R.layout.clock_digital_clock
    //        R.layout.main
    };

    private class AlarmTimeAdapter extends CursorAdapter {
        public AlarmTimeAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View ret = mFactory.inflate(R.layout.clock_alarm_time, parent, false);
            DigitalClock digitalClock = (DigitalClock) ret.findViewById(R.id.digitalClock);
            digitalClock.setLive(false);
            return ret;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int id = cursor.getInt(Alarms.AlarmColumns.ALARM_ID_INDEX);
            final int hour = cursor.getInt(Alarms.AlarmColumns.ALARM_HOUR_INDEX);
            final int minutes = cursor.getInt(Alarms.AlarmColumns.ALARM_MINUTES_INDEX);
            final Alarms.DaysOfWeek daysOfWeek = new Alarms.DaysOfWeek(
                            cursor.getInt(Alarms.AlarmColumns.ALARM_DAYS_OF_WEEK_INDEX));
            final boolean enabled = cursor.getInt(Alarms.AlarmColumns.ALARM_ENABLED_INDEX) == 1;
            final String label = cursor.getString(Alarms.AlarmColumns.ALARM_MESSAGE_INDEX);
            CheckBox onButton = (CheckBox) view.findViewById(R.id.alarmButton);
            onButton.setChecked(enabled);
            onButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    boolean isChecked = ((CheckBox) v).isChecked();
                    Alarms.enableAlarm(AlarmClock.this, id, isChecked);
                    if (isChecked) {
                        SetAlarm.popAlarmSetToast(AlarmClock.this, hour, minutes, daysOfWeek);
                    }
                }
            });

            DigitalClock digitalClock = (DigitalClock) view.findViewById(R.id.digitalClock);

            digitalClock.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (true) {
                        Intent intent = new Intent(AlarmClock.this, SetAlarm.class);
                        intent.putExtra(Alarms.ID, id);
                        startActivityForResult(intent, SET_ALARM);
                    }
                    /*else {
                        // TESTING: immediately pop alarm
                        Intent fireAlarm = new Intent(AlarmClock.this, AlarmAlert.class);
                        fireAlarm.putExtra(Alarms.ID, id);
                        fireAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(fireAlarm);
                    }
                    */}
            });

            // set the alarm text
            final Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minutes);
            digitalClock.updateTime(c);

            // Set the repeat text or leave it blank if it does not repeat.
            TextView daysOfWeekView = (TextView) digitalClock.findViewById(R.id.daysOfWeek);
            final String daysOfWeekStr = daysOfWeek.toString(AlarmClock.this, false);
            if (daysOfWeekStr != null && daysOfWeekStr.length() != 0) {
                daysOfWeekView.setText(daysOfWeekStr);
                daysOfWeekView.setVisibility(View.VISIBLE);
            } else {
                daysOfWeekView.setVisibility(View.GONE);
            }

            // Display the label
            TextView labelView = (TextView) digitalClock.findViewById(R.id.label);
            if (label != null && label.length() != 0) {
                labelView.setText(label);
            } else {
                labelView.setText(R.string.default_label);
            }

            digitalClock.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    ContextDialog dialog = new ContextDialog(AlarmClock.this);
                    dialog.addDialogEntry(R.string.delete_alarm, id);
                    dialog.setTitle(Alarms.formatTime(AlarmClock.this, c));
                    dialog.setOnItemSelectListener(new ContextDialogListener() {

                        @Override
                        public boolean onDialogItemClicked(int tag, final Object tagNote) {
                            DialogUtil.createConfirmDeleteDialog(AlarmClock.this,
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which) {
                                                    case Dialog.BUTTON1:
                                                        Alarms.deleteAlarm(AlarmClock.this, (Integer) tagNote);
                                                        break;
                                                    }
                                                    dialog.dismiss();
                                                }
                                            }).show();

                            return false;
                        }
                    });
                    dialog.show();
                    return true;
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // sanity check -- no database, no clock
        if (getContentResolver() == null) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(getString(R.string.dberror))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert).create().show();
            return;
        }

        setContentView(R.layout.clock_alarm_clock);

        findViewById(R.id.add_alarm_clock).setOnClickListener(this);
        mShowHide = (TextView) findViewById(R.id.show_hide);
        mShowHide.setOnClickListener(this);
        findViewById(R.id.setting).setOnClickListener(this);

        findViewById(R.id.base_layout).setBackgroundDrawable(CommonUtil.getBackGround(this));
        mFactory = LayoutInflater.from(this);
        mPrefs = getSharedPreferences(PREFERENCES, 0);

        mCursor = Alarms.getAlarmsCursor(getContentResolver());
        mAlarmsList = (ListView) findViewById(R.id.alarms_list);
        mAlarmsList.setAdapter(new AlarmTimeAdapter(this, mCursor));
        mAlarmsList.setVerticalScrollBarEnabled(true);
        mAlarmsList.setItemsCanFocus(true);

        mClockLayout = (ViewGroup) findViewById(R.id.clock_layout);
        mClockLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent intent = new Intent(AlarmClock.this, ClockPicker.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        setClockVisibility(mPrefs.getBoolean(PREF_SHOW_CLOCK, true));
    }

    @Override
    protected void onResume() {
        super.onResume();

        int face = mPrefs.getInt(PREF_CLOCK_FACE, 0);
        if (mFace != face) {
            if (face < 0 || face >= AlarmClock.CLOCKS.length)
                mFace = 0;
            else
                mFace = face;
            inflateClock();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastMaster.cancelToast();
        mCursor.deactivate();
    }

    protected void inflateClock() {
        if (mClock != null) {
            mClockLayout.removeView(mClock);
        }
        mClock = mFactory.inflate(CLOCKS[mFace], null);
        mClockLayout.addView(mClock, 0);
    }

    private boolean getClockVisibility() {
        return mClockLayout.getVisibility() == View.VISIBLE;
    }

    private void setClockVisibility(boolean visible) {
        mClockLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void saveClockVisibility() {
        mPrefs.edit().putBoolean(PREF_SHOW_CLOCK, getClockVisibility()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.add_alarm_clock:
            Uri uri = Alarms.addAlarm(getContentResolver());
            // FIXME: scroll to new item.  mAlarmsList.requestChildRectangleOnScreen() ?
            String segment = uri.getPathSegments().get(1);
            int newId = Integer.parseInt(segment);
            if (Log.LOGV)
                Log.v("In AlarmClock, new alarm id = " + newId);
            Intent intent = new Intent(AlarmClock.this, SetAlarm.class);
            intent.putExtra(Alarms.ID, newId);
            startActivityForResult(intent, SET_ALARM);
            break;
        case R.id.show_hide:
            setClockVisibility(!getClockVisibility());

            mShowHide.setText(getClockVisibility() ? R.string.show_clock : R.string.hide_clock);

            saveClockVisibility();
            break;
        case R.id.setting:
            startActivity(new Intent(this, SettingsActivity.class));
            break;

        default:
            break;
        }
    }
}
