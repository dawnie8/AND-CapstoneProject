package ipomoea.quotes.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static ipomoea.quotes.data.QuotesContract.QuoteEntry.TABLE_NAME;

public class QuotesContentProvider extends ContentProvider {

    private QuotesDbHelper quotesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        quotesDbHelper = new QuotesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase db = quotesDbHelper.getReadableDatabase();

        Cursor returnedCursor;
        returnedCursor = db.query(TABLE_NAME,
                strings,
                s,
                strings1,
                null,
                null,
                s1);

        returnedCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnedCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = quotesDbHelper.getWritableDatabase();
        Uri returnUri;

        long id = db.insert(TABLE_NAME, null, contentValues);
        if (id > 0) {
            returnUri = ContentUris.withAppendedId(QuotesContract.QuoteEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = quotesDbHelper.getWritableDatabase();
        int quotesDeleted;

        String id = uri.getPathSegments().get(1);

        quotesDeleted = db.delete(TABLE_NAME, QuotesContract.QuoteEntry._ID + "=?", new String[]{id});

        if (quotesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return quotesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}