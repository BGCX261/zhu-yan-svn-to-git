
package com.view.main;

import com.tool.util.ShortCutUtil;
import com.view.wheelview.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ShortCutActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_cut);
        findViewById(R.id.create_short_cut).setOnClickListener(this);
        findViewById(R.id.del_short_cut).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_short_cut:
                ShortCutUtil.addShortCut(this);
                break;
            case R.id.del_short_cut:
                ShortCutUtil.delShortcut(this);
                break;
            default:
                break;
        }
    }
}
