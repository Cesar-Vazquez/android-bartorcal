package com.cesarvazquez.bartorcal.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cesarvazquez.bartorcal.R;
import com.cesarvazquez.bartorcal.tools.DataManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cesar on 2/9/14.
 */
public class AdapterPlayers extends ArrayAdapter<HashMap<String, String>> {

    private Activity _activity;
    private int resource;
    private int[] colors;

    public AdapterPlayers(Activity context, int resource, List<HashMap<String, String>> items) {
        super(context, resource, items);
        this._activity = context;
        this.resource = resource;

        Resources resources = context.getResources();
        this.colors = new int[]{
            resources.getColor(R.color.Row1),
            resources.getColor(R.color.Row2)
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(resource, null);
        }

        int color = colors[position % colors.length];
        v.setBackgroundColor(color);

        HashMap<String, String> p = getItem(position);
        if (p != null) {
            String dorsal = p.get(DataManager.players_dorsal);
            String nombre = p.get(DataManager.players_nombre);
            String amarillas = p.get(DataManager.players_amarillas);
            String rojas = p.get(DataManager.players_rojas);
            String goles = p.get(DataManager.players_goles);

            TextView viewDorsal = (TextView) v.findViewById(R.id.players_dorsal);
            if (viewDorsal != null) viewDorsal.setText(dorsal);

            TextView viewNombre = (TextView) v.findViewById(R.id.players_nombre);
            if (viewNombre != null) viewNombre.setText(nombre);

            TextView viewAmarillas = (TextView) v.findViewById(R.id.players_amarillas);
            if (viewAmarillas != null) viewAmarillas.setText(amarillas);

            TextView viewRojas = (TextView) v.findViewById(R.id.players_rojas);
            if (viewRojas != null) viewRojas.setText(rojas);

            TextView viewGoles = (TextView) v.findViewById(R.id.players_goles);
            if (viewGoles != null) viewGoles.setText(goles);

        }
        return v;
    }
}
