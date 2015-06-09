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
        Log.i("BAZA", "Pozycja: " + position + " ID: " + id);
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
        /*startManagingCursor(c);

        String[] from = new String[] { DatabaseHandler.KEY_NAME,
                                       DatabaseHandler.KEY_ADDRESS,
                                    DatabaseHandler.KEY_RATING,
                                    DatabaseHandler.KEY_DATE};
        int[] to = new int[] { R.id.placeName, R.id.placeAddress, R.id.ratingBar2, R.id.placeDate };

        // Uzupełniamy listę wartościami
        this.places = new SimpleCursorAdapter(this, R.layout.list_item, c, from, to);
        */
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
        Log.i("test", "POZYCJA:"+position+" ID_NOTKI:"+idNotatki);


        new AlertDialog.Builder(this)
                .setTitle("Kasujesz z ulubionych")
                .setMessage("Czy na pewno chcesz usunąć bezpowrotnie notatkę?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        db.deletePlace(idNotatki);
                        fillData();
                        Toast.makeText(FavouritePlacesActivity.this, "Notatka usunięta", Toast.LENGTH_SHORT).show();


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
        Log.i("tag", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHandler.KEY_PRICING)));
    }
}