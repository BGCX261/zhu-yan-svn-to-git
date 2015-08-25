
package com.view.screenscroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.surface.MySurfaceView;
import com.view.wheelview.R;

public class ScreenActivity extends Activity {
    int[] resIds = {
            R.drawable.bg_0, R.drawable.bg_1, R.drawable.bg_2, R.drawable.bg_3, R.drawable.bg_4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        LayoutInflater inflater = LayoutInflater.from(this);
        final ScreenScroller screenScroller = (ScreenScroller) findViewById(R.id.screen_scroller);
        for (int i = 0; i < 5; i++) {
            View view = inflater.inflate(R.layout.screen_main_item, null);
            ImageView img = (ImageView) view.findViewById(R.id.img);
            Button btn = (Button) view.findViewById(R.id.btn);
            screenScroller.addView(view, i);
            btn.setText("item" + i);
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    View view = (View) v.getParent();
                    screenScroller.removeView(view);
                }
            });
            img.setImageResource(resIds[i]);
        }
        screenScroller.addView(new MySurfaceView(this));
    }
}
