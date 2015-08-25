
package com.surface;

import com.view.main.ActivityBase;

import android.os.Bundle;
import android.view.KeyEvent;

public class TestSurfaceView3 extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MySurfaceView(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}
