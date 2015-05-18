package mateusz.pogodynka;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;


public class MainActivity extends ListActivity {

    private static final String PREFERENCES_NAME = "myPreferences";

    private SharedPreferences preferences;

    private String PreferencesTextField;
    private TextView cityText;
    private TextView temp;

    private TextView hum;
    private ImageView imgView;

    public boolean weatherView = true;
    public RelativeLayout weather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int orientation = getScreenOrientation();

        if(orientation == 2)
            setContentView(R.layout.list_horizontal);
        else
            setContentView(R.layout.list);



        final Continent continent = new Continent();
        final Country country = new Country();
        final City city = new City();

        final List<String> list = continent.GenerateContinents();
        final List<String> Continents = continent.GenerateContinents();

        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, list);

        weather = (RelativeLayout) findViewById(R.id.weather);
        if(orientation != 2)
            weather.setVisibility(View.GONE);

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                boolean search = true;

                for(String s : Continents) {
                    if(position == list.indexOf(s)){
                        adapter.notifyDataSetChanged();
                        continent.GetCountry(list, s);
                        search = false;
                        break;
                    }
                    else
                        search = true;
                }

                adapter.notifyDataSetChanged();

                if(search) {

                    for(String c : list) {

                        //Kliknięcie w miasto
                        if(position == list.indexOf(c) && (city.getCities().contains(list.get(position)))){
                            weather.setVisibility(View.VISIBLE);
                            if(orientation != 2)
                                weatherView = false;

                            //TUTAJ PODŁĄCZYC API Z POGODA
                            String location = (String) ((TextView) view).getText();

                            Log.i("miasto ", location);
                            PreferencesTextField = location;

                            preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);

                            cityText = (TextView) findViewById(R.id.cityText);
                            temp = (TextView) findViewById(R.id.temp);
                            hum = (TextView) findViewById(R.id.hum);
                            imgView = (ImageView) findViewById(R.id.condIcon);

                            boolean internet = isNetworkAvailable();

                            if(internet){
                                JSONWeatherTask task = new JSONWeatherTask();
                                task.execute(new String[]{location});
                            }
                            else{
                                try {
                                    String data = restoreCache(location);
                                    Log.e("CACHE", data);
                                    if(data == ""){
                                        hideWeather();
                                        Toast.makeText(getApplicationContext(), "Brak połączenia internetowego oraz cache.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        WeatherParser weatherParser = new WeatherParser(data);
                                        Weather weather = weatherParser.parseWeather();
                                        OpenWeatherClient openWeatherClient = new OpenWeatherClient();
                                        weather.iconData = openWeatherClient.getImage(weather.icon);
                                        displayWeather(weather);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        //Kliknięcie w państwo
                        else if(position == list.indexOf(c)){
                            System.out.println("Wyświetl miasta: " + c);
                            adapter.notifyDataSetChanged();
                            country.GetCity(list, c);
                            break;
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });

        setListAdapter(adapter);
    }


    @Override
    public void onBackPressed() {

        if (weatherView) {
            super.onBackPressed();
            //additional code
        } else {
            weatherView = true;
            weather.setVisibility(View.GONE);
        }

    }

    public int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void hideWeather(){
        temp.setVisibility(View.INVISIBLE);
        hum.setVisibility(View.INVISIBLE);
        imgView.setVisibility(View.INVISIBLE);
    }

    private void displayWeather(Weather weather){
        if (weather.iconData != null && weather.iconData.length > 0) {
            Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
            imgView.setImageBitmap(img);
        }
        temp.setVisibility(View.VISIBLE);
        temp.setVisibility(View.VISIBLE);
        imgView.setVisibility(View.VISIBLE);

        cityText.setText(weather.city + "," + weather.country);
        temp.setText("" + Math.round((weather.temperature - 273.15)) + " C");
        hum.setText("" + weather.humidity + "%");
    }

    private void saveCache(String cache) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(PreferencesTextField, cache);
        preferencesEditor.commit();
    }

    private String restoreCache(String location) {
        String textFromPreferences = preferences.getString(location, "");
        return textFromPreferences;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();

            OpenWeatherClient openWeatherClient = new OpenWeatherClient();
            String data = openWeatherClient.getWeatherData(params[0]);

            WeatherParser weatherParser = new WeatherParser(data);

            saveCache(data);

            try {
                weather = weatherParser.parseWeather();

                weather.iconData = OpenWeatherClient.getImage(weather.icon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            displayWeather(weather);
        }
    }
}