package com.rvtechnologies.grigorahq.complete_profile.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rvtechnologies.grigorahq.R;
import com.rvtechnologies.grigorahq.complete_profile.pojo.PlacePredictions_Pojo;

import java.util.List;


public class AutoCompleteAdapter extends BaseAdapter {
    LayoutInflater lf;
    List<PlacePredictions_Pojo.Predictions> Places;
    private Activity mActivity;

    public AutoCompleteAdapter(List<PlacePredictions_Pojo.Predictions> modelsArrayList, Activity activity) {
        this.Places = modelsArrayList;
        this.mActivity = activity;
        lf = mActivity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return Places.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = lf.inflate(R.layout.autocomplete_row, parent, false);
        TextView main_text = (TextView) v.findViewById(R.id.main_text);
        TextView place_name = (TextView) v.findViewById(R.id.place_name);
        main_text.setText(Places.get(position).getStructured_formatting().getMain_text());
        place_name.setText(Places.get(position).getDescription());

        return v;
    }

}