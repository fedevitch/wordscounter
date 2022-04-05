package com.lyubomyr.wordscounter;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import org.jsoup.Jsoup;

import java.io.IOException;


public class MainView extends AppCompatActivity {

    private final String LOG_TAG = "MAIN_VIEW";

    private Store store;

    protected String text;
    protected WordCounter wordCounter;

    private EditText inputText;
    private DrawerLayout mDrawerLayout;

    private Intent showResults;

    private static final int enterAnimation = android.R.anim.slide_in_left;
    private static final int exitAnimation = android.R.anim.slide_out_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(enterAnimation, exitAnimation);
        setContentView(R.layout.main_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        inputText = findViewById(R.id.inputText);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        this.store = Store.getInstance();

        this.showResults = new Intent(this, DisplayResultsView.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startCount());

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    switch(menuItem.getItemId()) {
                        case R.id.nav_new:
                            Log.d(LOG_TAG, "new");
                            return true;
                        case R.id.nav_history:
                            Log.d(LOG_TAG, "history");
                            openSavedCounts();
                            return true;
                        case R.id.nav_settings:
                            Log.d(LOG_TAG, "settings");
                            openSettings();
                            return true;
                        case R.id.nav_exit:
                            Log.d(LOG_TAG, "exit");
                            closeApplication();
                            return true;
                        default:
                            return true;
                    }
                });

        initializeSettings();

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleTextSendTo(intent); // Handle text being sent
            }
        }

        if(intent.hasExtra(DisplayResultsView.VIEWING_SAVED_RESULT) &&
           intent.getBooleanExtra(DisplayResultsView.VIEWING_SAVED_RESULT, true)){
            inputText.setText(store.getCountResult().text);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "item selected");
        Log.d(LOG_TAG, String.valueOf(item.getItemId()));

        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void startCount() {
        wordCounter = new WordCounter();
        text = inputText.getText().toString();

        wordCounter.startCounting(text);

        showResultsView(wordCounter.getCountResult());

    }

    protected void showResultsView(CountResult countResult) {
        store.setCountResult(countResult);
        this.showResults.putExtra(DisplayResultsView.VIEWING_SAVED_RESULT, false);
        startActivity(this.showResults);
    }

    private void initializeSettings(){

        Runnable r = () -> {
            SettingsViewModel settingsViewModel = ViewModelProviders.of(MainView.this).get(SettingsViewModel.class);

            Settings settings = new Settings(settingsViewModel);

            store.setSettings(settings);

            Log.d(LOG_TAG, "Settings set");

        };


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

    private void handleTextSendTo(Intent intent){
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(sharedText != null) {
            if(URLUtil.isValidUrl(sharedText)){
                handleHtmlSendTo(sharedText);
            } else {
                Log.d(LOG_TAG, sharedText);
                inputText.setText(sharedText);
            }
        }
    }

    private void handleHtmlSendTo(String link){

        Log.d(LOG_TAG, "Parsing web page");
        Log.d(LOG_TAG, link);

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute(link);

    }

    private class JsoupAsyncTask extends AsyncTask<String, Void, Void> {

        private String htmlContentInStringFormat;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                org.jsoup.nodes.Document htmlDocument = Jsoup.connect(params[0]).get();
                htmlContentInStringFormat = htmlDocument.text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            inputText.setText(htmlContentInStringFormat);
        }
    }

    private Bundle getNavAnimation(){
        return ActivityOptions
                .makeCustomAnimation(
                        this,
                        enterAnimation,
                        exitAnimation
                )
                .toBundle();
    }

    private void openSettings(){
        Intent settingsIntent = new Intent(this, SettingsView.class);
        startActivity(settingsIntent, getNavAnimation());
    }

    private void openSavedCounts(){
        Intent historyIntent = new Intent(this, SavedCountsView.class);
        startActivity(historyIntent, getNavAnimation());
    }

    private void closeApplication(){
        finishAndRemoveTask();
    }
}
