package com.example.andrzej.zabytki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.UserRecoverableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void Map(View view) {

        Intent mapa = new Intent(this, MapsActivity.class);


        startActivity(mapa);
    }

    public void Help(View view) throws UserRecoverableException, GooglePlayServicesNotAvailableException {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Context context = getApplicationContext();
        startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                /*String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
                PlaceData.ID = place.getId();
                PlaceData.ADDRESS = String.valueOf(place.getAddress());
                PlaceData.LATLNG = place.getLatLng();
                PlaceData.LOCALE = place.getLocale();
                PlaceData.NAME = place.getName().toString();
                PlaceData.PHONE_NUMBER = place.getPhoneNumber().toString();
                PlaceData.PRICING = place.getPriceLevel();
                PlaceData.TYPES = place.getPlaceTypes();
                PlaceData.URI = place.getWebsiteUri().toString();
                PlaceData.RATING = place.getRating();

                Intent placeActivity = new Intent(this, PlaceActivity.class);

                startActivity(placeActivity);
            }
        }
    }

    public void Lists(View view) {
        Intent activity = new Intent(this, FavouritePlacesActivity.class);

        startActivity(activity);

    }
}
