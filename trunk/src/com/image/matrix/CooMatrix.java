
package com.image.matrix;

import com.view.wheelview.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CooMatrix extends Activity {

    private Button change;
    private EditText[] et = new EditText[9];
    private float[] carray = new float[9];
    private MyImage1 sv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coo_matrix);

        change = (Button) findViewById(R.id.change);
        sv = (MyImage1) findViewById(R.id.MyImage);

        for (int i = 0; i < 9; i++) {

            et[i] = (EditText) findViewById(R.id.indexa10 + i);
            carray[i] = Float.valueOf(et[i].getText().toString());

        }

        change.setOnClickListener(l);

    }

    private Button.OnClickListener l = new Button.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub  
            getValues();
            sv.setValues(carray);
            sv.invalidate();
        }

    };

    public void getValues() {
        for (int i = 0; i < 9; i++) {

            carray[i] = Float.valueOf(et[i].getText().toString());
        }

    }
}
