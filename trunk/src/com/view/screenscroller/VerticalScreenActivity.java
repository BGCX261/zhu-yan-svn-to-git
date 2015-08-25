
package com.view.screenscroller;

import com.view.wheelview.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VerticalScreenActivity extends Activity implements OnScrollListener {
    int[] resIds = {
            R.drawable.bg_0, R.drawable.bg_1, R.drawable.bg_2, R.drawable.bg_3, R.drawable.bg_4
    };

    private VerticalScreenScroller screenScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_screen_main);
        LayoutInflater inflater = LayoutInflater.from(this);
        screenScroller = (VerticalScreenScroller) findViewById(R.id.screen_scroller);
        View view = inflater.inflate(R.layout.vertical_screen_main_item, null);
        ListView list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(new ItemAdapter());
        screenScroller.addView(view);
        list.setOnScrollListener(this);
    }

    private int count = 0;

    public class ItemAdapter extends BaseAdapter {
        private List<String> items = new ArrayList<String>();

        public ItemAdapter() {
            for (int i = 0; i < 30; i++) {
                items.add("item" + i);
            }
        }

        public void addItem() {
            items.add(0, "newItem" + count);
            count++;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int arg0) {
            return items.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(VerticalScreenActivity.this).inflate(
                        R.layout.list_item1, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.view1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(items.get(position));
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView1;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        if (totalItemCount <= firstVisibleItem + visibleItemCount) {
            mListener.setBottom(true);
            screenScroller.setScrollable(true);
        } else if (firstVisibleItem == 0) {
            mListener.setTop(true);
            screenScroller.setScrollable(true);
        } else {
            mListener.setBottom(false);
            mListener.setTop(false);
            screenScroller.setScrollable(false);
        }
    }

    private TopBottomListener mListener;

    public void setTopBottomListener(TopBottomListener listener) {
        mListener = listener;
    }

    public interface TopBottomListener {

        void setTop(boolean top);

        void setBottom(boolean bottom);
    }

}
