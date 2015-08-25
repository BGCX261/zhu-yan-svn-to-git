
package com.view.main;

import com.common.util.SystemInfos;

import android.app.ListActivity;
import android.content.res.Configuration;

public class ListActivityBase extends ListActivity {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SystemInfos.updateDimension(getResources());
    }
}
