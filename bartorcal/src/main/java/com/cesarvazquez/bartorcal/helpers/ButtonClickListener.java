package com.cesarvazquez.bartorcal.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cesarvazquez.bartorcal.R;

/**
 * Created by cesar on 2/22/14.
 */
public class ButtonClickListener implements View.OnClickListener {
    private Animation _animation;
    private Intent _intent;
    private Activity _context;
    private Handler _handler;

    public ButtonClickListener(Activity ctx, Intent i){
        _animation = AnimationUtils.loadAnimation(ctx, R.anim.image_click);
        _intent = i;
        _context = ctx;
        _handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                _context.startActivityForResult(_intent, 0);
            }
        };
    }

    @Override
    public void onClick(View view) {

        _animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                _handler.sendEmptyMessage(0);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(_animation);
    }
}
