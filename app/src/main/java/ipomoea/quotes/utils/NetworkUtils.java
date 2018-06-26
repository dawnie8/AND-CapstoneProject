package ipomoea.quotes.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import ipomoea.quotes.BuildConfig;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.citatum.hu/idezet.php?f=";
    private static final String PARAM_API_KEY = "&j=";
    private final static String USERNAME = BuildConfig.USERNAME;
    private final static String API_KEY = BuildConfig.API_KEY;

    private NetworkUtils() {
    }

    public static URL buildUrl() {
        URL url = null;
        try {
            url = new URL(BASE_URL + USERNAME + PARAM_API_KEY + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}