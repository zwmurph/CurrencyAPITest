package com.example.android.currencyapitest;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Currency>> {

    //URL that retrieves the JSON response from the API
    public static final String REQUEST_URL = "https://api.fixer.io/latest?base=GBP";

    //Constant value for the currency loader ID. This is only really used if you're using multiple loaders.
    private static final int CURRENCY_LOADER_ID = 1;

    //Global instance of the CurrencyAdapter, so it can be used in multiple methods in this class
    private CurrencyAdapter mAdapter;

    //Global instance of the TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    //Global instance of the ProgressBar, so it can be used in multiple methods in this class
    private ProgressBar mProgressBar;

    /**
     * OnCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if the user is connected to the Internet
        if (!checkInternetConnectivity()) {
            //Hide the loading spinner
            mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            mProgressBar.setVisibility(View.GONE);

            //Let the user know there is no Internet connection
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText(R.string.no_connectivity);
        } else {
            //Create an object constructor for the ListView
            ListView currencyListView = (ListView) findViewById(R.id.list);

            //Find and set the empty text view as the empty view for the layout
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            currencyListView.setEmptyView(mEmptyStateTextView);

            //Creates an adapter for the words to use, appends the array of words to the adapter,
            //the adapter is responsible for making a View for each item in the data set
            mAdapter = new CurrencyAdapter(this, new ArrayList<Currency>());

            //Sets the adapter method on the ListView
            //so the list can be populated in the user interface
            currencyListView.setAdapter(mAdapter);

            //Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //Initialize the loader. Pass in the int ID constant defined above and pass in null for
            //the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            //because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(CURRENCY_LOADER_ID, null, this);
        }
    }

    /**
     * Method that checks if the user is connected to the Internet
     *
     * @return true or false
     */
    private boolean checkInternetConnectivity() {
        //Create a connectivity manager
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get the active network's network info, and return a boolean value
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Method for the creation of the loader, pass in:
     * The Loader {@param id} to be used
     * The {@param args} (arguments) the Loader should take
     *
     * @return a new instance of the Loader
     */
    @Override
    public Loader<List<Currency>> onCreateLoader(int i, Bundle bundle) {
        return new CurrencyLoader(this);
    }

    /**
     * Once the loader has finished, execute this method with:
     * The {@param loader} to be used
     * The {@param result} from the Loader creation
     */
    @Override
    public void onLoadFinished(Loader<List<Currency>> loader, List<Currency> currencies) {
        //Hide the loading spinner
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mProgressBar.setVisibility(View.GONE);

        //Set the empty state text to display "No exchange rates found."
        mEmptyStateTextView.setText(R.string.no_dataResults);

        //Clear the adapter of previous results
        mAdapter.clear();

        //If there is a valid list of Currency's, then add them to the adapter's dataset
        //This triggers the ListView to update
        if (currencies != null && !currencies.isEmpty()) {
            mAdapter.addAll(currencies);
        }
    }

    /**
     * If the Loader is reset (i.e. through orientation change), handle that in this method
     * The {@param loader} to be used
     */
    @Override
    public void onLoaderReset(Loader<List<Currency>> loader) {
        //Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
