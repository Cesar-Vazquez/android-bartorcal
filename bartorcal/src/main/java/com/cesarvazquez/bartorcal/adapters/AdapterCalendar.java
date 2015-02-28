package com.cesarvazquez.bartorcal.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cesarvazquez.bartorcal.R;
import com.cesarvazquez.bartorcal.helpers.ButtonClickListener;
import com.cesarvazquez.bartorcal.tools.DataManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by cesar on 2/9/14.
 */
public class AdapterCalendar extends ArrayAdapter<HashMap<String,String>> {

    private Activity _activity;
    private int resource;
    private int[] colors;

    public AdapterCalendar(Activity context, int resource, List<HashMap<String,String>> items) {
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

        HashMap<String,String> p = getItem(position);
        if (p != null) {
            String local     = p.get(DataManager.calendar_local);
            String visitante = p.get(DataManager.calendar_visitante);
            String fechaHora = p.get(DataManager.calendar_fechaHora);
            String pista     = p.get(DataManager.calendar_pista);
            String latitude  = p.get(DataManager.calendar_latitud);
            String longitude = p.get(DataManager.calendar_longitud);
            String zoom      = p.get(DataManager.calendar_zoom);
            String uri       = "geo:?q=" + Uri.encode(latitude + "," + longitude + "(" + pista + ")") + "&z=" + zoom;

            TextView viewLocal = (TextView) v.findViewById(R.id.calendar_local);
            if (viewLocal != null)  viewLocal.setText(local);

            TextView viewVisitante = (TextView) v.findViewById(R.id.calendar_visitante);
            if (viewVisitante != null)  viewVisitante.setText(visitante);

            TextView viewFechaHora = (TextView) v.findViewById(R.id.calendar_fechahora);
            if (viewFechaHora != null)  viewFechaHora.setText(fechaHora);

            TextView viewPista = (TextView) v.findViewById(R.id.calendar_pista);
            if (viewPista != null)  viewPista.setText(pista);

            ImageView pinButton = (ImageView) v.findViewById(R.id.calendar_pin);
            if (pinButton != null){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                pinButton.setOnClickListener(new ButtonClickListener(_activity, intent));
            }
        }
        return v;
    }
}
