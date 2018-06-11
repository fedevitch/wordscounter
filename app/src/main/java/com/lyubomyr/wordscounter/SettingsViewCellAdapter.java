package com.lyubomyr.wordscounter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lyubomyr.wordscounter.Storage.SettingsEntity;

import java.util.List;

public class SettingsViewCellAdapter extends ArrayAdapter<String> {

    private List<SettingsEntity> settings;
    private Context context;
    private String LOG_TAG = "SETTING_ITEM_ADAPTER";

    public SettingsViewCellAdapter(Context context, List<SettingsEntity> settings){
        super(context, R.layout.settings_list_item);
        this.context = context;
        this.settings = settings;
        Log.d(LOG_TAG, "Adapter Started");
        Log.d(LOG_TAG, String.valueOf(settings.size()));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.settings.size();
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        Log.d(LOG_TAG, "getId");
        Log.d(LOG_TAG, String.valueOf(arg0));
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.d(LOG_TAG, "called getView()");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.settings_list_item, parent, false);

        TextView title = (TextView) view.findViewById(R.id.setting_item_title);
        //TextView subTitle = (TextView) view.findViewById(R.id.setting_item_subtitle);

        title.setText(settings.get(position).setting_name);

        Log.d(LOG_TAG, String.valueOf(view.toString()));

        return view;
    }

}
