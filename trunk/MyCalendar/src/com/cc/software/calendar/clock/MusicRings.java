package com.cc.software.calendar.clock;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

public class MusicRings extends Activity {
    static final int REQUEST_SOUND = 1;
    static final int REQUEST_ORIGINAL = 2;
    static final int RESULT_CANCEL = 3;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent musicPickerIntent = new Intent(this, MusicPicker.class);
        musicPickerIntent.putExtras(getIntent());
        musicPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(musicPickerIntent, REQUEST_SOUND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SOUND && resultCode == RESULT_OK) {
            Intent resultIntent = new Intent();
            Uri uri = data != null ? data.getData() : null;
            resultIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, uri);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else if (requestCode == REQUEST_ORIGINAL && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }else if(requestCode == REQUEST_SOUND && resultCode == RESULT_CANCEL){
            setResult(RESULT_CANCEL, null);
            finish();
        }
    }
}
