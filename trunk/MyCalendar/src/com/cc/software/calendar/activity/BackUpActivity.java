package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.cc.software.calendar.helper.ContactsBackUpTask;
import com.cc.software.calendar.helper.SmsBackUpTask;
import com.cc.software.calendar.util.CommonUtil;

public class BackUpActivity extends Activity implements OnClickListener {

    private TextView mSmsBackUp, mContactBackUp, mNoteBackUp, mBackUpHigh, mBackUpLow, mRecoverHigh, mRecoverLow;
    private static final int backUpContacts = 1, backUpSms = 2, backUpNote = 3;
    private int currentState = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.back_up_main);
        initComponent();
    }

    private void initComponent() {
        findViewById(R.id.back_root).setBackgroundDrawable(CommonUtil.getBackGround(this));
        mSmsBackUp = (TextView) findViewById(R.id.back_up_sms);
        mContactBackUp = (TextView) findViewById(R.id.back_up_contact);
        mNoteBackUp = (TextView) findViewById(R.id.back_up_note);
        mBackUpHigh = (TextView) findViewById(R.id.back_up_high);
        mBackUpLow = (TextView) findViewById(R.id.back_up_low);
        mRecoverHigh = (TextView) findViewById(R.id.recover_high);
        mRecoverLow = (TextView) findViewById(R.id.recover_low);
        mSmsBackUp.setOnClickListener(this);
        mContactBackUp.setOnClickListener(this);
        mNoteBackUp.setOnClickListener(this);
        findViewById(R.id.sync).setOnClickListener(this);
        findViewById(R.id.recover).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.back_up_contact:
            updateView(v.getId());
            setButtonEnable(backUpContacts);

            break;
        case R.id.back_up_note:
            updateView(v.getId());
            setButtonEnable(backUpNote);
            break;
        case R.id.back_up_sms:
            updateView(v.getId());
            setButtonEnable(backUpSms);
            break;
        case R.id.sync:
            if (currentState == backUpContacts) {
                ContactsBackUpTask mInitTask = new ContactsBackUpTask(this);
                mInitTask.execute(this);
            } else if (currentState == backUpNote) {

            } else if (currentState == backUpSms) {
                SmsBackUpTask mTask = new SmsBackUpTask(this);
                mTask.execute(this);
            }
            break;
        case R.id.recover:
            if (currentState == backUpContacts) {
                ContactsBackUpTask mInitTask = new ContactsBackUpTask(this, "contact.xml");
                mInitTask.execute(this);
            } else if (currentState == backUpNote) {

            } else if (currentState == backUpSms) {
                //getContentResolver().delete(Uri.parse("content://sms"), null, null);
                SmsBackUpTask mRecoverTask = new SmsBackUpTask(this, "sms.xml");
                mRecoverTask.execute(this);
            }
            break;

        default:
            break;
        }
    }

    private void setButtonEnable(int buttonPosition) {
        currentState = buttonPosition;
        mContactBackUp.setEnabled(buttonPosition != backUpContacts);
        mNoteBackUp.setEnabled(buttonPosition != backUpNote);
        mSmsBackUp.setEnabled(buttonPosition != backUpSms);
    }

    private void updateView(int id) {
        switch (id) {
        case R.id.back_up_contact:
            mBackUpHigh.setText(R.string.back_up_contacts);
            mBackUpLow.setText(R.string.back_up_contact_to_net);
            mRecoverHigh.setText(R.string.recover_contacts);
            mRecoverLow.setText(R.string.recover_contact_from_net);
            break;
        case R.id.back_up_note:
            mBackUpHigh.setText(R.string.back_up_note);
            mBackUpLow.setText(R.string.back_up_note_to_net);
            mRecoverHigh.setText(R.string.recover_note);
            mRecoverLow.setText(R.string.recover_note_from_net);
            break;
        case R.id.back_up_sms:
            mBackUpHigh.setText(R.string.back_up_sms);
            mBackUpLow.setText(R.string.back_up_sms_to_net);
            mRecoverHigh.setText(R.string.recover_sms);
            mRecoverLow.setText(R.string.recover_sms_from_net);
            break;

        default:
            break;
        }

    }
}
