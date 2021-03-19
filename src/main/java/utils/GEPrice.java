package utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GEPrice {
    private static Map<Character, Integer> suffixMultipliers = new HashMap<>();

    static {
        suffixMultipliers.put('k', 1000);
        suffixMultipliers.put('m', 1_000_000);
        suffixMultipliers.put('b', 1_000_000_000);
    }

    public Optional<Integer> getPrice(final int itemID) throws IOException {
        JSONObject json = readJsonFromUrl("https://secure.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=" + itemID);

        JSONObject item = (JSONObject) json.get("item");

        JSONObject currentPriceData = (JSONObject) item.get("current");

        Object currentPriceObj = currentPriceData.get("price");

        int currentPrice;

        if (currentPriceObj instanceof String) {
            currentPrice = parsePriceStr((String) currentPriceData.get("price"));
        } else if (currentPriceObj instanceof Long) {
            currentPrice = ((Long) currentPriceObj).intValue();
        } else {
            currentPrice = (int) currentPriceObj;
        }

        return Optional.of(currentPrice);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static int parsePriceStr(String priceStr) {
        priceStr = priceStr.trim();
        priceStr = priceStr.replaceAll(",", "");

        char suffix = priceStr.charAt(priceStr.length() - 1);

        if (!suffixMultipliers.containsKey(suffix)) {
            return Integer.parseInt(priceStr);
        }

        String priceNoSuffix = priceStr.substring(0, priceStr.length() - 1);

        double priceDouble = Double.parseDouble(priceNoSuffix);

        priceDouble = priceDouble * suffixMultipliers.get(suffix);

        return (int) priceDouble;
    }
}
