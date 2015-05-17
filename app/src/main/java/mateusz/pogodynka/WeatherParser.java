package mateusz.pogodynka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherParser {

    public WeatherParser() {}

    public WeatherParser(String data) {
        this.data = data;
    }

    protected String data;

    public Weather parseWeather() throws JSONException {
        Weather weather = new Weather();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);
        weather.city = getString("name", jObj);

        JSONObject sysObj = getObject("sys", jObj);
        weather.country = getString("country", sysObj);

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray("weather");
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.icon = (getString("icon", JSONWeather));


        JSONObject mainObj = getObject("main", jObj);
        weather.temperature = (getFloat("temp", mainObj));
        weather.humidity = getInt("humidity", mainObj);

        return weather;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
