package com.lyubomyr.wordscounter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.lyubomyr.wordscounter.Storage.SavedResultsViewModel;
import java.util.List;


public class SavedCountsViewCellAdapter extends ArrayAdapter {

    private final String LOG_TAG = "Saved counts adapter";
    private final Context context;
    private List<CountResult> countResults;
    private final SavedResultsViewModel savedResultsViewModel;

    SavedCountsViewCellAdapter(Context context, List<CountResult> results, SavedResultsViewModel savedResultsViewModel){
        super(context, R.layout.saved_results_list_item);
        this.context = context;
        this.savedResultsViewModel = savedResultsViewModel;

        this.countResults = results;

        Log.d(LOG_TAG, "Adapter Started");
        Log.d(LOG_TAG, String.valueOf(countResults.size()));
    }

    @Override
    public int getCount() {
        return this.countResults.size();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    static class ViewHolder {
        TextView title;
        TextView subtitle;
        TextView createdAt;
        Button viewButton;
        Button removeButton;
        int position;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        final CountResult result = countResults.get(position);
        final ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            try {
                assert inflater != null;
                convertView = inflater.inflate(R.layout.saved_results_list_item, parent, false);
                convertView.setTag(holder);
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "exception caught");
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String title = "No text";
        if (null != result.text) {
            if (result.text.length() >= 20) {
                title = result.text.substring(0, 20) + "...";
            } else {
                title = result.text;
            }
        }
        assert convertView != null;
        holder.title = convertView.findViewById(R.id.saved_results_list_item_title);
        holder.title.setText(title);

        String subtitle = "Uniq words: " + String.valueOf(result.getWordsCount()) + " chars: " + String.valueOf(result.getCharsCount());
        holder.subtitle = convertView.findViewById(R.id.saved_results_list_item_subtitle);
        holder.subtitle.setText(subtitle);

        holder.createdAt = convertView.findViewById(R.id.saved_results_list_item_date);
        holder.createdAt.setText(result.created_at.toString());

        holder.viewButton = convertView.findViewById(R.id.saved_results_list_item_view_button);
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "view clicked");
                viewResult(result);
            }
        });

        holder.removeButton = convertView.findViewById(R.id.saved_results_list_item_remove_button);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "remove clicked");
                removeResult(result.getResultId());
            }
        });

        return convertView;
    }

    public void setCountResultsList(List<CountResult> newData){
        this.countResults = newData;
        notifyDataSetChanged();
    }

    private void viewResult(CountResult result){
        Store store = Store.getInstance();
        store.setCountResult(result);

        Intent showResults = new Intent(context, DisplayResultsView.class);
        showResults.putExtra(DisplayResultsView.VIEWING_SAVED_RESULT, true);
        context.startActivity(showResults);
    }

    private void removeResult(final String resultId){

        Runnable r = () -> savedResultsViewModel.deleteSavedResult(resultId);

        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch(Exception e){
            Log.e(LOG_TAG, "Тред не дочекався");
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

    }

}


