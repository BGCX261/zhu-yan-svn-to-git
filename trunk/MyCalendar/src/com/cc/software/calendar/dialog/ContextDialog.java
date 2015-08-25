package com.cc.software.calendar.dialog;

import hut.cc.software.calendar.R;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContextDialog extends Dialog implements OnClickListener,
        OnItemClickListener {
    private Context context;
    private ContextDialogListener listener;
    private ArrayList<Integer> tagList;
    private ArrayList<Object> tagNotes;
    private ArrayList<String> items;
    private ListView mList;

    public ContextDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        tagList = new ArrayList<Integer>(10);
        items = new ArrayList<String>(10);
        tagNotes = new ArrayList<Object>(10);
        //        this.setCanceledOnTouchOutside(true);
        this.setContentView(R.layout.custom_context_dialog);
        mList = (ListView) this.findViewById(R.id.list);
    }

    public ContextDialog addDialogEntry(String txt, int tag, Object tagNote) {
        items.add(txt);
        tagList.add(tag);
        tagNotes.add(tagNote);
        return this;
    }

    public ContextDialog addDialogEntry(int resourceIdAndtag, Object tagNote) {
        return addDialogEntry(context.getResources()
                .getString(resourceIdAndtag), resourceIdAndtag, tagNote);
    }

    public ContextDialog setOnItemSelectListener(ContextDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public void setTitle(String title) {
        if (title != null) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }
    }

    @Override
    public void show() {
        if (tagList.size() != 0) {
            ItemAdater adapter = new ItemAdater();
            mList.setAdapter(adapter);
            mList.setOnItemClickListener(this);
        }
        super.show();
    }

    public void dismissDialog() {
        this.dismiss();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Object tagNote = tagNotes.get(which);
        dialog.dismiss();
        int tag = tagList.get(which);
        if (listener != null) {
            if (listener.onDialogItemClicked(tag, tagNote) == true) {
                return;
            }
        }
    }

    private class ItemAdater extends BaseAdapter {

        @Override
        public int getCount() {
            if (items != null)
                return items.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (items != null)
                return items.get(position);
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.custom_context_dialog_item, null);
            }
            ((TextView) convertView.findViewById(R.id.title)).setText(getItem(
                    position).toString());
            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Object tagNote = tagNotes.get(position);
        int tag = tagList.get(position);
        if (listener != null) {
            dismissDialog();
            if (listener.onDialogItemClicked(tag, tagNote) == true) {
                return;
            }
        } else {
            dismissDialog();
        }
    }
}
