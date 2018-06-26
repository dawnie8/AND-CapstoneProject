package ipomoea.quotes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuotesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite_quotes.db";

    private static final int DATABASE_VERSION = 1;

    public QuotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_QUOTES_TABLE = "CREATE TABLE " + QuotesContract.QuoteEntry.TABLE_NAME + " (" +
                QuotesContract.QuoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuotesContract.QuoteEntry.COLUMN_QUOTE_TEXT + " TEXT NOT NULL, " +
                QuotesContract.QuoteEntry.COLUMN_AUTHOR + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_QUOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QuotesContract.QuoteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}