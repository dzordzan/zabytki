package com.example.andrzej.zabytki;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class DatabaseHandler extends SQLiteOpenHelper {

    // stałe do bazy
    // wersja bazy
    private static final int DATABASE_VERSION = 1;

    // nazwa bazy
    private static final String DATABASE_NAME = "bazadev_1.db";

    // tabela z ulubionymi miejscami
    private static final String TABLE_PLACES = "places2";

    // nazwy kolumn naszej tabeli
    public static final String KEY_ID = "_id";
    public static final String KEY_G_ID = "g_id"; //id obiektu z google
    public static final String KEY_NAME = "nazwa";
    public static final String KEY_ADDRESS = "adres";
    public static final String KEY_DATE = "data";
    public static final String KEY_RATING = "ocena";
    public static final String KEY_PHONE_NUMBER = "numer_telefonu";
    public static final String KEY_URI = "uri";
    public static final String KEY_PRICING = "cena";
    public static final String KEY_TYPES = "typy";
    //public static Locale locale;
    public static final String KEY_LATITUDE = "szerokosc";
    public static final String KEY_LONGITUDE = "dlugosc";

    public static String SORT = KEY_RATING;
    public static String ORDER = "DESC";
    // referencja do bazy
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();


    }

    // tworzenie tabeli
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_PLACES + "(" + KEY_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_G_ID +  " TEXT," +
                KEY_NAME +  " TEXT," +
                KEY_ADDRESS + " TEXT," +
                KEY_DATE + " DATETIME," +
                KEY_RATING + " REAL," +
                KEY_PHONE_NUMBER + " TEXT," +
                KEY_URI + " TEXT," +
                KEY_PRICING + " INT," +
                KEY_TYPES + " TEXT," +
                KEY_LONGITUDE + " TEXT, " +
                KEY_LATITUDE + " TEXT" +
                ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    // aktualizacja bazy
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // usunięcie tabeli z notatkami
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        // ponowne utworzenie tabeli
        onCreate(db);
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    // dodanie miejsca
    // przekazujemy klase z polami statycznymi, bo tak jest najłatwiej
    public void addPlace() {

        ContentValues values = new ContentValues();
        values.put(KEY_G_ID, PlaceData.ID);
        values.put(KEY_NAME, PlaceData.NAME); // nazwa miejsca
        values.put(KEY_ADDRESS, PlaceData.ADDRESS);  // treść notatki
        values.put(KEY_DATE, getDateTime());  // ustawiamy date dodania
        values.put(KEY_RATING, PlaceData.RATING);
        values.put(KEY_PHONE_NUMBER, PlaceData.PHONE_NUMBER);
        values.put(KEY_URI, PlaceData.URI);
        values.put(KEY_PRICING, PlaceData.PRICING);
        values.put(KEY_TYPES, PlaceData.TYPES.toString());
        values.put(KEY_LATITUDE, Double.toString(PlaceData.LATLNG.latitude));
        values.put(KEY_LONGITUDE, Double.toString(PlaceData.LATLNG.longitude));


        // wstawienie notatki do bazy
        db.insert(TABLE_PLACES, null, values);

    }

    // pobranie pojedynczej notatki po id z googla
    public boolean getPlace(String id) {
        Cursor cursor = db.query(TABLE_PLACES, null,
                KEY_G_ID + "=?", new String[] { id }, null, null, null, null);


        if(cursor.getCount() <= 0)
            return false;

        if (cursor != null)
            cursor.moveToFirst();

        List<Integer> typy = new ArrayList<Integer>();
        typy.add(1);

        PlaceData.ID = cursor.getString(1); // G_ID
        PlaceData.ADDRESS = cursor.getString(3);
        PlaceData.NAME = cursor.getString(2);
        PlaceData.PHONE_NUMBER = cursor.getString(6);
        PlaceData.URI = cursor.getString(7);
        PlaceData.RATING = Float.parseFloat(cursor.getString(5));
        PlaceData.PRICING = Integer.parseInt(cursor.getString(8));
        PlaceData.TYPES = typy;
        //PlaceData.LOCALE;
        PlaceData.LATLNG = new LatLng(Double.parseDouble(cursor.getString(10)), // dlugosc
                Double.parseDouble(cursor.getString(11)));

        return true;
    }
    public boolean getPlace(long id) {
        Cursor cursor = db.query(TABLE_PLACES, null,
                KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);


        if(cursor.getCount() <= 0)
            return false;

        if (cursor != null)
            cursor.moveToFirst();

        List<Integer> typy = new ArrayList<Integer>();
        typy.add(1);
        //Log.i("aaaXX", cursor.getString(3));
        PlaceData.ID = cursor.getString(1); // G_ID
        PlaceData.ADDRESS = cursor.getString(3);
        PlaceData.NAME = cursor.getString(2);
        PlaceData.PHONE_NUMBER = cursor.getString(6);
        PlaceData.URI = cursor.getString(7);
        PlaceData.RATING = Float.parseFloat(cursor.getString(5));
        PlaceData.PRICING = Integer.parseInt(cursor.getString(8));
        PlaceData.TYPES = typy;
        //PlaceData.LOCALE;
        PlaceData.LATLNG = new LatLng(Double.parseDouble(cursor.getString(10)), // dlugosc
                Double.parseDouble(cursor.getString(11)));

        return true;
    }
    // pobranie wszystkich notatek
    public List<PlaceData> getAllNotes() {

        List<PlaceData> placeList = new ArrayList<PlaceData>();

        // zapytanie SQL
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        // inny sposób wywołania zapytania
        Cursor cursor = db.rawQuery(selectQuery, null);

        // pętla przez wszystkie elementy z dodzwaniem ich do listy
        if (cursor.moveToFirst()) {
            do {
                List<Integer> typy = new ArrayList<Integer>();
                typy.add(1);

                PlaceData place = new PlaceData(
                        Integer.parseInt(cursor.getString(0)), // ID
                        cursor.getString(1),
                        cursor.getString(2), // Nazwa
                        cursor.getString(3), //Adres
                        cursor.getString(4), //data
                        Float.parseFloat(cursor.getString(5)), // rating
                        cursor.getString(6), // numer telefonu
                        cursor.getString(7), // uri
                        Integer.parseInt(cursor.getString(8)), // pricing
                        typy, // types
                        new LatLng(Double.parseDouble(cursor.getString(10)), // dlugosc
                                Double.parseDouble(cursor.getString(11))) // szerokosc
                );

                placeList.add(place);

            } while (cursor.moveToNext());
        }

        // return contact list
        return placeList;
    }

    public Cursor fetchAllNotes() {
        return db.query(TABLE_PLACES, null, null, null, null, null,
                SORT+" "+ORDER);
    }

    // pobranie liczbe zapisanych miejsc w ulubionych
    public int getPlacesCount() {

        String countQuery = "SELECT  * FROM " + TABLE_PLACES;
        Cursor cursor = db.rawQuery(countQuery, null);

        // zwracamy liczbę wierszy
        return cursor.getCount();
    }

    // aktualizacja notatki
    /*public int updateNote(Notatka notatka) {

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, notatka.getTytul());
        values.put(KEY_BODY, notatka.getTresc());
        values.put(KEY_PRIORITY, notatka.getPriority());

        // aktualizacja wiersza
        return db.update(TABLE_PLACES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(notatka.getId()) });
    }*/

    // usunięcie pojedynczego miejsca
    public void deletePlace(PlaceData place) {

        db.delete(TABLE_PLACES, KEY_ID + " = ?",
                new String[] { String.valueOf(place.getId()) });

    }

    // usunięcie pojedynczego miejsca po ID
    public void deletePlace(String id) {

        db.delete(TABLE_PLACES, KEY_G_ID + " = ?",
                new String[] { id });

    }
    //usuwanie po zwyklym id
    public void deletePlace(long id) {

        db.delete(TABLE_PLACES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

    }
}