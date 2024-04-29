package edu.canisius.csc213.project2.quotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.canisius.csc213.project2.util.*;

public class PolygonStockQuoteProvider implements StockQuoteProvider{

    @Override
    public StockQuote getStockQuote(String stockQuoteEndpoint) throws IOException {
        String json = sendGetRequest(stockQuoteEndpoint);
        PolygonJsonReplyTranslator jft = new PolygonJsonReplyTranslator();
        return jft.translateJsonToFinancialInstrument(json);

    }

    public static String sendGetRequest(String endpointUrl) throws IOException {
        StringBuilder response = new StringBuilder();

        // Open a connection to the endpoint URL
        URL url = new URL(endpointUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method to GET
        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();

        // If the response code is not 200 (OK), throw an IOException
        if (responseCode != 200) {
            throw new IOException("Failed to fetch stock quote. Response code: " + responseCode);
        }

        // Read the response from the API
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // Close the connection
        connection.disconnect();

        return response.toString();
    }
    
    @Override
    public String getEndpointUrl(String symbolName, String date, String apiKey) throws IOException {
        // Validate the date format
        if (!isValidDateFormat(date)) {
            throw new IllegalArgumentException("Invalid date format. Date must be in the format YYYY-MM-DD.");
        }
        return "https://api.polygon.io/v2/aggs/ticker/" + symbolName + "/range/1/day/" + date + "/" + date + "?apiKey=" + apiKey;
    }

    private boolean isValidDateFormat(String date) {
        // Perform validation of date format (e.g., using regular expressions)
        // For simplicity, you can check if the date matches the format YYYY-MM-DD
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }


}
