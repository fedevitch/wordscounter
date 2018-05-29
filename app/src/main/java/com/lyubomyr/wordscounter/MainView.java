package com.lyubomyr.wordscounter;

import android.app.PendingIntent;
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

import java.io.Serializable;

public class MainView extends AppCompatActivity {

    private String LOG_TAG = "MAIN_VIEW";

    protected String text;
    protected WordCounter wordCounter;

    private EditText inputText;
    private DrawerLayout mDrawerLayout;

    private Intent showResults;
    private PendingIntent pendingIntent;

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

        this.showResults = new Intent(this, DisplayResultsView.class);
        this.pendingIntent = TaskStackBuilder.create(this)
                // add all of DetailsActivity's parents to the stack,
                // followed by DetailsActivity itself
                .addNextIntentWithParentStack(showResults).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

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

                        return true;
                    }
                });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                        Log.d(LOG_TAG, "slide");
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                        Log.d(LOG_TAG, "opened");
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                        Log.d(LOG_TAG, "closed");
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                        Log.d(LOG_TAG, "changed");
                    }
                }
        );
    }

    protected void startCount(View view) {
        wordCounter = new WordCounter();
        text = inputText.getText().toString();

        wordCounter.startCounting(text);

        String result = wordCounter.getResult();
        Log.d(LOG_TAG, result);
        showResultsView(wordCounter.getCountResult(), wordCounter.getResult());

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

    protected void showResultsView(CountResult countResult, String result) {

        this.showResults.putExtra(DisplayResultsView.KEY_STRING_RESULTS, result);
        //this.showResults.putExtra("countResult", (Serializable) countResult);
        this.showResults.putExtra(DisplayResultsView.KEY_RESULTS, "sss");
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
}
