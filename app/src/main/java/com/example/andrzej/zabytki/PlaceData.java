package com.example.andrzej.zabytki;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class PlaceData {

    /* Pola statyczne, przekazywane tylko nowej aktywnosc/dodwania notatek */
    public static String ID;
    public static String ADDRESS;
    public static String NAME;
    public static String PHONE_NUMBER;
    public static String URI;
    public static float RATING;
    public static int PRICING;
    public static List<Integer> TYPES;
    public static Locale LOCALE;
    public static LatLng LATLNG;

    /* Pola prywatne obiektu */
    private int id;
    private String g_id;
    private String address;
    private String date;
    private String name;
    private String phoneNumber;
    private String uri;
    private float rating;
    private int pricing;
    private List<Integer> types;
    private Locale locale;
    private LatLng latLng;

    public PlaceData(int id,
                     String g_id,
                     String name,
                     String address,
                     String date,
                     float rating,
                     String phoneNumber,
                     String uri,

                     int pricing,
                     List<Integer> types,
                     //Locale locale,
                     LatLng latLng) {
        this.g_id = g_id;
        this.id = id;
        this.address = address;
        this.date = date;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.uri = uri;
        this.rating = rating;
        this.pricing = pricing;
        this.types = types;
        //this.locale = locale;
        this.latLng = latLng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPricing() {
        return pricing;
    }

    public void setPricing(int pricing) {
        this.pricing = pricing;
    }

    public List<Integer> getTypes() {
        return types;
    }

    public void setTypes(List<Integer> types) {
        this.types = types;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
