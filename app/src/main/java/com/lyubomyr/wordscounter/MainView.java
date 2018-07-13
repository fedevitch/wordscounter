package com.lyubomyr.wordscounter;

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

    private String LOG_TAG = "MAIN_VIEW";

    private Store store;

    protected String text;
    protected WordCounter wordCounter;

    private EditText inputText;
    private DrawerLayout mDrawerLayout;

    private Intent showResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        inputText = findViewById(R.id.inputText);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        this.store = Store.getInstance();

        this.showResults = new Intent(this, DisplayResultsView.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCount();
            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
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
                        }

                        return true;
                    }
                });

//        mDrawerLayout.addDrawerListener(
//                new DrawerLayout.DrawerListener() {
//                    @Override
//                    public void onDrawerSlide(View drawerView, float slideOffset) {
//                        // Respond when the drawer's position changes
//                        //Log.d(LOG_TAG, "slide");
//                    }
//
//                    @Override
//                    public void onDrawerOpened(View drawerView) {
//                        // Respond when the drawer is opened
//                        //Log.d(LOG_TAG, "opened");
//                    }
//
//                    @Override
//                    public void onDrawerClosed(View drawerView) {
//                        // Respond when the drawer is closed
//                        //Log.d(LOG_TAG, "closed");
//                    }
//
//                    @Override
//                    public void onDrawerStateChanged(int newState) {
//                        // Respond when the drawer motion state changes
//                        //Log.d(LOG_TAG, "changed");
//                        //Log.d(LOG_TAG, String.valueOf(newState));
//                    }
//                }
//        );

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_input_text, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "item selected");
        Log.d(LOG_TAG, String.valueOf(item.getItemId()));

        switch (item.getItemId()) {
            case android.R.id.home:
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

        Runnable r = new Runnable() {
            @Override
            public void run() {
                SettingsViewModel settingsViewModel = ViewModelProviders.of(MainView.this).get(SettingsViewModel.class);

                Settings settings = new Settings(settingsViewModel);

                store.setSettings(settings);

                Log.d(LOG_TAG, "Settings set");

            }
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

    private void openSettings(){
        Intent settingsIntent = new Intent(this, SettingsView.class);
        startActivity(settingsIntent);
    }

    private void openSavedCounts(){
        Intent historyIntent = new Intent(this, SavedCountsView.class);
        startActivity(historyIntent);
    }

    private void closeApplication(){
        finishAndRemoveTask();
    }
}
