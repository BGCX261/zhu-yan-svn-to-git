
package com.view.refereshlist.copy;

import com.view.refereshlist.copy.PullToRefreshListView.OnRefreshListener;
import com.view.wheelview.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends Activity implements OnRefreshListener {
    private ItemAdapter mAdapter;
    PullToRefreshListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_list);
        mListView = (PullToRefreshListView) findViewById(R.id.refresh_list);
        mListView.setOnRefreshListener(this);
        mAdapter = new ItemAdapter();
        mListView.setAdapter(mAdapter);
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
            System.out.println("getCount");
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
                convertView = LayoutInflater.from(RefreshActivity.this).inflate(
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
    public void onRefresh() {
        /*
         * new AsyncTask<Void, Void, Void>() {
         * @Override protected Void doInBackground(Void... params) { try {
         * Thread.sleep(1000); } catch (InterruptedException e) {
         * e.printStackTrace(); } return null; }
         * @Override protected void onPostExecute(Void result) {
         * super.onPostExecute(result); mAdapter.addItem();
         * mListView.onRefreshComplete(); } }.execute();
         */
        mListView.onRefreshComplete();
    }
}
