package com.example.android.currencyapitest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class CurrencyAdapter extends ArrayAdapter<Currency> {

    /**
     * A custom constructor.
     *
     * @param context    is used to inflate the layout file
     * @param currencies is the data we want to populate into the lists
     */
    public CurrencyAdapter(Activity context, List<Currency> currencies) {
        // This initialises the ArrayAdapter's internal storage for the context and the list.
        // The second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for four TextViews the adapter is not
        // going to use this second argument, so it can be any value.
        super(context, 0, currencies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get the data item for this position
        Currency currentExchange = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_layout, parent, false);
        }

        //Lookup the views for the data population
        TextView dateView = convertView.findViewById(R.id.dateView);
        TextView baseCurrencyView = convertView.findViewById(R.id.baseCurrencyView);
        TextView exchangeCurrencyView = convertView.findViewById(R.id.exchangeCurrencyView);
        TextView exchangeCurrencyRateView = convertView.findViewById(R.id.exchangeCurrencyRateView);

        //Populate data into the template view using the data object
        dateView.setText(currentExchange.getDateOfExchange());
        baseCurrencyView.setText(currentExchange.getBaseCurrency());
        exchangeCurrencyView.setText(currentExchange.getConvertedCurrency());
        exchangeCurrencyRateView.setText("" + currentExchange.getExchangeRate());

        //Return the completed view to render on-screen
        return convertView;
    }
}
