package com.slidingmenu.example.anim;

import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.view.wheelview.R;

import android.graphics.Canvas;

public class CustomScaleAnimation extends CustomAnimation {

	public CustomScaleAnimation() {
		super(R.string.anim_scale, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);
			}			
		});
	}

}
