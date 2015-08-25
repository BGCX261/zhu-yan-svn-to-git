
package com.view.draglist;

import com.view.wheelview.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DragListAdapter extends ArrayAdapter<String> {
    public DragListAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            // 加载列表项模板
            view = LayoutInflater.from(getContext()).inflate(R.layout.drag_list_item, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.drag_list_item_text);
        textView.setText(getItem(position));
        return view;
    }
}
