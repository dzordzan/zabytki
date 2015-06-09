package com.example.andrzej.zabytki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class PlaceActivity extends Activity {
    private TextView name;
    private TextView addres;
    private TextView number;
    private TextView url;
    private RatingBar rating;
    private Button addOrDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);


        name= (TextView)findViewById(R.id.textView);
        addres = (TextView) findViewById(R.id.textView2);
        number = (TextView) findViewById(R.id.textView3);
        url = (TextView) findViewById(R.id.textView4);
        addOrDel = (Button) findViewById(R.id.button2);
        rating = (RatingBar) findViewById(R.id.ratingBar);


        if(PlaceData.NAME == null){
            onBackPressed();
            Context context = getApplicationContext();
            CharSequence text = "Przepraszamy ale wystąpił problem";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            name.setText(PlaceData.NAME);
            addres.setText(PlaceData.ADDRESS);
            number.setText(PlaceData.PHONE_NUMBER);
            url.setText(PlaceData.URI);
            rating.setRating(PlaceData.RATING);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zabytek, menu);
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

    public void navigation(View view) {

        String latitude  = Double.toString(PlaceData.LATLNG.latitude);
        String longitude = Double.toString(PlaceData.LATLNG.longitude);
        String navigateTo = "google.navigation:q=" + latitude +"," + longitude;

        Uri gmmIntentUri = Uri.parse(navigateTo);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void addToFavourite(View view) {
       // if(){

            //hendler insert
           // Intent favourite = new Intent(this, class.FavouritePlacesActivity);
           // startActivity(favourite);
       // }
       // else{
            //delete
            Context context = getApplicationContext();
            CharSequence text = "Obiekt został usunięty z ulubionych";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
       // }
    }

    public void phoneNumber(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        //callIntent.setData(Uri.parse()));
        startActivity(callIntent);
    }
}
