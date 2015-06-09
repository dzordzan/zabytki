package com.example.andrzej.zabytki;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Rating;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.UserRecoverableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


@SuppressWarnings("deprecation")
public class FavouritePlacesActivity extends ListActivity {

    private DatabaseHandler db;
    private PlaceCursorAdapter places;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_places);

        // nowy obiekt - obsługa bazy danych
        db = new DatabaseHandler(this);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
            int idDefault = pref.getInt("SelectedIndex", -1);

       //ImageButton b = (ImageButton)view;
        if (idDefault == 2131361912) {
            DatabaseHandler.SORT = DatabaseHandler.KEY_RATING;
            //b.setImageResource(android.R.drawable.arrow_down_float);
        } else {
            DatabaseHandler.SORT = DatabaseHandler.KEY_DATE;
           // b.setImageResource(android.R.drawable.arrow_up_float);
        }
        // wywołanie metody uzupełniającej listę
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Log.i("BAZA", "Pozycja: " + position + " ID: " + id);
        // informacja o danych związanych z kliknięciem (id jest związane z naszą bazą)

        db.getPlace(id);

        Intent cel = new Intent(this, PlaceActivity.class);
        startActivity(cel);
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



    // metoda uzupełnia naszą listę danymi pobranymi z bazy
    private void fillData() {

        Cursor c = db.fetchAllNotes();

        ListView lvItems = (ListView) findViewById(R.id.list_item);
        this.places = new PlaceCursorAdapter(this, c);

        setListAdapter(places);

    }

    public void addNote(View view) {

        // wywołanie aktywności
      //  Intent intent = new Intent(this, AddNoteActivity.class);
      //  startActivity(intent);
    }

    public void deletePlace(View view) {
        final int position = getListView().getPositionForView((LinearLayout) view.getParent());
        final long idNotatki = places.getItemId(position);


        new AlertDialog.Builder(this)
                .setTitle("Kasujesz z ulubionych")
                .setMessage("Czy na pewno chcesz usunąć bezpowrotnie placówkę?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        db.deletePlace(idNotatki);
                        fillData();
                        Toast.makeText(FavouritePlacesActivity.this, "Placówka usunięta", Toast.LENGTH_SHORT).show();


                    }})
                .setNegativeButton(android.R.string.no, null).show();


    }

    public void sortDate(View view) {
        DatabaseHandler.SORT = DatabaseHandler.KEY_DATE;
        fillData();
        Button b = (Button)view;
        Button bPrioryetet = (Button)findViewById(R.id.bPriorytet);
        b.setEnabled(false);
        bPrioryetet.setEnabled(true);
    }

    public void sortPriority(View view) {
        DatabaseHandler.SORT = DatabaseHandler.KEY_RATING;
        fillData();
        Button b = (Button)view;
        Button b2 = (Button)findViewById(R.id.button);
        b.setEnabled(false);
        b2.setEnabled(true);
    }

    public void setOrder(View view) {

        ImageButton b = (ImageButton)view;

        if (DatabaseHandler.ORDER.equals("ASC")) {
            DatabaseHandler.ORDER = "DESC";
            b.setImageResource(android.R.drawable.arrow_down_float);
        } else {
            DatabaseHandler.ORDER = "ASC";
            b.setImageResource(android.R.drawable.arrow_up_float);
        }
        fillData();

    }

    public void findPlace(View view) throws UserRecoverableException, GooglePlayServicesNotAvailableException {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Context context = getApplicationContext();
        startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                PlaceData.ID = place.getId();
                PlaceData.ADDRESS = String.valueOf(place.getAddress());
                PlaceData.LATLNG = place.getLatLng();
                PlaceData.LOCALE = place.getLocale();
                PlaceData.NAME = place.getName().toString();
                PlaceData.PHONE_NUMBER = (place.getPhoneNumber()!=null)?place.getPhoneNumber().toString():null;
                PlaceData.PRICING = place.getPriceLevel();
                PlaceData.TYPES = place.getPlaceTypes();
                PlaceData.URI = (place.getWebsiteUri()!=null)?place.getWebsiteUri().toString():null;
                PlaceData.RATING = place.getRating();

                Intent placeActivity = new Intent(this, PlaceActivity.class);

                startActivity(placeActivity);
            }
        }
    }
}

class PlaceCursorAdapter extends CursorAdapter {
    public PlaceCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView name = (TextView) view.findViewById(R.id.placeName);
        TextView address = (TextView) view.findViewById(R.id.placeAddress);
        TextView date = (TextView) view.findViewById(R.id.placeDate);
        RatingBar rating = (RatingBar) view.findViewById(R.id.ratingBar2);
       /* ,
        DatabaseHandler.KEY_ADDRESS,
        DatabaseHandler.KEY_RATING,
        DatabaseHandler.KEY_DATE}; DatabaseHandler.KEY_NAME,
        DatabaseHandler.KEY_ADDRESS,
        DatabaseHandler.KEY_RATING,
        DatabaseHandler.KEY_DATE};*/

        // Populate fields with extracted properties
        name.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.KEY_NAME)));
        address.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.KEY_ADDRESS)));
        date.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.KEY_DATE)));
        rating.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHandler.KEY_RATING)));

    }
}