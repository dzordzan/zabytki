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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.UserRecoverableException;
import com.google.android.gms.location.places.ui.PlacePicker;


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

        //if(){ // obiekt w ulubionych
       //     addOrDel.setText("Usuń z ulubionych");
       // }else{
            addOrDel.setText("Dodaj do ulubionych");
      //  }

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

            if(PlaceData.PHONE_NUMBER != null){
                number.setText(PlaceData.PHONE_NUMBER);
               // number.setClickable(true);
            }else
                number.setText("Brak Numeru Tel");
               // number.setClickable(false);
            if(PlaceData.URI != null){
                url.setText(PlaceData.URI);
                url.setClickable(true);
            }
            else {
                url.setText("Brak Url");
                url.setClickable(false);
            }

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
        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
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

        String phoneNumber = "tel:" + PlaceData.PHONE_NUMBER;
        Uri number = Uri.parse(phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    public void openBrowse(View view) {
        String url = PlaceData.URI;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void map(View view) throws UserRecoverableException, GooglePlayServicesNotAvailableException {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Context context = getApplicationContext();
        startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
    }
}
