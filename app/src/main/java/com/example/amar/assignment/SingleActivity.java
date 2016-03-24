package com.example.amar.assignment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle extra=getIntent().getExtras();
        if(extra!=null) {

            String i=extra.getString("ingredient_name");
            String steps[]=i.split("\n");
            String x="";
            for(int j=0;j<steps.length;j++)
                x=x+"• INGREDIENT "+(j+1)+": "+steps[j]+"\n\n";

            TextView textview=(TextView)findViewById(R.id.textView);


            textview.setText(x);
           // Log.d("Position",steps[0]);


        }
        else
            Log.d("In Activity","name not found");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }


}
