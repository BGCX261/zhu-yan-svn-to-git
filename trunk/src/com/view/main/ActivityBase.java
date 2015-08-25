package com.view.main;

import com.common.util.SystemInfos;

import android.app.Activity;
import android.content.res.Configuration;

public class ActivityBase extends Activity {
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SystemInfos.updateDimension(getResources());
    }

}
