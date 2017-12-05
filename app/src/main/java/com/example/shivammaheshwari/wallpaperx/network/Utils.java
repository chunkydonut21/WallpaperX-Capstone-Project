package com.example.shivammaheshwari.wallpaperx.network;


import android.util.Log;

import com.example.shivammaheshwari.wallpaperx.model.Walls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String LOG_TAG = "Utils";

    private static URL createUrl(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return url;
    }

    private static String httpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String response = "";
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);// milliseconds
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving info", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return response;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Walls> extractingJsonData(String jsonString) throws JSONException {
        List<Walls> walls = new ArrayList<>();
        String imageId, imageWidth, imageHeight, imageUrl, thumbnailUrl, imageUrlPage;


        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray mArray = jsonObject.getJSONArray("wallpapers");

        for (int i = 0; i < mArray.length(); i++) {
            imageId = mArray.getJSONObject(i).getString("id");
            imageWidth = mArray.getJSONObject(i).getString("width");
            imageHeight = mArray.getJSONObject(i).getString("height");
            imageUrl = mArray.getJSONObject(i).getString("url_image");
            thumbnailUrl = mArray.getJSONObject(i).getString("url_thumb");
            imageUrlPage = mArray.getJSONObject(i).getString("url_page");


            walls.add(new Walls(imageId, imageWidth, imageHeight, imageUrl, thumbnailUrl, imageUrlPage));

        }
        return walls;
    }


    public static List<Walls> fetchData(String stringUrl) throws JSONException, IOException {

        URL url = createUrl(stringUrl);
        String jsonResponse = httpRequest(url);
        List<Walls> listOfMovies = extractingJsonData(jsonResponse);

        return listOfMovies;

    }

}
