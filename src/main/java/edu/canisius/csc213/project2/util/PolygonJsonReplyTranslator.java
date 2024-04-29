package edu.canisius.csc213.project2.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.canisius.csc213.project2.quotes.*;


import java.io.IOException;

public class PolygonJsonReplyTranslator {

    public StockQuote translateJsonToFinancialInstrument(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // Extract necessary information from the JSON response
        String symbol = rootNode.path("ticker").asText();
        JsonNode resultsNode = rootNode.path("results").get(0); // Assuming results is an array
        double closePrice = resultsNode.path("c").asDouble(); // Adjust according to your JSON structure
        double highestPrice = resultsNode.path("h").asDouble();
        double lowestPrice = resultsNode.path("l").asDouble();
        int numberOfTransactions = resultsNode.path("n").asInt();
        double openPrice = resultsNode.path("o").asDouble();
        long timestamp = resultsNode.path("t").asLong();
        double tradingVolume = resultsNode.path("v").asDouble();

        // Create and return a StockQuote object
        return new StockQuote(symbol, closePrice, highestPrice, lowestPrice,
                numberOfTransactions, openPrice, timestamp, tradingVolume);
    }
    
}
