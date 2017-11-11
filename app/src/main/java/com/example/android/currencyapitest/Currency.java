package com.example.android.currencyapitest;

/**
 * The Currency class that creates and provides methods that return parts of the ArrayList when called
 * by other parts of the app
 */
class Currency {

    //Declare private variables
    private String mDate;
    private String mBase;
    private String mCurrencyKey;
    private double mCurrencyValue;

    /**
     * Create the constructor for this class, a constructor creates an instance of a class
     * This constructor will create an instance of two Strings and a double
     *
     * @param date
     * @param base
     * @param currencyKey
     * @param currencyValue
     */
    public Currency(String date, String base, String currencyKey, double currencyValue) {
        mDate = date;
        mBase = base;
        mCurrencyKey = currencyKey;
        mCurrencyValue = currencyValue;
    }

    /**
     * @return the date that is associated with the results
     */
    public String getDateOfExchange() {
        return mDate;
    }

    /**
     * @return the base currency
     */
    public String getBaseCurrency() {
        return mBase;
    }

    /**
     * @return the currency that the conversion rate applies to
     */
    public String getConvertedCurrency() {
        return mCurrencyKey;
    }

    /**
     * @return the exchange rate that the country is providing
     */
    public double getExchangeRate() {
        return mCurrencyValue;
    }
}
