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

import com.google.android.gms.maps.model.LatLng;

public class DatabaseHandler extends SQLiteOpenHelper {

    // stałe do bazy
    // wersja bazy
    private static final int DATABASE_VERSION = 1;

    // nazwa bazy
    private static final String DATABASE_NAME = "bazadev.db";

    // tabela z ulubionymi miejscami
    private static final String TABLE_PLACES = "places";

    // nazwy kolumn naszej tabeli
    public static final String KEY_ID = "_id";
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
                KEY_NAME +  " TEXT," +
                KEY_ADDRESS + " TEXT," +
                KEY_DATE + " DATETIME," +
                KEY_RATING + " REAL," +
                KEY_PHONE_NUMBER + " TEXT," +
                KEY_URI + " TEXT," +
                KEY_PRICING + " INT," +
                KEY_TYPES + " TEXT," +
                KEY_LONGITUDE + " TEXT" +
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
    public void addPlace(PlaceData place) {

        ContentValues values = new ContentValues();

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

    // pobranie pojedynczej notatki
    public PlaceData getPlace(int id) {

        // zamiast new String[] { KEY_ID, KEY_TITLE, KEY_BODY }
        // możemy użyć null (wszystkie kolumny)
        Cursor cursor = db.query(TABLE_PLACES, null,
                KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        /*
        KEY_ID
        KEY_NAME +  " TEXT," +
        KEY_ADDRESS + " TEXT," +
        KEY_DATE + " DATETIME," +
        KEY_RATING + " REAL," +
        KEY_PHONE_NUMBER + " TEXT," +
        KEY_URI + " TEXT," +
        KEY_PRICING + " INT," +
        KEY_TYPES + " TEXT," +
        KEY_LONGITUDE + " TEXT" +
        KEY_LATITUDE + " TEXT" +*/
        List<Integer> typy = new ArrayList<Integer>();
        typy.add(1);
        PlaceData place = new PlaceData(
                Integer.parseInt(cursor.getString(0)), // ID
                cursor.getString(1), // Nazwa
                cursor.getString(2), //Adres
                cursor.getString(3), //data
                Float.parseFloat(cursor.getString(4)), // rating
                cursor.getString(5), // numer telefonu
                cursor.getString(6), // uri
                Integer.parseInt(cursor.getString(7)), // pricing
                typy, // types
                new LatLng(Double.parseDouble(cursor.getString(9)), // dlugosc
                           Double.parseDouble(cursor.getString(10))) // szerokosc
        );
        // zwracamy miejsce
        return place;
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
                        cursor.getString(1), // Nazwa
                        cursor.getString(2), //Adres
                        cursor.getString(3), //data
                        Float.parseFloat(cursor.getString(4)), // rating
                        cursor.getString(5), // numer telefonu
                        cursor.getString(6), // uri
                        Integer.parseInt(cursor.getString(7)), // pricing
                        typy, // types
                        new LatLng(Double.parseDouble(cursor.getString(9)), // dlugosc
                                Double.parseDouble(cursor.getString(10))) // szerokosc
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
    public void deletePlace(long id) {

        db.delete(TABLE_PLACES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

    }

}