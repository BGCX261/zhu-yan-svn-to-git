package com.view.list;

import com.view.wheelview.R;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MultipleItemsList extends ListActivity {
    private MyCustomAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyCustomAdapter();
        for (int i = 1; i < 50; i++) {
            mAdapter.addItem("item " + i);
            if (i % 4 == 0) {
                mAdapter.addSeparatorItem("separator " + i);
            }
        }
        setListAdapter(mAdapter);
    }

    private class MyCustomAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public void addSeparatorItem(final String item) {
            mData.add(item);
            // save separator position
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position % 5 == 0) {
                return TYPE_ITEM;
            } else {
                return TYPE_SEPARATOR;
            }
            //return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.list_item1, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.view1);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.list_item2, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.view1);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.view2);
                    break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(mData.get(position));
            if (type == TYPE_SEPARATOR) {
                holder.textView2.setText("item 2");
            }
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
        public TextView textView2;
    }

}
