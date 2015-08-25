
package com.image.matrix;

import com.view.main.ActivityBase;
import com.view.wheelview.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ColorMatrixActivity extends ActivityBase {

    private Button change;
    private EditText[] et = new EditText[20];
    private float[] carray = new float[20];
    private MyImage sv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_matrix);

        change = (Button) findViewById(R.id.change);
        sv = (MyImage) findViewById(R.id.MyImage);

        for (int i = 0; i < 20; i++) {

            et[i] = (EditText) findViewById(R.id.indexa0 + i);
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
        for (int i = 0; i < 20; i++) {

            carray[i] = Float.valueOf(et[i].getText().toString());
        }

    }

}
