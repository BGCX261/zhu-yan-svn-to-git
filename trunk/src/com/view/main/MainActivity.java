
package com.view.main;

import com.common.util.SystemInfos;
import com.image.cache.CacheActivity;
import com.image.matrix.MatrixActivity;
import com.surface.MainSurfaceActivity;
import com.view.crop.CropActivity;
import com.view.draglist.DragListActivity;
import com.view.flinggallary.FlingGalleryActivity;
import com.view.list.MultiTypeViewListActivity;
import com.view.refereshlist.RefreshActivity;
import com.view.screenscroller.ScreenActivity;
import com.view.screenscroller.VerticalScreenActivity;
import com.view.special.menu.PureComposerDemoActivity;
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

public class MainActivity extends ListActivityBase {

    private String[] functions = {
            "listview中高效显示多种view", "可以向下拉完成刷新的listview",
            "可以水平（竖直）方向循环滚动的gallary（不局限图片），可以设置是配置f", "水平滾動，类似桌面滚动(可以动态添加（删除一屏）)，不能设置适配器",
            "竖直方向上下拖动效果", "memu特效", "拖曳list", "surface",
            "bitmap test", "创建删除快捷方式", "matrix bitmap", "crop image"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ViewAdapter());
        SystemInfos.updateDimension(getResources());
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, MultiTypeViewListActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this,
                                RefreshActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, FlingGalleryActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, ScreenActivity.class);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, VerticalScreenActivity.class);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, PureComposerDemoActivity.class);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, DragListActivity.class);
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this, MainSurfaceActivity.class);
                        break;
                    case 8:
                        intent = new Intent(MainActivity.this, CacheActivity.class);
                        break;
                    case 9:
                        intent = new Intent(MainActivity.this, ShortCutActivity.class);
                        break;
                    case 10:
                        intent = new Intent(MainActivity.this, MatrixActivity.class);
                        break;
                    case 11:
                        intent = new Intent(MainActivity.this, CropActivity.class);
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
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_item1,
                        null);
            }
            TextView view = (TextView) convertView.findViewById(R.id.view1);
            view.setText(functions[position]);
            return convertView;
        }

    }
}
