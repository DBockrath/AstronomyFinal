package com.astronomyfinal.diboc;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    TextView txtTemp;
    TextView txtCity;
    TextView txtDescription;
    TextView txtDate;
    TextView txtClouds;
    TextView txtHumidity;
    TextView txtResult;
    TextView txtTempMinMax;

    ImageView imageView;

    String ZIP_CODE = "14450";
    String COUNTRY = "us";
    String API_KEY = "99673b5a3da186cedb0298ca712213cb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp = (TextView)findViewById(R.id.txt_temp);
        txtCity = (TextView)findViewById(R.id.txt_city);
        txtDescription = (TextView)findViewById(R.id.txt_description);
        txtDate = (TextView)findViewById(R.id.txt_date);
        txtClouds = (TextView)findViewById(R.id.txt_clouds);
        txtHumidity = (TextView)findViewById(R.id.txt_humidity);
        txtResult = (TextView)findViewById(R.id.txt_result);
        txtTempMinMax = (TextView)findViewById(R.id.txt_temp_min_max);

        imageView = (ImageView)findViewById(R.id.image_view);


        findWeather();

    }

    public void findWeather() {

        String url = ("https://api.openweathermap.org/data/2.5/weather?zip=" + ZIP_CODE + "," + COUNTRY + "&appid=" + API_KEY);

            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {

                        JSONObject main_object = response.getJSONObject("main");
                        JSONObject clouds_object = response.getJSONObject("clouds");

                        JSONArray array = response.getJSONArray("weather");
                        JSONObject object = array.getJSONObject(0);

                        int temp = main_object.getInt("temp");
                        int temp_min = main_object.getInt("temp_min");
                        int temp_max = main_object.getInt("temp_max");

                        String humidity = String.valueOf(Math.round(main_object.getDouble("humidity")));
                        String all = String.valueOf(Math.round(clouds_object.getDouble("all")));
                        String city = response.getString("name");
                        String description = object.getString("description");
                        String id = object.getString("id");

                        long longFahrenheit = Math.round(((1.8) * (temp - 273.15)) + 34);
                        String fahrenheit = String.valueOf(longFahrenheit);

                        long longFahrenheitMin = Math.round(((1.8) * (temp_min - 273.15)) + 34);
                        long longFahrenheitMax = Math.round(((1.8) * (temp_max - 273.15)) + 34);
                        txtTempMinMax.setText(String.valueOf(longFahrenheitMin + "°  /  " + longFahrenheitMax + "°"));

                        if (Integer.valueOf(humidity) <= 40 && Integer.valueOf(humidity) >= 15 && Integer.valueOf(all) == 0 && longFahrenheit <= 75 && longFahrenheit <= 50) {

                            txtResult.setText("Today is an Ideal Day for Astrophotography");

                        } else if (Integer.valueOf(humidity) <= 10 && Integer.valueOf(humidity) >= 60 && Integer.valueOf(all) <= 10 && longFahrenheit <= 100 && longFahrenheit >= 10) {

                            txtResult.setText("Today is not Ideal for Astrophotography");

                        } else {

                            txtResult.setText("Today is not a Good Day for Astrophotography");

                        }

                        txtTemp.setText(fahrenheit + "°");
                        txtCity.setText(city);
                        txtDescription.setText(description);
                        txtClouds.setText("Cloud Cover: " + all + "%");
                        txtHumidity.setText("Humidity: " + humidity + "%");

                        Calendar calendar = new GregorianCalendar();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM  dd,  YYYY");
                        String formatted_date = sdf.format(calendar.getTime());

                        txtDate.setText(formatted_date);

                        double actualID = Integer.parseInt(id);
                        Drawable mDrawable;

                        if ((Math.floor(actualID / 100)) == 2) {

                            mDrawable = getResources().getDrawable(R.drawable.simple_weather_icon_27);
                            imageView.setImageDrawable(mDrawable);

                        } else if ((Math.floor(actualID / 100)) == 3) {

                            mDrawable = getResources().getDrawable(R.drawable.simple_weather_icon_21);
                            imageView.setImageDrawable(mDrawable);

                        } else if ((Math.floor(actualID / 100)) == 5) {

                            mDrawable = getResources().getDrawable(R.drawable.simple_weather_icon_23);
                            imageView.setImageDrawable(mDrawable);

                        } else if ((Math.floor(actualID / 100)) == 6) {

                            mDrawable = getResources().getDrawable(R.drawable.simple_weather_icon_25);
                            imageView.setImageDrawable(mDrawable);

                        } else if ((Math.floor(actualID / 100)) == 7) {

                            mDrawable = getResources().getDrawable(R.drawable.simple_weather_icon_09);
                            imageView.setImageDrawable(mDrawable);

                        } else if (actualID == 800) {

                            mDrawable = getResources().getDrawable(R.drawable.simple_weather_icon_01);
                            imageView.setImageDrawable(mDrawable);

                        } else if ((Math.floor(actualID / 10)) == 80 && actualID != 800) {

                            mDrawable = getResources().getDrawable(R.drawable.simple_weather_icon_06);
                            imageView.setImageDrawable(mDrawable);

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                }

            });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

    }

}