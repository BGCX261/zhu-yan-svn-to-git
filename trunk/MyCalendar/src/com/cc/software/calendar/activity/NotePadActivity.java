package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.DialogUtil;
import com.cc.software.calendar.util.FileUtil;
import com.cc.software.calendar.util.NoteManager;

public class NotePadActivity extends Activity implements OnClickListener {

    private TextView btnDelete, btnSave;
    private EditText mTitleTxt, mContentTxt;
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notepad);
        btnDelete = (TextView) findViewById(R.id.note_delete);
        btnSave = (TextView) findViewById(R.id.note_save);
        mTitleTxt = (EditText) findViewById(R.id.title_txt);
        mContentTxt = (EditText) findViewById(R.id.content_txt);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        id = getIntent().getIntExtra("id", -1);
        if (title != null) {
            mTitleTxt.setText(title);
        }
        if (content != null) {
            mContentTxt.setText(content);
        }
        btnDelete.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.note_delete:
            DialogUtil.createConfirmDeleteDialog(this, confirmClickListener).show();
            break;
        case R.id.note_save:
            final String title = mTitleTxt.getText().toString().trim();
            final String content = mContentTxt.getText().toString().trim();
            final String date = getIntent().getExtras().getString("date");
            if (title.equals("")) {
                CommonUtil.showToastMessage(R.string.title_not_can_not_null, this);
                return;
            }
            if (content.equals("")) {
                CommonUtil.showToastMessage(R.string.content_not_can_not_null, this);
                return;
            }
            if (id == -1) {
                NoteManager.addNote(this, title, content,
                                FileUtil.getFilePathByType(NotePadActivity.this, title, Constants.NOTE) + ".txt",
                                Constants.NOTE, date);

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(new File(FileUtil.getFilePathByType(NotePadActivity.this, title
                                            + ".txt", Constants.NOTE)));
                            out.write(content.getBytes());
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            } else {
                NoteManager.updateNote(this, id, title, content);
            }

            this.finish();
            break;

        default:
            break;
        }
    }

    private DialogInterface.OnClickListener confirmClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
            case Dialog.BUTTON1:
                finish();
                break;
            case Dialog.BUTTON2:
                break;
            default:
                break;
            }
            dialog.dismiss();
        }
    };

}
