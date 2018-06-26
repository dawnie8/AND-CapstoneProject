package ipomoea.quotes.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ipomoea.quotes.QuotesAppWidgetProvider;
import ipomoea.quotes.R;
import ipomoea.quotes.data.QuotesContract;

public class QuotesCursorAdapter extends RecyclerView.Adapter<QuotesCursorAdapter.QuoteViewHolder> {

    private Cursor cursor;
    private Context context;

    public QuotesCursorAdapter(Context context) {
        this.context = context;
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new QuoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(QuoteViewHolder holder, int position) {

        int idIndex = cursor.getColumnIndex(QuotesContract.QuoteEntry._ID);
        int quoteTextIndex = cursor.getColumnIndex(QuotesContract.QuoteEntry.COLUMN_QUOTE_TEXT);
        int authorIndex = cursor.getColumnIndex(QuotesContract.QuoteEntry.COLUMN_AUTHOR);

        cursor.moveToPosition(position);

        final int id = cursor.getInt(idIndex);
        String quoteText = cursor.getString(quoteTextIndex);
        String author = cursor.getString(authorIndex);

        holder.itemView.setTag(id);
        holder.quoteTextView.setText(quoteText);
        holder.authorTextView.setText(author);

    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    public class QuoteViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView quoteTextView;
        TextView authorTextView;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            quoteTextView = itemView.findViewById(R.id.quote_textView);
            authorTextView = itemView.findViewById(R.id.author_textView);
        }

        @Override
        public boolean onLongClick(View view) {

            PopupMenu popup = new PopupMenu(context, view);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    addToWidget();
                    return true;
                }
            });
            popup.show();

            return true;
        }

        void addToWidget() {

            String quoteText = quoteTextView.getText().toString();
            String author = authorTextView.getText().toString();

            SharedPreferences sharedPref = context.getSharedPreferences("quote_to_widget", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("quote_text", quoteText);
            editor.putString("author", author);
            editor.apply();

            Intent intent = new Intent(context, QuotesAppWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            int[] ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(new ComponentName(context, QuotesAppWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }

    }
}