package com.example.andrzej.zabytki;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class SettingsActivity extends Activity {
//
//    private RadioGroup rG;
//    private RadioButton rBData, rBRating;

    private RadioGroup radioGroup;

    private static String SELECTED_INDEX="SelectedIndex";


    private RadioGroup.OnCheckedChangeListener checkedChangedListener=new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            saveSelectedIndex(checkedId);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        rBData = (RadioButton)findViewById(R.id.radioButton);
//        rBRating = (RadioButton)findViewById(R.id.radioButton2);
//        SharedPreferences prefs = this.getSharedPreferences(
//                "com.example.app", Context.MODE_PRIVATE);
//
//        boolean rData = prefs.getBoolean("rBData", true);
//        boolean rRating = prefs.getBoolean("rBRating",true);
//        if(rData){
//            rBData.setChecked(true);
//        }else {
//            rBRating.setChecked(true);
//        }

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangedListener);
        RadioButton rbtn=((RadioButton)radioGroup.findViewById(getSelectedValue()));
        if(rbtn!=null) {
            rbtn.setChecked(true);
        }

        String value = Integer.toString(getSelectedValue());
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
//        SharedPreferences prefs = this.getSharedPreferences(
//                "com.example.app", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        if(rBData.isChecked()){
//            editor.putBoolean("rBData",rBData.isChecked());
//        }else {
//            editor.putBoolean("rBRating", rBRating.isChecked());
//        }
//        editor.apply();
//
//        Context context = getApplicationContext();
//        CharSequence text = "Ustawienia zosta�y zapisane";
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
    }
}