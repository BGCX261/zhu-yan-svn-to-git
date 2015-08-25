
package com.surface;

import com.view.main.ListActivityBase;
import com.view.wheelview.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainSurfaceActivity extends ListActivityBase {

    private String[] functions = {
            "test surface", "test surface2", "test surface3"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ViewAdapter());
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {

                    case 7:
                        intent = new Intent(MainSurfaceActivity.this, TestSurfaceView.class);
                        break;
                    case 8:
                        intent = new Intent(MainSurfaceActivity.this, TestSurfaceView2.class);
                        break;
                    case 9:
                        intent = new Intent(MainSurfaceActivity.this, TestSurfaceView3.class);
                        break;

                    default:
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private class ViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return functions.length;
        }

        @Override
        public Object getItem(int position) {
            return functions[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainSurfaceActivity.this).inflate(
                        R.layout.main_item1,
                        null);
            }
            TextView view = (TextView) convertView.findViewById(R.id.view1);
            view.setText(functions[position]);
            return convertView;
        }

    }
}
