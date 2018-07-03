package com.lyubomyr.wordscounter;

import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.lyubomyr.wordscounter.Storage.SettingsViewModel;

import java.io.Serializable;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        inputText = (EditText) findViewById(R.id.inputText);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        this.store = Store.getInstance();

        this.showResults = new Intent(this, DisplayResultsView.class);

        //wordCounter = new WordCounter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCount(view);
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                        //Log.d(LOG_TAG, "slide");
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                        //Log.d(LOG_TAG, "opened");
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                        //Log.d(LOG_TAG, "closed");
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                        //Log.d(LOG_TAG, "changed");
                        //Log.d(LOG_TAG, String.valueOf(newState));
                    }
                }
        );

        initializeSettings();
    }

    protected void startCount(View view) {
        wordCounter = new WordCounter();
        text = inputText.getText().toString();

        wordCounter.startCounting(text);

        showResultsView(wordCounter.getCountResult());

//        Snackbar.make(view, "Calculating words, please wait...", Snackbar.LENGTH_INDEFINITE)
//                .setAction("", null).show();
//
//        Snackbar.make(view, "Words calculated", Snackbar.LENGTH_INDEFINITE)
//                .setAction("Show results",new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //displayResults(wordCounter.getResult());
//                        String result = wordCounter.getResult();
//                        Log.d(LOG_TAG, result);
//                        showResultsView(result);
//                    }
//                }).show();
    }

    protected void showResultsView(CountResult countResult) {
        store.setCountResult(countResult);
        this.showResults.putExtra(DisplayResultsView.VIEWING_SAVED_RESULT, false);
        startActivity(this.showResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_text, menu);
        return true;
    }

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

    private void initializeSettings(){

        Runnable r = new Runnable() {
            @Override
            public void run() {
                SettingsViewModel settingsViewModel = ViewModelProviders.of(MainView.this).get(SettingsViewModel.class);

                Settings settings = new Settings(settingsViewModel);

                store.setSettings(settings);

                Log.d(LOG_TAG, "Settings set");

                //

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

    public void openSettings(){
        Intent settingsIntent = new Intent(this, SettingsView.class);
        startActivity(settingsIntent);
    }

    public void openSavedCounts(){
        Intent historyIntent = new Intent(this, SavedCountsView.class);
        startActivity(historyIntent);
    }

    public void closeApplication(){
        finishAndRemoveTask();
    }
}
