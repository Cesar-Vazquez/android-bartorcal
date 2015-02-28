package com.cesarvazquez.bartorcal.tools;

import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by cesar on 2/9/14.
 */
public class DataManager {
    public static final DataManager instance = new DataManager();

    public static final String calendar = "calendario";
    public static final String calendar_local = "local";
    public static final String calendar_visitante = "visitante";
    public static final String calendar_fechaHora = "FechaHora";
    public static final String calendar_pista = "pista";
    public static final String calendar_latitud = "Latitud";
    public static final String calendar_longitud = "Longitud";
    public static final String calendar_zoom = "Zoom";

    public static final String players = "plantilla";
    public static final String players_dorsal = "Dorsal";
    public static final String players_nombre = "Nombre";
    public static final String players_nombre_corto = "NombreCorto";
    public static final String players_goles = "Goles";
    public static final String players_amarillas = "Amarillas";
    public static final String players_rojas = "Rojas";

    private String valueFile = "data.json";
    private HashMap<String, ArrayList<HashMap<String, String>>> data = new HashMap<String, ArrayList<HashMap<String, String>>>();

    private FileManager fileManager = FileManager.instance;
    private WebServiceManager webServiceManager = WebServiceManager.instance;

    public ArrayList<HashMap<String, String>> get(String key){
        if (data.containsKey(key))
            return data.get(key);
        else
            return new ArrayList<HashMap<String, String>>();
    }

    public boolean updateFromFile(Context context){
        try {
            data.clear();
            String jsonString = fileManager.readFromFile(context, valueFile);
            return parseJson(jsonString);
        } catch (IOException e){}
        return false;
    }

    public boolean updateFromWeb(Context context){
        try {
            if (webServiceManager.update(WebServiceManager.DATA)){
                String jsonString = webServiceManager.get(WebServiceManager.DATA);
                fileManager.writeFile(context, valueFile, jsonString);
                return parseJson(jsonString);
            }
        } catch (IOException e){}
        return false;
    }

    public synchronized Thread updateFromWebBackground(final Context context, final Handler handler){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager.instance.updateFromWeb(context);
                if (handler != null)
                    handler.sendEmptyMessage(0);
            }
        });
        thread.start();
        return thread;
    }

    private boolean parseJson(String jsonString){
        try {
            JSONObject json = new JSONObject(jsonString);
            Iterator<String> k1s = json.keys();
            while(k1s.hasNext()){
                ArrayList al = new ArrayList();
                String k1 = k1s.next();
                JSONArray v1 = null;
                v1 = json.getJSONArray(k1);
                for (int i1=0; i1<v1.length(); i1++){
                    HashMap<String, String> hm = new HashMap<String, String>();
                    JSONObject json2 = v1.getJSONObject(i1);
                    Iterator<String> k2s = json2.keys();
                    while(k2s.hasNext()){
                        String k2 = k2s.next();
                        hm.put(k2, json2.getString(k2));
                    }
                    al.add(hm);
                }
                data.put(k1, al);
            }
            return true;
        } catch (JSONException e){}
        return false;
    }
}
