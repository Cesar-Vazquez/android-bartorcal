package com.cesarvazquez.bartorcal;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.cesarvazquez.bartorcal.adapters.AdapterCalendar;
import com.cesarvazquez.bartorcal.helpers.BaseListActivity;
import com.cesarvazquez.bartorcal.tools.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Calendar extends BaseListActivity {
    private DataManager dataManager = DataManager.instance;
    private ArrayList<HashMap<String,String>> data;
    private AdapterCalendar adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        data = dataManager.get(DataManager.calendar);
        adapter = new AdapterCalendar(this, R.layout.calendar_hole, data);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    protected void updateDataHandler(Message msg) {
        finish();
        startActivity(getIntent());
    }
}
