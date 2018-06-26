package ipomoea.quotes.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import ipomoea.quotes.R;
import ipomoea.quotes.data.QuotesContract;
import ipomoea.quotes.model.Quote;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Quote> {

    private static final int QUOTES_LOADER_ID = 1;
    private TextView quoteTextView;
    private TextView authorTextView;
    private TextView noNetworkTextView;
    private ImageButton favoriteButton;
    private ImageButton newQuoteButton;
    private ImageButton shareButton;
    private RelativeLayout buttonsRelativeLayout;
    private Quote currentQuote = null;
    private boolean isFavoriteClicked;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quote_textView);
        authorTextView = findViewById(R.id.author_textView);
        newQuoteButton = findViewById(R.id.new_quote_button);
        favoriteButton = findViewById(R.id.favorite_button);
        shareButton = findViewById(R.id.share_button);
        buttonsRelativeLayout = findViewById(R.id.buttons_relative_layout);
        noNetworkTextView = findViewById(R.id.no_network_message);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isFavoriteClicked")) {
                favoriteButton.setBackgroundResource(R.drawable.heart_red);
                favoriteButton.setEnabled(false);
                favoriteButton.setClickable(false);
            }
        }

        newQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(QUOTES_LOADER_ID, null, MainActivity.this);
                favoriteButton.setBackgroundResource(R.drawable.heart_white);
                favoriteButton.setEnabled(true);
                favoriteButton.setClickable(true);
                isFavoriteClicked = false;
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuote != null) {
                    favoriteButton.setBackgroundResource(R.drawable.heart_red);
                    favoriteButton.setEnabled(false);
                    favoriteButton.setClickable(false);
                    isFavoriteClicked = true;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(QuotesContract.QuoteEntry.COLUMN_QUOTE_TEXT, currentQuote.getQuoteText());
                    contentValues.put(QuotesContract.QuoteEntry.COLUMN_AUTHOR, currentQuote.getAuthor());

                    getContentResolver().insert(QuotesContract.QuoteEntry.CONTENT_URI, contentValues);
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuote != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, currentQuote.getQuoteText());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
            }
        });

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(QUOTES_LOADER_ID, null, this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.favorite_quotes:
                Intent intent = new Intent(this, FavoriteQuotesActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public Loader<Quote> onCreateLoader(int id, Bundle args) {
        return new QuotesLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Quote> loader, Quote data) {

        currentQuote = data;
        if (currentQuote != null) {
            quoteTextView.setText(currentQuote.getQuoteText());
            authorTextView.setText(currentQuote.getAuthor());
            noNetworkTextView.setVisibility(View.GONE);
            buttonsRelativeLayout.setVisibility(View.VISIBLE);
            quoteTextView.setVisibility(View.VISIBLE);
            authorTextView.setVisibility(View.VISIBLE);
        } else {
            noNetworkTextView.setVisibility(View.VISIBLE);
            buttonsRelativeLayout.setVisibility(View.INVISIBLE);
            quoteTextView.setVisibility(View.INVISIBLE);
            authorTextView.setVisibility(View.INVISIBLE);

            noNetworkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoaderManager loaderManager = getSupportLoaderManager();
                    loaderManager.restartLoader(QUOTES_LOADER_ID, null, MainActivity.this);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Quote> loader) {
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFavoriteClicked", isFavoriteClicked);
    }
}

