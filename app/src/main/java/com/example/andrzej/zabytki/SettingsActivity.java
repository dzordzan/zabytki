package com.example.andrzej.zabytki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends Activity {


    private RadioGroup radioGroup;

    public static String SELECTED_INDEX="SelectedIndex";


    private RadioGroup.OnCheckedChangeListener checkedChangedListener=new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            saveSelectedIndex(checkedId);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangedListener);
        RadioButton rbtn=((RadioButton)radioGroup.findViewById(getSelectedValue()));
        if(rbtn!=null) {
            rbtn.setChecked(true);
        }

        String value = Integer.toString(getSelectedValue());
        TextView test = (TextView)findViewById(R.id.testText);
        test.setText(value);
    }

    private int getSelectedValue(){
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getInt(SELECTED_INDEX, -1);
    }

    private void saveSelectedIndex(int value){
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt(SELECTED_INDEX, value);
        editor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public void saveSettings(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Ustawienia zostały zapisane";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

//        Intent menu = new Integer(this, MainActivity.class);
//        startActivity(menu);
    }
}
