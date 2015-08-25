
package com.view.draglist;

import com.view.main.ActivityBase;
import com.view.wheelview.R;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class DragListActivity extends ActivityBase {
    // 数据列表
    private List<String> list = null;

    // 数据适配器
    private DragListAdapter adapter = null;

    // 存放分组标签
    public static List<String> groupKey = new ArrayList<String>();
    // 分组一
    private List<String> navList = new ArrayList<String>();
    // 分组二
    private List<String> moreList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_list);

        // 初始化样本数据
        initData();

        // 后面会介绍DragListView
        DragListView dragListView = (DragListView) findViewById(R.id.drag_list);
        adapter = new DragListAdapter(this, list);
        dragListView.setAdapter(adapter);
    }

    public void initData() {
        // 数据结果
        list = new ArrayList<String>();

        // groupKey存放的是分组标签
        groupKey.add("A组");
        groupKey.add("B组");

        for (int i = 0; i < 5; i++) {
            navList.add("A选项" + i);
        }
        list.add("A组");
        list.addAll(navList);

        for (int i = 0; i < 8; i++) {
            moreList.add("B选项" + i);
        }
        list.add("B组");
        list.addAll(moreList);
    }
}
