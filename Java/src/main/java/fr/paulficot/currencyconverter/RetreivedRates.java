package fr.paulficot.currencyconverter;

import java.io.Serializable;
import java.util.HashMap;

public class RetreivedRates implements Serializable {

    /** mode mémoire. */
    private HashMap rates = new HashMap<String, Double>();

    /** mode mémoire. */
    private String base;

    /** mode mémoire. */
    private String date;

    public HashMap getRates() {
        return rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "RetreivedRates{" +
                "rates='" + rates + '\'' +
                ", base='" + base + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
