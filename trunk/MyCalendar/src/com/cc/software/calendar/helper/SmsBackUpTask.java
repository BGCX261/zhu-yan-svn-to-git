package com.cc.software.calendar.helper;

import hut.cc.software.calendar.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cc.software.calendar.util.CommonUtil;

public class SmsBackUpTask extends AsyncTask<Context, Integer, String> {
    private NodeList smsList;
    private Cursor cursor;
    private Context mContext;
    private File mFile;
    private boolean isBackUp = false;
    public static final String DIRECTORY = "/mnt/sdcard/calendar";
    private ProgressDialog mProgressBar;

    public static final String getDateString() {
        String dateString = "";
        Date date = new Date();
        dateString += "_" + (date.getYear() + 1900) + "_" + (date.getMonth() + 1) + "_" + date.getDate() + "_"
                        + date.getHours() + "_" + date.getMinutes();
        return dateString;
    }

    public SmsBackUpTask(Context context) {
        mContext = context;
        isBackUp = true;
        cursor = mContext.getContentResolver().query(Uri.parse("content://sms"), SMS_PROJECTION1, null, null,
                        "thread_id");
        initProgressBar(R.string.back_up_sms, cursor.getCount());
    }

    public SmsBackUpTask(Context context, String fileName) {
        mContext = context;
        isBackUp = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(DIRECTORY + "/" + fileName));
            Element root = document.getDocumentElement();
            smsList = root.getElementsByTagName("sms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (smsList == null) {
            return;
        }
        initProgressBar(R.string.recover_sms_from_net, smsList.getLength());
    }

    public void initProgressBar(int messageId, int len) {
        mProgressBar = new ProgressDialog(mContext);
        mProgressBar.setMessage(mContext.getString(messageId));
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setMax(len);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setCancelable(true);
        mProgressBar.show();
    }

    @Override
    protected String doInBackground(Context... params) {
        if (isBackUp) {
            mFile = new File(DIRECTORY);
            if (!mFile.isDirectory()) {
                mFile.mkdir();
            }
            String fileNames[]  = mFile.list();
            for(String str:fileNames){
                if(str.equals("sms.xml")){
                    mFile = new File(DIRECTORY + "/sms.xml");
                    mFile.delete();
                }
            }
            //mFile = new File(DIRECTORY + "/sms" + getDateString() + ".xml");
            mFile = new File(DIRECTORY + "/sms.xml");
            
            backupToXml();
            cursor.close();
        } else {
            if (smsList == null) {
                return "";
            }
            restoreFromXml();
        }
        return mContext.getString(R.string.operate_success);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressBar.setProgress(values[0] + 1);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // loadData();
        CommonUtil.showToastMessage(result, mContext);
        if (mProgressBar != null)
            mProgressBar.cancel();
    }

    private void backupToXml() {
        int i = 0;
        int count = cursor.getCount();
        write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<smsinfo>\n");
        while (i <= count && cursor.moveToNext()) {
            try {
                backupEntry(mContext, cursor);
                publishProgress(i);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        write("</smsinfo>\n");
    }

    private void restoreFromXml() {
        int i = 0;
        int count = smsList.getLength();
        //final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>(10);
        final ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        while (i < count) {
            insertOneSms(smsList.item(i), resolver, values);
            publishProgress(i);
            i++;
        }
    }

    private void insertOneSms(Node dataEntryNode, ContentResolver resolver, ContentValues values) {
        values.clear();
        if (dataEntryNode.getNodeType() == Node.ELEMENT_NODE) {
            NamedNodeMap attributes = ((Element) dataEntryNode).getAttributes();
            for (int k = 0, len2 = attributes.getLength(); k < len2; k++) {
                Node attribute = attributes.item(k);
                values.put(attribute.getNodeName().toString(), attribute.getNodeValue().toString());
            }
            resolver.insert(Uri.parse("content://sms"), values);
        }
    }

    public static final String SMS_PROJECTION1[] = { "address", "date", "read", "type", "body" };

    private void backupEntry(Context context, Cursor cursor) {
        if (cursor != null) {
            write("<sms ");
            write("address");
            write("=\"" + cursor.getString(0) + "\" ");

            write("date");
            write("=\"" + cursor.getString(1) + "\" ");

            write("read");
            write("=\"" + cursor.getString(2) + "\" ");

            write("type");
            write("=\"" + cursor.getString(3) + "\" ");
            write("body" + "=\"");
            String body = cursor.getString(4);
            if (body.contains("&")) {
                body = body.replace("&", "&amp;");

            }
            if (body.contains("\"")) {
                body = body.replace("\"", "&quot;");
            }
            if (body.contains("<") && body.contains(">")) {
                body = body.replace("<", "&lt;");
                body = body.replace(">", "&gt;");
            }
            write(body);
            write("\">\n");
            write("</sms>\n");
        }
    }

    public void write(String content) {
        if (TextUtils.isEmpty(content)) {
            content = "null";
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(mFile, true);
            os.write(content.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
