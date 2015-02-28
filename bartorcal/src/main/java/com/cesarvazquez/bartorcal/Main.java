package com.cesarvazquez.bartorcal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;

import com.cesarvazquez.bartorcal.helpers.BaseActivity;
import com.cesarvazquez.bartorcal.helpers.ButtonClickListener;
import com.cesarvazquez.bartorcal.tools.DataManager;
import com.cesarvazquez.bartorcal.tools.VersionManager;

/**
 * Created by cesar on 2/9/14.
 */
@SuppressWarnings("rawtypes")
public class Main extends BaseActivity {

    private static final SparseArray<Class> buttons = new SparseArray<Class>();
    static
    {
        buttons.put(R.id.button_calendar, Calendar.class);
    	buttons.put(R.id.button_cards   , Cards.class);
    	buttons.put(R.id.button_goals   , Goals.class);
    	buttons.put(R.id.button_players , Players.class);
    }

    private DataManager dataManager = DataManager.instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        for(int i = 0; i < buttons.size(); i++) {
        	int key = buttons.keyAt(i);
        	final Class value = buttons.get(key);

            View view = findViewById(key);
            view.setOnClickListener(new ButtonClickListener(this, new Intent(this, value)));
        }

        dataManager.updateFromFile(this);
        dataManager.updateFromWebBackground(this, null);

        checkVersion();
    }

    private void checkVersion() {
        VersionManager.instance.checkVersion(this, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                    builder.setMessage(getResources().getString(R.string.main_update_message));
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String url = VersionManager.instance.getUpdateURL();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
