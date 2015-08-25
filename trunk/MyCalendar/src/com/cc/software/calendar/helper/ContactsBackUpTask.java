package com.cc.software.calendar.helper;

import hut.cc.software.calendar.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;

import com.cc.software.calendar.util.CommonUtil;

public class ContactsBackUpTask extends AsyncTask<Context, Integer, String> {
    private NodeList contactList;
    private Cursor mCursor;
    private Context mContext;
    private File mFile;
    public static final String DIRECTORY = "/mnt/sdcard/calendar";
    private ProgressDialog mProgressBar;
    public static final String DATA_PROJECTION[] = new String[] { Data.MIMETYPE, Data.DATA1, Data.DATA2, Data.DATA3,
                    Data.DATA4, Data.DATA5, Data.DATA6, Data.DATA7, Data.DATA8, Data.DATA9, Data.DATA10,
                    Data.RAW_CONTACT_ID };
    public static final String RAW_PROJECTION[] = new String[] { RawContacts._ID, RawContacts.ACCOUNT_NAME,
                    RawContacts.ACCOUNT_TYPE, RawContacts.STARRED };
    public static final String CONTACT_PROJECTION[] = new String[] { ContactsContract.Contacts._ID };
    private boolean isBackUp = false;

    public static final String getDateString() {
        String dateString = "";
        Date date = new Date();
        dateString += "_" + (date.getYear() + 1900) + "_" + (date.getMonth() + 1) + "_" + date.getDate() + "_"
                        + date.getHours() + "_" + date.getMinutes();
        return dateString;
    }

    public ContactsBackUpTask(Context context) {
        mContext = context;
        isBackUp = true;
        //int row = mContext.getContentResolver().delete(RawContacts.CONTENT_URI, "_id < 868", null);
        //mContext.getContentResolver().delete(ContactsContract.Contacts.CONTENT_URI, null, null);
        //mContext.getContentResolver().delete(ContactsContract.Data.CONTENT_URI, null, null);

        mCursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, CONTACT_PROJECTION, null,
                        null, "_id asc");
        initProgressBar(R.string.back_up_contacts, mCursor.getCount());
    }

    public ContactsBackUpTask(Context context, String fileName) {
        mContext = context;
        isBackUp = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(DIRECTORY + "/" + fileName));
            Element root = document.getDocumentElement();
            contactList = root.getElementsByTagName("contact");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initProgressBar(R.string.recover_contacts, contactList.getLength());

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
            String fileNames[] = mFile.list();
            for (String str : fileNames) {
                if (str.equals("contact.xml")) {
                    mFile = new File(DIRECTORY + "/contact.xml");
                    mFile.delete();
                }
            }
            mFile = new File(DIRECTORY + "/contact.xml");

            backupToXml();
            mCursor.close();
        } else {

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
        mProgressBar.cancel();
    }

    private void backupToXml() {
        int i = 0;        
        write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<contacts>\n");
        while (mCursor.moveToNext()) {
            try {
                backupEntry(mContext, mCursor);
                publishProgress(i);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        write("</contacts>\n");
    }

    private void restoreFromXml() {
        int i = 0;
        int count = contactList.getLength();
        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>(10);
        final ContentResolver resolver = mContext.getContentResolver();
        while (i < count) {
            insertOneContact((Element) contactList.item(i), ops);
            if (ops.size() == 0)
                continue;
            try {
                if (i % 50 == 0) {
                    resolver.applyBatch(ContactsContract.AUTHORITY, ops);
                    ops.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress(i);
            i++;
        }
        if (ops.size() != 0) {
            try {
                resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ops.clear();
        }
    }

    private void insertOneContact(Element contactE, ArrayList<ContentProviderOperation> ops) {
        String accoutName = contactE.getAttribute(RawContacts.ACCOUNT_NAME), accoutType = contactE
                        .getAttribute(RawContacts.ACCOUNT_TYPE);
        if (accoutName.equals("")) {
            accoutName = null;
        }
        if (accoutType.equals("")) {
            accoutType = null;
        }
        int rawContactIdIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accoutType)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accoutType)
                        .withValue(RawContacts.STARRED, contactE.getAttribute(RawContacts.STARRED)).build());

        NodeList contactInfoList = contactE.getElementsByTagName("data");
        for (int j = 0, len1 = contactInfoList.getLength(); j < len1; j++) {
            // one data entry
            Builder builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactIdIndex);
            Node dataEntryNode = contactInfoList.item(j);
            if (dataEntryNode.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap attributes = ((Element) dataEntryNode).getAttributes();
                for (int k = 0, len2 = attributes.getLength(); k < len2; k++) {
                    Node attribute = attributes.item(k);
                    builder.withValue(attribute.getNodeName().toString(), attribute.getNodeValue().toString());
                }
            }
            ops.add(builder.build());
        }
    }

    private void backupEntry(Context context, Cursor cursor) {
        long rawContactId = cursor.getLong(0);
        Cursor rawCursor = mContext.getContentResolver().query(RawContacts.CONTENT_URI, RAW_PROJECTION,
                        RawContacts._ID + "=" + rawContactId, null, RawContacts._ID + " asc");
        Cursor nCursor = context.getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI,
                        DATA_PROJECTION, android.provider.ContactsContract.Data.RAW_CONTACT_ID + "=?",
                        new String[] { String.valueOf(rawContactId) }, null);
        if (rawCursor.moveToNext() && nCursor.getCount() > 0) {
            int starred = rawCursor.getInt(3);
            Account[] accounts = AccountManager.get(context).getAccounts();
            if (accounts.length > 0) {
                write("<contact ");
                write(RawContacts.STARRED);
                write("=\"" + starred + "\" ");
                write(RawContacts._ID + "=\"");
                write(String.valueOf(rawContactId));
                write("\"  account_name=\"");
                write(rawCursor.getString(1));
                write("\" account_type=\"");
                write(rawCursor.getString(2) + "\">\n");
            } else {
                write("<contact ");
                write(RawContacts.STARRED);
                write("=\"" + starred + "\" ");
                write(RawContacts._ID + "=\"");
                write(String.valueOf(rawContactId));
                write("\"  account_name=\"\" account_type=\"\">\n");
            }
            while (nCursor.moveToNext()) {
                String mimeType = nCursor.getString(nCursor
                                .getColumnIndex(android.provider.ContactsContract.Data.MIMETYPE));
                backMimeType(nCursor, DATA_PROJECTION, mimeType);
            }
            write("</contact>\n");
        }
        nCursor.close();
    }

    private void backMimeType(Cursor cursor, String[] pro, String mimeType) {
        write("<data " + Data.MIMETYPE + "=\"" + mimeType + "\" ");
        int count = pro.length;
        for (int i = 1; i < count - 1; i++) {
            String dataValue = cursor.getString(i);
            if (dataValue != null) {
                write(cursor.getColumnName(i));
                write("=\"");
                write(dataValue);
                write("\" ");
            }
        }
        write("/>\n");
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

//}
