/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USG_URL="https://earthquake.usgs.gov/fdsnws/event/1/" +
            "query?format=geojson&starttime=2017-05-01&endtime=2017-05-20";     // This is the URL or the Web API with the Query.
    public String JSONData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        QuakeAsyncTask quakeAsyncTask=new QuakeAsyncTask();  //Calling the method of the inner class.
        quakeAsyncTask.execute();
        /* Fist the Execute method is called which after completion calls the doInBackground()
            method automatically. After that it calls the onPostExecute() method.
            Remember the Cycle of calling the methods in the Async Task.
            THIS TAKES PLACE IN SEPARATE THREAD.
         */
    }
    public void updateUI(String data){
        JsonParser jsonParser=new JsonParser(data);
        /*
        The internet connection and data
        retriving of the data takes time hence main thread goes and we have to do that in another thread.
         */
        ArrayList<QuakeReport> arrayList=jsonParser.extractEarthquakes();
        // Find a reference to the {@link ListView} in the layout
        CustomAdapter quakeAdapter = new CustomAdapter(this,arrayList);
        ListView listView=(ListView) findViewById(R.id.list);
        listView.setAdapter(quakeAdapter);
    }

    /**
     * This is the inner class. URL is the Input parameter the WEB API and the STRING is the
     * output of the JSON.
     */
    public class QuakeAsyncTask extends AsyncTask<URL,Void,String> {
        @Override
        /**
         * This is the Method which is called first.
         * Remember this method is running in another thread. NOT IN THE MAIN THREAD.
         */
        protected String doInBackground(URL... urls) {
            URL url = createURL(USG_URL);
            try {
                JSONData = makeHTTPRequest(url);
            } catch (IOException e) {
                Log.d(EarthquakeActivity.LOG_TAG, e.toString());
            }
            return JSONData;  // it returns the JSON DATA.
        }

        @Override
        /**
         * This Return type of the doInBackground Method is the Input parameter.
         * This method is called after the doInBackground method is executed.
         */
        protected void onPostExecute(String jsonData) {
            updateUI(jsonData);
        }

        /**
         * This method is Redundant. Eta na korleo hoto. Ami aktu Kaida marlam.
         * @param url
         * @return
         */
        protected URL createURL(String url) {
            URL urll = null;
            try {
                urll = new URL(url);
            } catch (MalformedURLException e) {
                Log.d(EarthquakeActivity.LOG_TAG, e.toString());
            }
            return urll;
        }

        protected String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {  // Converting the raw JSON to String Human Readable.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }  //Converting the STRING BUIDER to STRING.
            return output.toString();
        }

        /**
         * This Method is called from the doInBackground() method.
          * @param url: The WEB API
         * @return: JSON STRING.
         * @throws IOException
         */
        private String makeHTTPRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");   // Connecting in GET mode to get data.
                                                          // if you had to send or upload data then you had to use POST Mode.
                urlConnection.setReadTimeout(10000);    // A wait time for the internet connection
                urlConnection.setConnectTimeout(10000);
                inputStream = urlConnection.getInputStream();  // getting the data.
                jsonResponse = readFromStream(inputStream);   // formatting the JSON input from the internet stream
            } catch (IOException e) {
                Log.d(EarthquakeActivity.LOG_TAG, e.toString());
            } finally {    // Closing the internet connection. ELSE battery would be drained.
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (inputStream != null)
                    inputStream.close();
            }
            return jsonResponse;
        }
    }
}
