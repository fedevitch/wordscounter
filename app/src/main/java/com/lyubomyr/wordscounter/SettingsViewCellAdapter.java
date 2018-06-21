package com.lyubomyr.wordscounter;

import android.content.Context;
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

    private String title;
    private String subTitle;

    public SettingsViewCellAdapter(Context context, List<SettingsEntity> settings){
        super(context, R.layout.settings_list_item_simple);
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


        SettingsEntity setting = settings.get(position);


        Log.d(LOG_TAG, setting.setting_type);



        if(setting.setting_type.equals(Settings.Types._switch.toString())){
            return renderSwitch(inflater, parent, setting);
        } else if(setting.setting_type.equals(Settings.Types.checkbox.toString())){
            return renderCheckbox(inflater, parent, setting);
        } else if(setting.setting_type.equals(Settings.Types.num_picker.toString())){
            return renderModal(inflater, parent, setting);
        } else if(setting.setting_type.equals((Settings.Types.text_modal.toString()))){
            return renderModal(inflater, parent, setting);
        }

        return renderSimpleListItem(inflater, parent, setting);

    }

    private String getTitle(SettingsEntity setting){
        if(setting.setting_name.equals(Settings.Names.num_of_records.toString())) {
            return this.context.getResources().getString(R.string.settings_num_of_records_title);
        } else if(setting.setting_name.equals(Settings.Names.save_results.toString())) {
            return this.context.getResources().getString(R.string.settings_save_records_title);
        } else if(setting.setting_name.equals(Settings.Names.ignored_symbols.toString())) {
            return this.context.getResources().getString(R.string.settings_ignored_symbols_title);
        } else if(setting.setting_name.equals(Settings.Names.display_table.toString())) {
            return this.context.getResources().getString(R.string.settings_display_table_title);
        } else if(setting.setting_name.equals(Settings.Names.display_chart.toString())) {
            return this.context.getResources().getString(R.string.settings_display_chart_title);
        } else {
            return setting.setting_name;
        }
    }

    private String getSubtitle(SettingsEntity setting){
        if(setting.setting_name.equals(Settings.Names.num_of_records.toString())) {
            return this.context.getResources().getString(R.string.settings_num_of_records_hint);
        } else if(setting.setting_name.equals(Settings.Names.save_results.toString())) {
            return this.context.getResources().getString(R.string.settings_save_records_hint);
        } else if(setting.setting_name.equals(Settings.Names.ignored_symbols.toString())) {
            return this.context.getResources().getString(R.string.settings_ignored_symbols_hint);
        } else if(setting.setting_name.equals(Settings.Names.display_table.toString())) {
            return this.context.getResources().getString(R.string.settings_display_table_hint);
        } else if(setting.setting_name.equals(Settings.Names.display_chart.toString())) {
            return this.context.getResources().getString(R.string.settings_display_chart_hint);
        } else {
            return "";
        }
    }

    private View renderSimpleListItem(LayoutInflater inflater, ViewGroup parent, SettingsEntity setting){
        View view = inflater.inflate(R.layout.settings_list_item_simple, parent, false);

        TextView title = view.findViewById(R.id.setting_list_item_simple_title);
        TextView subTitle = view.findViewById(R.id.setting_list_item_simple_subtitle);

        title.setText(this.getTitle(setting));
        subTitle.setText(this.getSubtitle(setting));

        return view;
    }

    private View renderCheckbox(LayoutInflater inflater, ViewGroup parent, SettingsEntity setting){
        View view = inflater.inflate(R.layout.settings_list_item_checkbox, parent, false);

        TextView title = view.findViewById(R.id.setting_list_item_checkbox_title);
        TextView subTitle = view.findViewById(R.id.setting_list_item_checkbox_subtitle);

        title.setText(this.getTitle(setting));
        subTitle.setText(this.getSubtitle(setting));

        return view;
    }

    private View renderSwitch(LayoutInflater inflater, ViewGroup parent, SettingsEntity setting){
        View view = inflater.inflate(R.layout.settings_list_item_switch, parent, false);

        TextView title = view.findViewById(R.id.setting_list_item_switch_title);
        TextView subTitle = view.findViewById(R.id.setting_list_item_switch_subtitle);

        title.setText(this.getTitle(setting));
        subTitle.setText(this.getSubtitle(setting));

        return view;
    }

    private View renderModal(LayoutInflater inflater, ViewGroup parent, SettingsEntity setting){
        View view = inflater.inflate(R.layout.settings_list_item_simple, parent, false);

        TextView title = view.findViewById(R.id.setting_list_item_simple_title);
        TextView subTitle = view.findViewById(R.id.setting_list_item_simple_subtitle);

        title.setText(this.getTitle(setting));
        subTitle.setText(this.getSubtitle(setting));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "opening modal here");
            }
        });

        return view;
    }

}
