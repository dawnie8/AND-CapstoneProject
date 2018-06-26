package ipomoea.quotes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import ipomoea.quotes.ui.FavoriteQuotesActivity;

public class QuotesAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPrefs = context.getSharedPreferences("quote_to_widget", 0);
        CharSequence quoteText = sharedPrefs.getString("quote_text", "You haven't added a quote here yet.");
        CharSequence author = sharedPrefs.getString("author", " ");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quotes_app_widget);
        views.setTextViewText(R.id.appwidget_quote, quoteText);
        views.setTextViewText(R.id.appwidget_author, author);

        Intent intent = new Intent(context, FavoriteQuotesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_linearLayout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

