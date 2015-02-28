package com.cesarvazquez.bartorcal.tools;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by cesar on 2/9/14.
 */
public class WebServiceManager {
    public static final WebServiceManager instance = new WebServiceManager();

    public static final String BASE_URL = "http://bar-torcal.rhcloud.com/android";
    public static final String DATA     = "DATA";
    public static final String VERSION  = "VERSION";
    private HashMap<String, URL> urls;
    private HashMap<String, String> values;

    public WebServiceManager(){
        urls = new HashMap<String, URL>();
        values = new HashMap<String, String>();
        try {
            urls.put(DATA,    new URL(BASE_URL + "/ws.php"));
            urls.put(VERSION, new URL(BASE_URL + "/version.php"));
        } catch (MalformedURLException e) {}
    }

    public synchronized boolean update(String key){
        try {
            URLConnection conn = urls.get(key).openConnection();
            StringBuffer out = new StringBuffer();
            InputStream is = conn.getInputStream();
            while (is.available()>0)
                out.append((char)is.read());
            values.put(key, out.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String get(String key){
        return values.get(key);
    }
}
