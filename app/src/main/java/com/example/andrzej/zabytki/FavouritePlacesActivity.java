package com.example.andrzej.zabytki;

<<<<<<< HEAD
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class FavouritePlacesActivity extends ListActivity {

    private DatabaseHandler db;
    private SimpleCursorAdapter places;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_places);

        // nowy obiekt - obsługa bazy danych
        db = new DatabaseHandler(this);

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

        //Intent cel = new Intent(this, NoteSettingsActivity.class);
        //cel.putExtra("id", (int) id);
        //startActivity(cel);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
=======
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class FavouritePlacesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_places);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite_places, menu);
>>>>>>> zabytki/master
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
<<<<<<< HEAD

    // metoda uzupełnia naszą listę danymi pobranymi z bazy
    private void fillData() {

        Cursor c = db.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { DatabaseHandler.KEY_NAME,
                                       DatabaseHandler.KEY_ADDRESS,
                                    //DatabaseHandler.KEY_RATING,
                                    DatabaseHandler.KEY_DATE};
        int[] to = new int[] { R.id.placeName, R.id.placeAddress, R.id.placeDate };

        // Uzupełniamy listę wartościami
        this.places = new SimpleCursorAdapter(this, R.layout.list_item, c, from, to);
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

        if (DatabaseHandler.ORDER == "ASC") {
            DatabaseHandler.ORDER = "DESC";
            b.setImageResource(android.R.drawable.arrow_down_float);
        } else {
            DatabaseHandler.ORDER = "ASC";
            b.setImageResource(android.R.drawable.arrow_up_float);
        }
        fillData();

    }
=======
>>>>>>> zabytki/master
}
