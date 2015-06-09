package com.example.andrzej.zabytki;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.UserRecoverableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PlaceActivity extends Activity implements onResponse {
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


        name = (TextView) findViewById(R.id.textView);
        addres = (TextView) findViewById(R.id.textView2);
        number = (TextView) findViewById(R.id.textView3);
        url = (TextView) findViewById(R.id.textView4);
        addOrDel = (Button) findViewById(R.id.button2);
        rating = (RatingBar) findViewById(R.id.ratingBar);

        DatabaseHandler db = new DatabaseHandler(this);

        if (db.getPlace(PlaceData.ID)) {
            addOrDel.setText("Usuń z ulubionych");
        } else {
            addOrDel.setText("Dodaj do ulubionych");
        }

        if (PlaceData.NAME == null) {
            onBackPressed();
            Context context = getApplicationContext();
            CharSequence text = "Przepraszamy ale wystąpił problem";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            name.setText(PlaceData.NAME);
            addres.setText(PlaceData.ADDRESS);

            if (PlaceData.PHONE_NUMBER != null) {
                number.setText(PlaceData.PHONE_NUMBER);
                // number.setClickable(true);
            } else
                number.setText("Brak Numeru Tel");
            // number.setClickable(false);
            if (PlaceData.URI != null) {
                url.setText(PlaceData.URI);
                url.setClickable(true);
            } else {
                url.setText("Brak Url");
                url.setClickable(false);
            }

            rating.setRating(PlaceData.RATING);


            String URL = null;
            try {
                URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + URLEncoder.encode(PlaceData.NAME + " " + PlaceData.ADDRESS, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            AsyncFetch parkingInfoFetch = new AsyncFetch(this);
            parkingInfoFetch.setOnResponse(this);
            parkingInfoFetch.execute(URL);
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

        String latitude = Double.toString(PlaceData.LATLNG.latitude);
        String longitude = Double.toString(PlaceData.LATLNG.longitude);
        String navigateTo = "google.navigation:q=" + latitude + "," + longitude;
        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
        Uri gmmIntentUri = Uri.parse(navigateTo);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void addToFavourite(View view) {
        DatabaseHandler db = new DatabaseHandler(this);
        addOrDel = (Button) findViewById(R.id.button2);

        if (db.getPlace(PlaceData.ID) == false) {
            Context context = getApplicationContext();
            CharSequence text = "Obiekt został dodany do ulubionych";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            // dodawanie do bd
            addOrDel.setText("Usuń z ulubionych");
            db.addPlace();
        } else {
            //delete
            db.deletePlace(PlaceData.ID);

            Context context = getApplicationContext();
            CharSequence text = "Obiekt został usunięty z ulubionych";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            addOrDel.setText("Dodaj do ulubionych");
        }
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

    @Override
    public void onResponse(JSONObject object) {


        try {
            //Log.d("TEST", "aa"+ object.getString("responseData"));
            Pattern p = Pattern.compile("url\":\"([^\"]+)\"");
            Matcher m = p.matcher(object.getString("responseData"));
            while (m.find()) {
                String url = m.group(1).replace("\\","");
                new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                        .execute(url);

                break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
class AsyncFetch extends AsyncTask<String, Void, JSONObject> {

    public AsyncFetch(Context context) {
        this.context = context;
    }

    private Context context;
    private JSONObject jsonObject;
    private onResponse onResponse;

    public onResponse getOnResponse() {
        return onResponse;
    }

    public void setOnResponse(onResponse onResponse) {
        this.onResponse = onResponse;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected JSONObject doInBackground(String... params) {
        try {

            HttpGet get = new HttpGet(params[0]);
            HttpClient client = new DefaultHttpClient();

            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            jsonObject = new JSONObject(result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        this.onResponse.onResponse(result);
    }
}
interface onResponse {
    public void onResponse(JSONObject object);
}
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}