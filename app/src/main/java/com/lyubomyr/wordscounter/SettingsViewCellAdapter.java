package com.lyubomyr.wordscounter;

import android.content.Context;
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

    public SettingsViewCellAdapter(Context context, List<SettingsEntity> settings){
        super(context, R.layout.settings_list_item);
        this.context = context;
        this.settings = settings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.settings_list_item, parent, false);

        TextView title = (TextView) view.findViewById(R.id.setting_item_title);
        TextView subTitle = (TextView) view.findViewById(R.id.setting_item_subtitle);

        title.setText(settings.get(position).setting_name);

        return view;
    }

}
