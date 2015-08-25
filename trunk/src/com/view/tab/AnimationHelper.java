package com.view.tab;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {

    public static final int BOTTOM_MENU_ANIMATION_DURATION = 200;

    private static TranslateAnimation mTranslateRightInAnimation;
    private static TranslateAnimation mTranslateRightOutAnimation;
    private static TranslateAnimation mTranslateInAnimation;
    private static TranslateAnimation mTranslateOutAnimation;
    private static TranslateAnimation mTranslateLeftInAnimation;
    private static TranslateAnimation mTranslateLeftOutAnimation;
    private static TranslateAnimation mTranslateTopOutAnimation;
    private static TranslateAnimation mTranslateTopInAnimation;
    private static AlphaAnimation mAlphaFadeInAnimation;
    private static AlphaAnimation mAlphaFadeOutAnimation;

    private static AccelerateDecelerateInterpolator mInterpolator;

    static {
        mTranslateTopOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF,
                        0f);

        mTranslateTopInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f);

        mTranslateRightInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f);
        mTranslateRightOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f);
        mTranslateRightInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f);
        mTranslateRightOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f);

        mTranslateLeftInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f);

        mTranslateLeftOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0f);

        mTranslateInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        mTranslateOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);

        mAlphaFadeInAnimation = new AlphaAnimation(1f, 0f);
        mAlphaFadeOutAnimation = new AlphaAnimation(0f, 1f);
        mInterpolator = new AccelerateDecelerateInterpolator();
        mAlphaFadeInAnimation.setDuration(BOTTOM_MENU_ANIMATION_DURATION);
        mAlphaFadeOutAnimation.setDuration(BOTTOM_MENU_ANIMATION_DURATION);

    }

    public static Animation animationComeOutOrBackHorizontal(boolean isAppear, boolean isLeft) {
        AnimationSet set = new AnimationSet(true);
        if (!isAppear) {
            if (isLeft)
                set.addAnimation(mTranslateLeftInAnimation);
            else
                set.addAnimation(mTranslateRightInAnimation);
            set.addAnimation(mAlphaFadeInAnimation);
        } else {
            if (isLeft)
                set.addAnimation(mTranslateLeftOutAnimation);
            else
                set.addAnimation(mTranslateRightOutAnimation);
            set.addAnimation(mAlphaFadeOutAnimation);
        }
        set.setDuration(BOTTOM_MENU_ANIMATION_DURATION);
        set.setInterpolator(mInterpolator);
        return set;
    }

    public static Animation animationComeOutOrIn(boolean isAppear) {
        AnimationSet set = new AnimationSet(true);
        if (!isAppear) {
            set.addAnimation(mTranslateInAnimation);
            set.addAnimation(mAlphaFadeInAnimation);
        } else {
            set.addAnimation(mTranslateOutAnimation);
            set.addAnimation(mAlphaFadeOutAnimation);
        }
        set.setDuration(BOTTOM_MENU_ANIMATION_DURATION);
        set.setInterpolator(mInterpolator);
        return set;
    }

    public static Animation animationComeOutOrInFromTop(boolean isAppear) {
        AnimationSet set = new AnimationSet(true);
        if (!isAppear) {
            set.addAnimation(mTranslateTopInAnimation);
        } else {
            set.addAnimation(mTranslateTopOutAnimation);
        }
        set.setDuration(BOTTOM_MENU_ANIMATION_DURATION);
        set.setInterpolator(mInterpolator);
        return set;
    }

    public static Animation getTabShowAnimation(final View currentTab, long animation_duration) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(animation_duration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                currentTab.setVisibility(View.VISIBLE);
                currentTab.clearAnimation();
            }
        });
        return animation;
    }

}
