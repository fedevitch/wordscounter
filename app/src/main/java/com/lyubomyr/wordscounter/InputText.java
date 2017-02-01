package com.lyubomyr.wordscounter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyubomyr.wordscounter.WordCounter;

public class InputText extends AppCompatActivity {

    protected String text;
    protected WordCounter wordCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final EditText inputText = (EditText) findViewById(R.id.inputText);
        setSupportActionBar(toolbar);

        //wordCounter = new WordCounter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordCounter = new WordCounter();
                text = inputText.getText().toString();

                wordCounter.startCounting(text);

                Snackbar.make(view, "Calculating words, please wait...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("", null).show();

                Snackbar.make(view, "Words calculated", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Show results",new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                displayResults(wordCounter.getResult());
                            }
                        }).show();

            }
        });
    }

    protected void displayResults(String results){
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //mainLayout.setVisibility(View.INVISIBLE);
        EditText inputText = (EditText) findViewById(R.id.inputText);
        inputText.setVisibility(View.INVISIBLE);

        final int resultsLayoutId = View.generateViewId();
        final int resultsBackButtonId = View.generateViewId();

        LinearLayout resultsLayout = new LinearLayout(this);
        resultsLayout.setOrientation(LinearLayout.HORIZONTAL);
        resultsLayout.setMinimumHeight(mainLayout.getHeight());
        resultsLayout.setMinimumWidth(mainLayout.getWidth());
        resultsLayout.setId(resultsLayoutId);

        TextView resultsView = new TextView(this);
        resultsView.setText(results);
        resultsLayout.addView(resultsView);

        Button hideResultsButton = new Button(this);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100 /*width*/, 50 /*height*/);
//        layoutParams.setMargins(0, 0, 0, 0 /*int left, int top, int right, int bottom*/);
//        hideResultsButton.setLayoutParams(layoutParams);
        hideResultsButton.setText("Back");

        hideResultsButton.setId(resultsBackButtonId);

        hideResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout resultsLayout = (LinearLayout) findViewById(resultsLayoutId);
                ViewGroup resultsLayoutParent = (ViewGroup) resultsLayout.getParent();
                if(null != resultsLayoutParent) //for safety only  as you are doing onClick
                    resultsLayoutParent.removeView(resultsLayout);

                EditText inputText = (EditText) findViewById(R.id.inputText);
                inputText.setVisibility(View.VISIBLE);

                Button hideResultsButton = (Button) findViewById(resultsBackButtonId);
                ViewGroup backButtonParent = (ViewGroup) hideResultsButton.getParent();
                if(null != backButtonParent) //for safety only  as you are doing onClick
                    backButtonParent.removeView(hideResultsButton);
            }
        });

        toolbar.addView(hideResultsButton);

        mainLayout.addView(resultsLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
