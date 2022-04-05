package com.lyubomyr.wordscounter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.lyubomyr.wordscounter.Storage.SettingsEntity;

import java.util.List;

public class SettingsViewCellAdapter extends ArrayAdapter {

    private final List<SettingsEntity> settings;
    private final Context context;
    private final String LOG_TAG = "SETTING_ITEM_ADAPTER";


    public SettingsViewCellAdapter(Context context, List<SettingsEntity> settings){
        super(context, R.layout.settings_list_item_simple);
        this.context = context;
        this.settings = settings;
        Log.d(LOG_TAG, "Adapter Started");
        Log.d(LOG_TAG, String.valueOf(settings.size()));
    }

    @Override
    public int getCount() {
        return this.settings.size();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SettingsEntity setting = settings.get(position);

        assert inflater != null;
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

    private View renderCheckbox(LayoutInflater inflater, ViewGroup parent, final SettingsEntity setting){
        View view = inflater.inflate(R.layout.settings_list_item_checkbox, parent, false);

        TextView title = view.findViewById(R.id.setting_list_item_checkbox_title);
        TextView subTitle = view.findViewById(R.id.setting_list_item_checkbox_subtitle);
        final CheckBox checkBox = view.findViewById(R.id.list_item_checkBox);

        title.setText(this.getTitle(setting));
        subTitle.setText(this.getSubtitle(setting));
        checkBox.setChecked(setting.setting_value.equals("true"));

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.setting_value = String.valueOf(checkBox.isChecked());
                onChangeSetting(setting);
            }
        });

        return view;
    }

    private View renderSwitch(LayoutInflater inflater, ViewGroup parent, final SettingsEntity setting){
        View view = inflater.inflate(R.layout.settings_list_item_switch, parent, false);

        TextView title = view.findViewById(R.id.setting_list_item_switch_title);
        TextView subTitle = view.findViewById(R.id.setting_list_item_switch_subtitle);
        final Switch _switch = view.findViewById(R.id.list_item_switch);

        title.setText(this.getTitle(setting));
        subTitle.setText(this.getSubtitle(setting));
        _switch.setChecked(setting.setting_value.equals("true"));

        _switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.setting_value = String.valueOf(_switch.isChecked());
                onChangeSetting(setting);
            }
        });

        return view;
    }

    private View renderModal(LayoutInflater inflater, ViewGroup parent, final SettingsEntity setting){
        View view = inflater.inflate(R.layout.settings_list_item_simple, parent, false);

        TextView title = view.findViewById(R.id.setting_list_item_simple_title);
        TextView subTitle = view.findViewById(R.id.setting_list_item_simple_subtitle);

        title.setText(this.getTitle(setting));
        subTitle.setText(this.getSubtitle(setting));

        if(setting.setting_type.equals(Settings.Types.num_picker.toString())){
            return renderNumPickerModal(view, setting);
        } else if(setting.setting_type.equals(Settings.Types.text_modal.toString())){
            return renderTextModal(view, setting);
        }

        return view;
    }

    private View renderNumPickerModal(View view, final SettingsEntity setting){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "opening modal here");

                final Dialog d = new Dialog(getContext());
                d.setContentView(R.layout.settings_number_dialog);

                Button OK = d.findViewById(R.id.dialogNumPicker_ok);
                Button Cancel = d.findViewById(R.id.dialogNumPicker_cancel);
                final NumberPicker num_picker = d.findViewById(R.id.dialogNumPicker);
                num_picker.setMinValue(0);
                num_picker.setMaxValue(1000);
                num_picker.setValue(Integer.parseInt(setting.setting_value));
                num_picker.setWrapSelectorWheel(true);
                OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setting.setting_value = String.valueOf(num_picker.getValue());
                        onChangeSetting(setting);
                        d.dismiss();
                    }
                });
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                d.show();

            }
        });

        return view;
    }

    private View renderTextModal(View view, final SettingsEntity setting){
        view.setOnClickListener(v -> {
            Log.d(LOG_TAG, "opening modal here");

            final Dialog d = new Dialog(getContext());
            d.setContentView(R.layout.settings_text_dialog);

            final EditText editText = d.findViewById(R.id.dialogInputText);
            editText.setText(setting.setting_value);

            Button OK = d.findViewById(R.id.dialogInputText_ok);
            Button Cancel = d.findViewById(R.id.dialogInputText_cancel);

            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting.setting_value = editText.getText().toString();
                    onChangeSetting(setting);
                    d.dismiss();
                }
            });
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });

            d.show();

        });

        return view;
    }

    private void onChangeSetting(SettingsEntity setting){
        Log.d(LOG_TAG, "changed setting");
        Store store = Store.getInstance();
        store.getSettings().saveSetting(setting);
    }

}
