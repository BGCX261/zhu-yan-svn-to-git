
package com.view.list;

import com.notification.test.RemindNotifictionHelper;
import com.view.wheelview.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeViewListActivity extends Activity {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
    public static final String COLOR_DATA[] = new String[] {
            "#adb2b8", "#f3aa38", "#ff93bb", "#85c23d", "#19c5f4", "#358cda", "#3bbb4b", "#db5a32",
            "#9f66cc", "#111213"
    };
    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list);
        ClassifyAdapter adapter = new ClassifyAdapter();
        RemindNotifictionHelper.getInstance().cancelNotifiction();
        listView.setAdapter(adapter);
        final View view = findViewById(R.id.change_color);
        view.setBackgroundColor(Color.parseColor(COLOR_DATA[current]));
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                current++;
                if (current + 1 > COLOR_DATA.length) {
                    current = 0;
                }
                view.setBackgroundColor(Color.parseColor(COLOR_DATA[current]));
            }
        });
    }

    public class ClassifyAdapter extends BaseAdapter {
        private List<Integer> items = new ArrayList<Integer>();

        public ClassifyAdapter() {
            for (int i = 0; i < 30; i++) {
                items.add(i);
            }
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
            int type = getItemViewType(position);
            System.out.println("position=" + position + "    type=" + type);
            if (convertView == null) {
                System.out.println("arg1 == null");
                holder = new ViewHolder();
                switch (type) {
                    case TYPE_SEPARATOR:
                        convertView = LayoutInflater.from(MultiTypeViewListActivity.this)
                                .inflate(R.layout.list_item1, null);
                        holder.textView1 = (TextView) convertView.findViewById(R.id.view1);

                        break;
                    case TYPE_ITEM:
                        convertView = LayoutInflater.from(MultiTypeViewListActivity.this)
                                .inflate(R.layout.list_item2, null);
                        holder.textView1 = (TextView) convertView.findViewById(R.id.view1);
                        holder.textView2 = (TextView) convertView.findViewById(R.id.view2);
                        break;
                }
                convertView.setTag(holder);
            } else {
                System.out.println("arg1 is not null");
                holder = (ViewHolder) convertView.getTag();
            }
            if (type == TYPE_SEPARATOR) {
                holder.textView1.setText("itemParent" + position);
            } else {
                holder.textView1.setText("itemChild0" + position);
                holder.textView2.setText("itemChild1" + position);
            }
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            if (position % 5 == 0) {
                return TYPE_SEPARATOR;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

    }

    public static class ViewHolder {
        public TextView textView1;
        public TextView textView2;
    }

}
