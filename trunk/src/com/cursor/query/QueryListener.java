
package com.cursor.query;

import android.database.Cursor;
import android.net.Uri;

public interface QueryListener {

    void onQueryComplete(int token, Object cookie, Cursor cursor);

    void onDeleteComplete(int token, Object cookie, int result);

    void onInsertComplete(int token, Object cookie, Uri uri);

    void onUpdateComplete(int token, Object cookie, int result);
}
