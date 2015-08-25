
package com.view.special.menu;

import com.view.wheelview.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

public class PureComposerDemoActivity extends Activity {

    private boolean areButtonsShowing;
    private ViewGroup composerButtonsWrapper;
    private View composerButtonsShowHideButtonIcon;
    private View composerButtonsShowHideButton;
    private Animation rotateStoryAddButtonIn;
    private Animation rotateStoryAddButtonOut;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        composerButtonsWrapper = (ViewGroup) findViewById(R.id.composer_buttons_wrapper);
        composerButtonsShowHideButton = findViewById(R.id.composer_buttons_show_hide_button);
        composerButtonsShowHideButtonIcon = findViewById(R.id.composer_buttons_show_hide_button_icon);
        rotateStoryAddButtonIn = AnimationUtils.loadAnimation(this,
                R.anim.rotate_story_add_button_in);
        rotateStoryAddButtonOut = AnimationUtils.loadAnimation(this,
                R.anim.rotate_story_add_button_out);
        //
        composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleComposerButtons();
            }
        });
        for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
            composerButtonsWrapper.getChildAt(i).setOnClickListener(
                    new ComposerLauncher(null, new Runnable() {

                        @Override
                        public void run() {
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(400);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    reshowComposer();
                                }
                            }).start();
                        }
                    }));
        }
        composerButtonsShowHideButton
                .startAnimation(new ComposerButtonGrowAnimationIn(200));
    }

    private void reshowComposer() {
        Animation growIn = new ComposerButtonGrowAnimationIn(300);
        growIn.setInterpolator(new OvershootInterpolator(2.0F));
        this.composerButtonsShowHideButton.startAnimation(growIn);
    }

    private void toggleComposerButtons() {
        if (!areButtonsShowing) {
            ComposerButtonAnimation.startAnimations(
                    this.composerButtonsWrapper, InOutAnimation.Direction.IN);
            this.composerButtonsShowHideButtonIcon
                    .startAnimation(this.rotateStoryAddButtonIn);
        } else {
            ComposerButtonAnimation.startAnimations(
                    this.composerButtonsWrapper, InOutAnimation.Direction.OUT);
            this.composerButtonsShowHideButtonIcon
                    .startAnimation(this.rotateStoryAddButtonOut);
        }
        areButtonsShowing = !areButtonsShowing;
    }

    private class ComposerLauncher implements View.OnClickListener {

        /*public final Runnable DEFAULT_RUN = new Runnable() {

            @Override
            public void run() {
                PureComposerDemoActivity.this
                        .startActivityForResult(
                                new Intent(
                                        PureComposerDemoActivity.this,
                                        PureComposerDemoActivity.ComposerLauncher.this.cls),
                                1);
            }
        };*/
        private final Class<? extends Activity> cls;
        private final Runnable runnable;

        private ComposerLauncher(Class<? extends Activity> c, Runnable runnable) {
            this.cls = c;
            this.runnable = runnable;
        }

        @Override
        public void onClick(View paramView) {
            PureComposerDemoActivity.this.startComposerButtonClickedAnimations(
                    paramView, runnable);
        }
    }

    private void startComposerButtonClickedAnimations(View view,
            final Runnable runnable) {
        this.areButtonsShowing = false;
        Animation shrinkOut1 = new ComposerButtonShrinkAnimationOut(300);
        Animation shrinkOut2 = new ComposerButtonShrinkAnimationOut(300);
        // Animation growOut = new ComposerButtonGrowAnimationOut(300);
        shrinkOut1.setInterpolator(new AnticipateInterpolator(2.0F));
        shrinkOut1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                PureComposerDemoActivity.this.composerButtonsShowHideButtonIcon
                        .clearAnimation();
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        this.composerButtonsShowHideButton.startAnimation(shrinkOut1);
        for (int i = 0; i < this.composerButtonsWrapper.getChildCount(); i++) {
            final View button = this.composerButtonsWrapper.getChildAt(i);
            if (!(button instanceof InOutImageButton))
                continue;
            if (button.getId() != view.getId())
                // 鍏朵粬鎸夐挳缂╁皬娑堝け
                button.setAnimation(shrinkOut2);
            else {
                // 琚偣鍑绘寜閽斁澶ф秷澶� button.startAnimation(growOut);
            }
        }
    }
}
