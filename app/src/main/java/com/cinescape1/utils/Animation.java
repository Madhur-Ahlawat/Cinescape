package com.cinescape1.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class Animation implements ViewPager.PageTransformer {
    View  page;
    float pos;
    public Animation(View page, float position){
       this.page = page;
        this.pos = position;
    }
    @Override
    public void transformPage(@NonNull View page, float position) {
        page = this.page;
        position = this.pos;
        page.setTranslationX(-position * page.getWidth());
        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 0) {
            page.setAlpha(1);
            page.setPivotX(0);
            page.setPivotY(0);
            page.setRotationY(90 * Math.abs(position));
        } else if (position <= 1) {
            page.setAlpha(1);
            page.setPivotX(0);
            page.setPivotY(0);
            page.setRotationY(90 * Math.abs(position));
        } else {
            page.setAlpha(0);
        }
    }
}
