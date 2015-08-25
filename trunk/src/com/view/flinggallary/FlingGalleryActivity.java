package com.view.flinggallary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.view.wheelview.R;

public class FlingGalleryActivity extends Activity {
    private final String[] mLabelArray = { "View1", "View2", "View3", "View4" };

    private FlingGallery mGallery;
    private CheckBox mCheckBox;
    private TextView t1;

    // Note: The following handler is critical to correct function of   
    // the FlingGallery class. This enables the FlingGallery class to   
    // detect when the motion event has ended by finger being lifted   

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGallery.onGalleryTouchEvent(event);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGallery = new FlingGallery(this);
        mGallery.setPaddingWidth(5);
        mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,
                        mLabelArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return new GalleryViewItem(getApplicationContext(), position);
            }
        });

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        //layoutParams.setMargins(10, 10, 10, 10);   
        layoutParams.weight = 1.0f;

        layout.addView(mGallery, layoutParams);

        mCheckBox = new CheckBox(getApplicationContext());
        mCheckBox.setText("Gallery is Circular");
        mCheckBox.setPadding(50, 10, 0, 10);
        mCheckBox.setTextSize(30);
        mCheckBox.setChecked(true);
        mCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mGallery.setIsGalleryCircular(mCheckBox.isChecked());
            }
        });

        layout.addView(mCheckBox, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mCheckBox.setBackgroundColor(Color.BLACK);
        setContentView(layout);

    }

    private class GalleryViewItem extends TableLayout {
        public GalleryViewItem(Context context, int position) {
            super(context);

            this.setOrientation(LinearLayout.VERTICAL);
            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

            if (position == 0) {
                View view = (View) getLayoutInflater().inflate(R.layout.gallary_item1, null);
                //给里面控件设置事件监听   
                t1 = (TextView) view.findViewById(R.id.main_view01);
                t1.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), t1.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                this.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
            } else if (position == 1) {
                View view = (View) getLayoutInflater().inflate(R.layout.gallary_item1, null);
                this.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
            } else if (position == 2) {
                View view = (View) getLayoutInflater().inflate(R.layout.gallary_item1, null);
                this.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
            } else if (position == 3) {
                View view = (View) getLayoutInflater().inflate(R.layout.gallary_item1, null);
                this.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }
    }
}
