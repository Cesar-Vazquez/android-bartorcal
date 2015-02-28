package com.cesarvazquez.bartorcal.helpers;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.cesarvazquez.bartorcal.Main;
import com.cesarvazquez.bartorcal.R;
import com.cesarvazquez.bartorcal.tools.DataManager;

/**
 * Created by cesar on 2/22/14.
 */
public abstract class BaseActivity extends Activity {

    private BaseActivityCommonEvents baseActivityCommonEvents;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!this.getClass().equals(Main.class)){
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        baseActivityCommonEvents = new BaseActivityCommonEvents();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update:
                final ProgressDialog dialogUpdating = new ProgressDialog(this);
                dialogUpdating.setMessage(getResources().getString(R.string.menu_update_message));
                dialogUpdating.show();

                Handler handlerUpdate = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        dialogUpdating.dismiss();
                        updateDataHandler(msg);
                    }
                };

                DataManager.instance.updateFromWebBackground(this, handlerUpdate);
                return true;
            default:
                // Action bar back button
                finish();
                return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return baseActivityCommonEvents.onTouchEvent(this, event);
    }

    protected void updateDataHandler(Message msg){
    }
}
