package com.lyubomyr.wordscounter;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainView extends AppCompatActivity {

    private String LOG_TAG = "MAIN_VIEW";

    protected String text;
    protected WordCounter wordCounter;

    private EditText inputText;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_text_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        inputText = (EditText) findViewById(R.id.inputText);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

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
        showResultsView(result);

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

    protected void showResultsView(String result) {
        Intent showResults = new Intent(MainView.this, DisplayResultsView.class);
        showResults.putExtra("results", result);
        MainView.this.startActivity(showResults);
    }

    // obsolete method

//    protected void displayResults(String results){
//        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        //mainLayout.setVisibility(View.INVISIBLE);
//        EditText inputText = (EditText) findViewById(R.id.inputText);
//        inputText.setVisibility(View.INVISIBLE);
//
//        final int resultsLayoutId = View.generateViewId();
//        final int resultsBackButtonId = View.generateViewId();
//
//        LinearLayout resultsLayout = new LinearLayout(this);
//        resultsLayout.setOrientation(LinearLayout.HORIZONTAL);
//        resultsLayout.setMinimumHeight(mainLayout.getHeight());
//        resultsLayout.setMinimumWidth(mainLayout.getWidth());
//        resultsLayout.setId(resultsLayoutId);
//
//        TextView resultsView = new TextView(this);
//        resultsView.setText(results);
//        resultsLayout.addView(resultsView);
//
//        Button hideResultsButton = new Button(this);
////        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100 /*width*/, 50 /*height*/);
////        layoutParams.setMargins(0, 0, 0, 0 /*int left, int top, int right, int bottom*/);
////        hideResultsButton.setLayoutParams(layoutParams);
//        hideResultsButton.setText("Back");
//
//        hideResultsButton.setId(resultsBackButtonId);
//
//        hideResultsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LinearLayout resultsLayout = (LinearLayout) findViewById(resultsLayoutId);
//                ViewGroup resultsLayoutParent = (ViewGroup) resultsLayout.getParent();
//                if(null != resultsLayoutParent) //for safety only  as you are doing onClick
//                    resultsLayoutParent.removeView(resultsLayout);
//
//                EditText inputText = (EditText) findViewById(R.id.inputText);
//                inputText.setVisibility(View.VISIBLE);
//
//                Button hideResultsButton = (Button) findViewById(resultsBackButtonId);
//                ViewGroup backButtonParent = (ViewGroup) hideResultsButton.getParent();
//                if(null != backButtonParent) //for safety only  as you are doing onClick
//                    backButtonParent.removeView(hideResultsButton);
//            }
//        });
//
//        toolbar.addView(hideResultsButton);
//
//        mainLayout.addView(resultsLayout);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        //obsolete
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        */

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
