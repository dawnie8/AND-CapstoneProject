package ipomoea.quotes.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import ipomoea.quotes.model.Quote;

public class XmlParser {

    private static final String KEY_ROOT = "idezetek";
    private static final String KEY_PARENT = "idezet";
    private static final String KEY_QUOTE = "idezetszoveg";
    private static final String KEY_AUTHOR = "szerzo";

    public static String convertXmlToJson(String string) {

        XmlToJson xmlToJson = new XmlToJson.Builder(string).build();

        return xmlToJson.toString();
    }

    public static Quote extractFeatureFromJson(String quoteJSON) {

        String quote_text = "";
        String author = "";
        Quote quote = null;

        if (TextUtils.isEmpty(quoteJSON)) {
            return null;
        }
        try {

            JSONObject baseJsonObject = new JSONObject(quoteJSON);

            JSONObject root = baseJsonObject.getJSONObject(KEY_ROOT);

            JSONObject parent = root.getJSONObject(KEY_PARENT);


            if (parent.has(KEY_QUOTE)) {
                quote_text = parent.optString(KEY_QUOTE);
            }

            if (parent.has(KEY_AUTHOR)) {
                author = parent.optString(KEY_AUTHOR);
            }

            quote = new Quote(quote_text, author);

        } catch (JSONException e) {
            Log.e("XmlParser", "Problem with JSON parsing", e);
        }

        return quote;
    }
}