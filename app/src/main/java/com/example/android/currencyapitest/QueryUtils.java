package com.example.android.currencyapitest;

import android.text.TextUtils;
import android.util.Log;

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

/**
 * Helper methods related to requesting and receiving currency data from Fixer.IO
 */
public class QueryUtils {

    //Tag for the log messages
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query Fixer.IO and return an {@link List<Currency>} object to represent a single exchange rate.
     */
    public static List<Currency> fetchCurrencyData(String requestUrl) {
        //Create a URL object
        URL url = createUrl(requestUrl);

        //Perform a HTTP request to the URL and receive a JSON response back
        //Initialise variable
        String jsonResponse = null;

        //Try the creation of a JSON response from the URL, if not catch the exception
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            //Log the error
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        //Extract the relevant fields from the JSON response and create a List<Currency> object
        List<Currency> currency =  extractFeaturesFromJson(jsonResponse);

        //Return the List<Currency>
        return currency;
    }

    /**
     * @return a new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        //Initialise variable
        URL url = null;

        //Try the creation of a URL, if not catch the exception
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            //Log the error
            Log.e(LOG_TAG ,"Error with creating URL", e);
        }
        //Return the successful URL
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     * @throws IOException if something goes wrong
     */
    private static String makeHttpRequest(URL url) throws IOException {
        //Initialise variable
        String jsonResponse = "";

        //If the URL is null, then return early with an empty string
        if (url == null) {
            return jsonResponse;
        }

        //Initialise variables
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        //Try connecting to the URL and reading the response, else catch the exception
        try {
            //Open the connection to the URL
            urlConnection = (HttpURLConnection) url.openConnection();

            //Set timeouts so if the connections expire before data is available, an error is thrown
            //Makes ure the suer is never waiting for long periods of time if there is no data
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            //Set the request method, as data wants to be retrieved
            urlConnection.setRequestMethod("GET");

            //Make the connection
            urlConnection.connect();

            //If the request was successful (response code 200), then read the input and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                //Log the error
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            //Log the error
            Log.e(LOG_TAG, "Problem retrieving the JSON results", e);
        } finally {
            //As long as there is an open connection
            if (urlConnection != null) {
                //Close the URL connection
                urlConnection.disconnect();
            }
            //As long as the inputStream is open
            if (inputStream != null) {
                //Close the inputStream
                inputStream.close();
            }
        }
        //Return the JSON response
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        //Initialise variables / create a new String Builder (S.B.)
        //S.B. builds one String bit by bit instead of appending more information each time through other variables
        StringBuilder output = new StringBuilder();

        //As long as the inputStream is not empty
        if (inputStream != null) {
            //Create a new InputStreamReader, using the inputStream and "UTF-8" character set
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            //Create a buffered reader, which stores information about the characters around each character
            BufferedReader reader = new BufferedReader(inputStreamReader);

            //Get the line of text
            String line = reader.readLine();

            //While the line isn't empty
            while (line != null) {
                //Append the line to the output
                output.append(line);
                //Goto the next line
                line = reader.readLine();
            }
        }
        //Return the completed output in String format
        return output.toString();
    }

    /**
     * @return an {@link List<Currency>} item by parsing out information
     * about the exchange rates from the input jsonResponse string.
     */
    private static List<Currency> extractFeaturesFromJson(String jsonResponse) {
        //If the JSON String is empty or null, then return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        //Create an empty ArrayList that we can start adding exchange rates to
        List<Currency> currencies = new ArrayList<>();

        //Try to parse the jsonResponse, else catch the JSONException
        try {
            //Get the root of the JSON string
            JSONObject root = new JSONObject(jsonResponse);

            //Get the base currency
            String baseCurrency = root.getString("base");
            Log.i("base currency", baseCurrency);

            //Get the date of the exchange rate
            String exchangeDate = root.getString("date");
            Log.i("exchange date", exchangeDate);

            //Get the "rates" array as an object
            JSONObject rates = root.getJSONObject("rates");

            //For loop that cycles through all of the rates objects
            for (int i = 0; i < rates.length(); i++) {
                //Get the object key (country code)
                String objectKey = rates.names().getString(i);

                //Get the exchange rate value associated with that object
                double keyValue = rates.getDouble(objectKey);

                //Add those details along with the above date and base to the array
                currencies.add(new Currency(exchangeDate, baseCurrency, objectKey, keyValue));
            }
        } catch (JSONException e) {
            //Log the error
            Log.e(LOG_TAG, "Problems parsing the currency JSON results", e);
        }
        return currencies;
    }
}
