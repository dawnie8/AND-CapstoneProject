package ipomoea.quotes.ui;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;

import ipomoea.quotes.model.Quote;
import ipomoea.quotes.utils.NetworkUtils;
import ipomoea.quotes.utils.XmlParser;

public class QuotesLoader extends AsyncTaskLoader<Quote> {

    private URL quoteRequestUrl = NetworkUtils.buildUrl();
    private Quote loadedQuote;


    public QuotesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (loadedQuote != null) {
            deliverResult(loadedQuote);
        } else {
            forceLoad();
        }

    }

    @Override
    public Quote loadInBackground() {

        try {
            String quoteXML = NetworkUtils.getResponseFromHttpUrl(quoteRequestUrl);
            String quoteJSON = XmlParser.convertXmlToJson(quoteXML);
            return XmlParser.extractFeatureFromJson(quoteJSON);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(Quote data) {
        loadedQuote = data;
        super.deliverResult(data);
    }
}