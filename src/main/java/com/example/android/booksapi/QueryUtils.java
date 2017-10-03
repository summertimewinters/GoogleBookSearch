package com.example.android.booksapi;

import android.util.Log;

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

import static com.example.android.booksapi.BooksDisplay.LOG_TAG;

/**
 * Parse String for book data
 */

public final class QueryUtils {
    private QueryUtils() {
        //An empty private constructor makes sure that the class is not going to be initialised.
    }

    public static ArrayList<BooksData> fetchBooks(String requestURL) {
        Log.i(LOG_TAG, "fetchBooks Loader");

        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        ArrayList<BooksData> booksDetail = extractBooks(jsonResponse);
        return booksDetail;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Grab input stream and convert into a readable String
     */
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

    /**
     * Parse books data and add it to an array
     */
    public static ArrayList<BooksData> extractBooks(String booksJSON) {

        // Create an empty ArrayList to add books
        ArrayList<BooksData> books = new ArrayList<>();

        // Parse JSON data to extract appropriate books characteristics
        try {
            //Create variables to capture parsed data
            String booksTitle = null;
            String booksAuthors = null;
            String booksPublishedDate = null;
            String booksDescription = null;
            String booksCoverImageURL = null;
            String booksInfoURL = null;

            JSONObject booksJsonObj = new JSONObject(booksJSON);
            JSONObject booksItemsJsonObject = new JSONObject(booksJSON);

            //Find the "items" and parse the array
            if (booksJsonObj.has("items")) {
                JSONArray booksItemsJsonArray = booksJsonObj.getJSONArray("items");
                for (int i = 0; i < booksItemsJsonArray.length(); i++) {
                    booksItemsJsonObject = booksItemsJsonArray.getJSONObject(i);


                    //Find "volume info" key within items and parse
                    JSONObject booksVolumesJsonObject = booksItemsJsonObject.getJSONObject("volumeInfo");

                    //Store title from title key
                    booksTitle = booksVolumesJsonObject.getString("title");

                    //Determine if the authors key exists, and if so, parse
                    if (booksVolumesJsonObject.has("authors")) {
                        JSONArray booksAuthorJsonArray = booksVolumesJsonObject.getJSONArray("authors");
                        for (int j = 0; j < booksAuthorJsonArray.length(); j++) {
                            booksAuthors = booksAuthorJsonArray.getString(j);
                            if (j > 1) {
                                booksAuthors = booksAuthors + " et al.";
                            }
                        }
                    }

                    //Get published date
                    booksPublishedDate = booksVolumesJsonObject.getString("publishedDate");

                    //Get short description
                    //JSONObject searchInfoDescription = booksItemsJsonObject.getJSONObject("searchInfo");
                    if (booksVolumesJsonObject.has("description")) {
                        booksDescription = booksVolumesJsonObject.getString("description");
                    }

                    //Get cover image URL
                    JSONObject imageJsonObject = booksVolumesJsonObject.getJSONObject("imageLinks");
                    booksCoverImageURL = imageJsonObject.getString("thumbnail");

                    //Get url link
                    booksInfoURL = booksVolumesJsonObject.getString("infoLink");

                    //Add parsed variables to bookData
                    books.add(new BooksData(booksTitle, booksAuthors, booksPublishedDate, booksDescription, booksCoverImageURL, booksInfoURL));
                }
            }
        } catch (JSONException e) {
            // If an error is thrown, catch exception so the app doesn't crash. Print a log message
            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        }

        // Return the list of books
        return books;
    }
}
