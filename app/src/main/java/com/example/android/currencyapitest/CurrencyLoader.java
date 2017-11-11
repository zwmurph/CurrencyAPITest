package com.example.android.currencyapitest;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Currency loader task
 */
class CurrencyLoader extends AsyncTaskLoader<List<Currency>> {

    //Log tag that returns the package name for errors
    public static final String LOG_TAG = CurrencyLoader.class.getSimpleName();

    //Global instance of the String URL, so it can be used in multiple methods in this class
    private String mUrl;

    /**
     * The constructor for this class
     */
    public CurrencyLoader(Context context) {
        super(context);
        mUrl = MainActivity.REQUEST_URL;
    }

    /**
     * When the loader is instructed to load
     */
    @Override
    protected void onStartLoading() {
        //Force the load
        forceLoad();
    }

    /**
     * The background process for the loader
     */
    @Override
    public List<Currency> loadInBackground() {
        //Don't perform the request is there are no URLs, or the URL is null
        if (mUrl == null) {
            return null;
        }

        //Get the data for the URL provided & Return the result
        return QueryUtils.fetchCurrencyData(mUrl);
    }
}
