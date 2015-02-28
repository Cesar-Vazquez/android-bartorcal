package com.cesarvazquez.bartorcal.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
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
        boolean result = false;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls.get(key).openConnection();
            conn.connect();

            int status = conn.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    values.put(key, sb.toString());
                    result = true;
            }

        } catch (Exception e) {
            result = false;
        }
        finally {
            if(conn != null){
                conn.disconnect();
            }
        }
        return result;
    }

    public String get(String key){
        return values.get(key);
    }
}
