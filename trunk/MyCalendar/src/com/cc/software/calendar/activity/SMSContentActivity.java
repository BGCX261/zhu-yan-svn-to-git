package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import java.util.ArrayList;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cc.software.calendar.bean.SmsInfo;
import com.cc.software.calendar.dialog.CustomDialog;
import com.cc.software.calendar.helper.AsyncJobHandler;
import com.cc.software.calendar.helper.IAsyncQueryJob;
import com.cc.software.calendar.observer.CommomObserver;
import com.cc.software.calendar.observer.SMSReceiver;
import com.cc.software.calendar.util.CommonUtil;

public class SMSContentActivity extends Activity implements IAsyncQueryJob, OnItemClickListener, OnClickListener {
    ListView mList;
    smsAdapter mAdapter;
    private Handler handler;
    private AsyncJobHandler mAsyncJobHandler;
    private TextView sortByContacts, sortByDate, noRead;
    private static final int showByContacts = 1, showByDate = 2, showNoReadSms = 3;
    private CustomDialog mCustomLoginProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sms_content_list);
        initComponent();
        loadSMSContentInAnotherThread(showByContacts);
        registerContentObserver();
        if (SMSReceiver.mNotificationManager != null) {
            NotificationManager nManager = SMSReceiver.mNotificationManager;
            nManager.cancel(SMSReceiver.NOTIFICATION_ID);
        }
    }

    public void initComponent() {
        mAsyncJobHandler = AsyncJobHandler.getInstance();
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
        findViewById(R.id.sms_content).setBackgroundDrawable(CommonUtil.getBackGround(this));
        mAdapter = new smsAdapter();
        sortByContacts = (TextView) findViewById(R.id.sort_by_contact);
        sortByDate = (TextView) findViewById(R.id.sort_by_date);
        noRead = (TextView) findViewById(R.id.no_read);

        sortByContacts.setOnClickListener(this);
        sortByDate.setOnClickListener(this);
        noRead.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAsyncJobHandler.release();
    }

    public void registerContentObserver() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == CommomObserver.CALLLOG_CONTENT_CHANGE) {
                    loadSMSContentInAnotherThread(showByContacts);
                }
            }
        };
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,
                        new CommomObserver(this, handler));
    }

    public void loadSMSContentInAnotherThread(int showFlag) {
        showProgressDialog();
        mAsyncJobHandler.startJob(this, showFlag);
    }

    @Override
    public Object doAsyncJob(Object jobArg) {
        int showFlag = (Integer) jobArg;
        Cursor cursor = null;
        if (showFlag == SMSContentActivity.showByContacts) {
            cursor = getContentResolver().query(Uri.parse("content://mms-sms/conversations"), SmsInfo.SMS_PROJECTION,
                            null, null, "date desc");
            //            initPhoneNumbers();
        } else if (showFlag == SMSContentActivity.showByDate) {
            cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), SmsInfo.SMS_PROJECTION, null, null,
                            " date DESC");
        } else if (showFlag == SMSContentActivity.showNoReadSms) {
            cursor = getContentResolver().query(Uri.parse("content://sms/"), SmsInfo.SMS_PROJECTION, "read =0", null,
                            null);
        }
        ArrayList<SmsInfo> smsInfos = SmsInfo.getSmsInfos(this, cursor);
        cursor.close();
        return smsInfos;
    }

    @Override
    public void asyncJobDone(Object result, Object jobArg) {
        @SuppressWarnings("unchecked")
        ArrayList<SmsInfo> smsInfos = (ArrayList<SmsInfo>) result;
        dissmissProgressDialog();
        if (smsInfos == null || smsInfos.size() == 0) {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
            findViewById(R.id.list_container).setVisibility(View.GONE);
            return;
        }
        findViewById(R.id.list_container).setVisibility(View.VISIBLE);
        findViewById(R.id.empty).setVisibility(View.GONE);
        mAdapter.bindSMSList(smsInfos);
        if(mList.getAdapter() == null){
            mList.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
        
    }

    /* public void initPhoneNumbers() {
         threadIds.clear();
         for (int i = 0; i < cursor.getCount(); i++) {
             cursor.moveToPosition(i);
             String number = cursor.getString(cursor.getColumnIndexOrThrow("thread_id"));
             threadIds.add(number);
         }
     }*/
    class Holder {
        TextView tbody;
        TextView tnum;
        TextView date;
    }

    public class smsAdapter extends BaseAdapter {
        ArrayList<SmsInfo> smsInfos;

        public void bindSMSList(ArrayList<SmsInfo> Infos) {
            smsInfos = Infos;
        }

        @Override
        public int getCount() {
            return smsInfos.size();
        }

        @Override
        public Object getItem(int arg0) {
            return smsInfos.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            return composeItem(position, convertView);
        }

        private View composeItem(int position, View convertView) {
            Holder holder;
            if (convertView == null) {
                convertView = SMSContentActivity.this.getLayoutInflater()
                                .inflate(R.layout.sms_content_list_entry, null);
                holder = new Holder();
                holder.tbody = (TextView) convertView.findViewById(R.id.content);
                holder.tnum = (TextView) convertView.findViewById(R.id.name);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                convertView.setTag(convertView.getId(), holder);
            } else {
                holder = (Holder) convertView.getTag(convertView.getId());
            }


            SmsInfo info = (SmsInfo) getItem(position);
            holder.tnum.setText(info.getContact_name());
            holder.date.setText(info.getDate());
            holder.tbody.setText(info.getBody());
            convertView.setTag(convertView.hashCode(),info.getThread_id());

            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String uri = "content://mms-sms/conversations/" + view.getTag(view.hashCode());
        Intent intent = new Intent();
        intent.setData(Uri.parse(uri));
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.sort_by_contact:            
            setButtonEnable(SMSContentActivity.showByContacts);
            loadSMSContentInAnotherThread(SMSContentActivity.showByContacts);
            break;
        case R.id.sort_by_date:            
            setButtonEnable(SMSContentActivity.showByDate);
            loadSMSContentInAnotherThread(SMSContentActivity.showByDate);
            break;
        case R.id.no_read:            
            setButtonEnable(SMSContentActivity.showNoReadSms);
            loadSMSContentInAnotherThread(SMSContentActivity.showNoReadSms);
            break;
        default:
            break;
        }
    }

    public void setButtonEnable(int buttonPosition) {
        sortByContacts.setEnabled(buttonPosition != SMSContentActivity.showByContacts);
        sortByDate.setEnabled(buttonPosition != SMSContentActivity.showByDate);
        noRead.setEnabled(buttonPosition != SMSContentActivity.showNoReadSms);
    }
    
    private void showProgressDialog(){
        if(mCustomLoginProgressDialog==null){
            mCustomLoginProgressDialog = new CustomDialog(this);
        }
        mCustomLoginProgressDialog.setTitle("正在加载");
        mCustomLoginProgressDialog.showDilemiterLineAboveButton(false);
        mCustomLoginProgressDialog.addContentView(R.layout.progress_bar_content);
        mCustomLoginProgressDialog.show();
    }
    
    private void dissmissProgressDialog() {
        if (mCustomLoginProgressDialog != null) {
            mCustomLoginProgressDialog.dismiss();
        }
    }

}
