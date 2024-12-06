package model.service;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter {

    // API-avain ja perus-URL
    private static final String API_KEY = "65a9b705032dba05af37375f";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    // Private constructor to prevent instantiation
    private CurrencyConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Static-metodi, jonka voit kutsua toisesta luokasta ilman olion luontia
    public static double convertCurrency(String base, String target, double amount) {
        double result = 0.0;
        try {
            // Rakennetaan API-kutsu
            String urlStr = BASE_URL + API_KEY + "/latest/" + base;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Luetaan API-vastaus
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Käsitellään JSON-vastaus
            JSONObject json = new JSONObject(content.toString());
            JSONObject rates = json.getJSONObject("conversion_rates");

            // Haetaan kohdevaluutan kurssi
            double exchangeRate = rates.getDouble(target);

            // Lasketaan muunnettu arvo
            result = amount * exchangeRate;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
