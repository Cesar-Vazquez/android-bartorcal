package com.cesarvazquez.bartorcal.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.cesarvazquez.bartorcal.R;
import com.cesarvazquez.bartorcal.tools.DataManager;

/**
 * Created by cesar on 2/26/14.
 */
public class BaseActivityCommonEvents {

    //----------------------------------------------------------------------------------------------

    private int touchActionDownX;
    private int touchActionDownY;
    private boolean touchActionMoveStatus;

    private int touchActionMoveX;
    private int touchActionMoveY;

    private int touchMoveThreshold = 10;

    public boolean onTouchEvent(Activity activity, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchActionDownX = (int)event.getX();
                touchActionDownY = (int)event.getY();
                touchActionMoveStatus = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                touchActionMoveStatus = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if(touchActionMoveStatus) {
                    touchActionMoveX = (int)event.getX();
                    touchActionMoveY = (int)event.getY();

                    if(touchActionMoveX < (touchActionDownX - touchMoveThreshold) && (touchActionMoveY > (touchActionDownY - touchMoveThreshold)) && (touchActionMoveY < (touchActionDownY + touchMoveThreshold))){
                        // Move Left
                        touchActionMoveStatus = false;
                    }
                    else if(touchActionMoveX > (touchActionDownX + touchMoveThreshold) && (touchActionMoveY > (touchActionDownY - touchMoveThreshold)) && (touchActionMoveY < (touchActionDownY + touchMoveThreshold))){
                        // Move Right
                        touchActionMoveStatus = false;
                    }
                    else if(touchActionMoveY < (touchActionDownY - touchMoveThreshold) && (touchActionMoveX > (touchActionDownX - touchMoveThreshold)) && (touchActionMoveX < (touchActionDownX + touchMoveThreshold))){
                        // Move Up
                        touchActionMoveStatus = false;
                    }
                    else if(touchActionMoveY > (touchActionDownY + touchMoveThreshold) && (touchActionMoveX > (touchActionDownX - touchMoveThreshold)) && (touchActionMoveX < (touchActionDownX + touchMoveThreshold))){
                        // Move Down
                        touchActionMoveStatus = false;
                    }
                }
                break;
        }
        return true;
    }
}
