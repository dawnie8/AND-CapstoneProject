package ipomoea.quotes.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class QuotesContract {

    private static final String AUTHORITY = "ipomoea.quotes";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String PATH_QUOTES = "quotes";

    public static final class QuoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUOTES).build();

        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "quotes";
        public static final String COLUMN_QUOTE_TEXT = "quoteText";
        public static final String COLUMN_AUTHOR = "author";

    }
}