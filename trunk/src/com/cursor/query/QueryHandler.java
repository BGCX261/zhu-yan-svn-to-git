
package com.cursor.query;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

public class QueryHandler extends AsyncQueryHandler {

    private static QueryHandler mInstance;
    private ArrayList<QueryListener> listeners = new ArrayList<QueryListener>();

    public synchronized static QueryHandler getInstance(ContentResolver contentResolver) {
        if (mInstance == null) {
            mInstance = new QueryHandler(contentResolver);
        }
        return mInstance;
    }

    private QueryHandler(ContentResolver cr) {
        super(cr);
    }

    public void addQueryListener(QueryListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeQueryListener(QueryListener listener) {
        listeners.remove(listener);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        for (QueryListener listener : listeners) {
            listener.onQueryComplete(token, cookie, cursor);
        }
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        for (QueryListener listener : listeners) {
            listener.onDeleteComplete(token, cookie, result);
        }
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        for (QueryListener listener : listeners) {
            listener.onInsertComplete(token, cookie, uri);
        }
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        for (QueryListener listener : listeners) {
            listener.onUpdateComplete(token, cookie, result);
        }
    }
}
