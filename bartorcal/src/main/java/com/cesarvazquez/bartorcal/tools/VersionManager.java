package com.cesarvazquez.bartorcal.tools;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cesar on 2/26/14.
 */
public class VersionManager {
    private static final String current = "2014-02-27-17-59";
    private static final String calendar = "version";

    public static final VersionManager instance = new VersionManager();

    public synchronized Thread checkVersion(final Context context, final Handler handler){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebServiceManager.instance.update(WebServiceManager.VERSION);
                    String jsonStr = WebServiceManager.instance.get(WebServiceManager.VERSION);
                    JSONObject json = new JSONObject(jsonStr);

                    String remote = json.getString(calendar);
                    int result = (current.equals(remote))? 1 : 0;

                    if (handler != null){
                        Message msg = new Message();
                        msg.arg1 = result;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {}
            }
        });
        thread.start();
        return thread;
    }

    public String getUpdateURL(){
        return WebServiceManager.BASE_URL;
    }
}
