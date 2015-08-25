package com.cc.software.calendar.helper;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.ContactsContract.Data;

import com.cc.software.calendar.view.ContactPhotoView;

public class ContactPhotoLoader implements Callback {
    private static final int MESSAGE_REQUEST_LOADING = 1;
    private static final int MESSAGE_PHOTOS_LOADED = 2;
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final String[] COLUMNS = new String[] { ContactsContract.CommonDataKinds.Photo._ID, ContactsContract.CommonDataKinds.Photo.PHOTO };

    private final ConcurrentHashMap<Long, BitmapHolder> mBitmapCache = new ConcurrentHashMap<Long, BitmapHolder>();
    private final ConcurrentHashMap<ContactPhotoView, Long> mPendingRequests = new ConcurrentHashMap<ContactPhotoView, Long>();
    private final Handler mMainThreadHandler;
    private LoaderThread mLoaderThread;
    private boolean mLoadingRequested;
    private boolean mPaused;
    private Context mContext;
    private static ContactPhotoLoader instance;

    public final static ContactPhotoLoader getInstance(Context context) {
        if (instance == null) {
            instance = new ContactPhotoLoader(context, 0);
        } else {
            instance.mContext = context;
        }
        return instance;
    }

    public final static ContactPhotoLoader getInstanceNoCreate() {
        return instance;
    }

    /**
     * Load photo into the supplied image view.  If the photo is already cached,
     * it is displayed immediately.  Otherwise a request is sent to load the photo
     * from the database.
     */
    public void loadPhoto(ContactPhotoView view, long photoId) {
        if (photoId <= 0) {
            view.showDefault();
            mPendingRequests.remove(view);
        } else if (view.getCurrentPhotoId() == photoId) {
            mPendingRequests.remove(view);
            return;
        } else {
            boolean loaded = loadCachedPhoto(view, photoId);
            if (loaded) {
                mPendingRequests.remove(view);
            } else {
                mPendingRequests.put(view, photoId);
                if (!mPaused) {
                    requestLoading();
                }
            }
        }
    }

    public void stop() {
        pause();

        if (mLoaderThread != null) {
            mLoaderThread.quit();
            mLoaderThread = null;
        }

        mPendingRequests.clear();
        mBitmapCache.clear();
        instance = null;
    }

    public void destroy() {
        stop();
        clear();
        instance = null;
    }

    public void clear() {
        mPendingRequests.clear();
        mBitmapCache.clear();
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        mPaused = false;
        if (!mPendingRequests.isEmpty()) {
            requestLoading();
        }
    }

    /**
     * Constructor.
     *
     * @param context content context
     * @param defaultResourceId the image resource ID to be used when there is
     *            no photo for a contact
     */
    private ContactPhotoLoader(Context context, int defaultResourceId) {
        mContext = context;
        mMainThreadHandler = new Handler(this);
        //        Logger.log("*************************** creating contact photoloader");
    }

    /**
     * Checks if the photo is present in cache.  If so, sets the photo on the view,
     * otherwise sets the state of the photo to {@link BitmapHolder#NEEDED} and
     * temporarily set the image to the default resource ID.
     */
    private boolean loadCachedPhoto(ContactPhotoView view, long photoId) {
        BitmapHolder holder = mBitmapCache.get(photoId);
        if (holder == null) {
            holder = new BitmapHolder();
            mBitmapCache.put(photoId, holder);
        } else if (holder.state == BitmapHolder.LOADED) {
            // Null bitmap reference means that database contains no bytes for the photo
            if (holder.bitmapRef == null) {
                view.showDefault();
                return true;
            }

            Bitmap bitmap = holder.bitmapRef.get();
            if (bitmap != null) {
                view.setImageBitmap(bitmap, photoId);
                return true;
            }

            // Null bitmap means that the soft reference was released by the GC
            // and we need to reload the photo.
            holder.bitmapRef = null;
        }

        // The bitmap has not been loaded - should display the placeholder image.        
        view.showDefault();
        holder.state = BitmapHolder.NEEDED;
        return false;
    }

    /**
     * Sends a message to this thread itself to start loading images.  If the current
     * view contains multiple image views, all of those image views will get a chance
     * to request their respective photos before any of those requests are executed.
     * This allows us to load images in bulk.
     */
    private void requestLoading() {
        if (mLoadingRequested == false) {
            mLoadingRequested = true;
            mMainThreadHandler.sendEmptyMessage(MESSAGE_REQUEST_LOADING);
        }
    }

    /**
     * Processes requests on the main thread.
     */
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case MESSAGE_REQUEST_LOADING: {
            mLoadingRequested = false;
            if (mPaused == false) {
                if (mLoaderThread == null) {
                    mLoaderThread = new LoaderThread(mContext.getContentResolver());
                    mLoaderThread.start();
                }

                mLoaderThread.requestLoading();
            }
            return true;
        }

        case MESSAGE_PHOTOS_LOADED: {
            if (!mPaused) {
                processLoadedImages();
            }
            return true;
        }
        }
        return false;
    }

    /**
     * Goes over pending loading requests and displays loaded photos.  If some of the
     * photos still haven't been loaded, sends another request for image loading.
     */
    private void processLoadedImages() {
        Iterator<ContactPhotoView> iterator = mPendingRequests.keySet().iterator();
        while (iterator.hasNext()) {
            ContactPhotoView view = iterator.next();
            long photoId = mPendingRequests.get(view);
            boolean loaded = loadCachedPhoto(view, photoId);
            if (loaded) {
                iterator.remove();
            }
        }

        if (!mPendingRequests.isEmpty()) {
            requestLoading();
        }
    }

    /**
     * Stores the supplied bitmap in cache.
     */
    private void cacheBitmap(long id, byte[] bytes) {
        if (mPaused) {
            return;
        }

        BitmapHolder holder = new BitmapHolder();
        holder.state = BitmapHolder.LOADED;
        if (bytes != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                holder.bitmapRef = new SoftReference<Bitmap>(bitmap);
            } catch (OutOfMemoryError e) {
                // Do nothing - the photo will appear to be missing
            }
        }
        mBitmapCache.put(id, holder);
    }

    /**
     * Populates an array of photo IDs that need to be loaded.
     */
    private void obtainPhotoIdsToLoad(ArrayList<Long> photoIds, ArrayList<String> photoIdsAsStrings) {
        photoIds.clear();
        photoIdsAsStrings.clear();

        /*
         * Since the call is made from the loader thread, the map could be
         * changing during the iteration. That's not really a problem:
         * ConcurrentHashMap will allow those changes to happen without throwing
         * exceptions. Since we may miss some requests in the situation of
         * concurrent change, we will need to check the map again once loading
         * is complete.
         */
        Iterator<Long> iterator = mPendingRequests.values().iterator();
        while (iterator.hasNext()) {
            Long id = iterator.next();
            BitmapHolder holder = mBitmapCache.get(id);
            if (holder != null && holder.state == BitmapHolder.NEEDED) {
                // Assuming atomic behavior
                holder.state = BitmapHolder.LOADING;
                photoIds.add(id);
                photoIdsAsStrings.add(id.toString());
            }
        }
    }

    /**
     * The thread that performs loading of photos from the database.
     */
    private class LoaderThread extends HandlerThread implements Callback {
        private final ContentResolver mResolver;
        private final StringBuilder mStringBuilder = new StringBuilder();
        private final ArrayList<Long> mPhotoIds = new ArrayList<Long>(5);
        private final ArrayList<String> mPhotoIdsAsStrings = new ArrayList<String>(5);
        private Handler mLoaderThreadHandler;

        public LoaderThread(ContentResolver resolver) {
            super("ContactPhotoLoader");
            mResolver = resolver;
        }

        /**
         * Sends a message to this thread to load requested photos.
         */
        public void requestLoading() {
            if (mLoaderThreadHandler == null) {
                mLoaderThreadHandler = new Handler(getLooper(), this);
            }
            mLoaderThreadHandler.sendEmptyMessage(0);
        }

        /**
         * Receives the above message, loads photos and then sends a message
         * to the main thread to process them.
         */
        public boolean handleMessage(Message msg) {
            loadPhotosFromDatabase();
            mMainThreadHandler.sendEmptyMessage(MESSAGE_PHOTOS_LOADED);
            return true;
        }

        private void loadPhotosFromDatabase() {
            obtainPhotoIdsToLoad(mPhotoIds, mPhotoIdsAsStrings);

            int count = mPhotoIds.size();
            if (count == 0) {
                return;
            }

            mStringBuilder.setLength(0);
            mStringBuilder.append(Photo._ID + " IN(");
            for (int i = 0; i < count; i++) {
                if (i != 0) {
                    mStringBuilder.append(',');
                }
                mStringBuilder.append('?');
            }
            mStringBuilder.append(')');

            Cursor cursor = null;
            try {
                cursor = mResolver.query(Data.CONTENT_URI, COLUMNS, mStringBuilder.toString(), mPhotoIdsAsStrings.toArray(EMPTY_STRING_ARRAY), null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        Long id = cursor.getLong(0);
                        byte[] bytes = cursor.getBlob(1);
                        cacheBitmap(id, bytes);
                        mPhotoIds.remove(id);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            // Remaining photos were not found in the database - mark the cache accordingly.
            count = mPhotoIds.size();
            for (int i = 0; i < count; i++) {
                cacheBitmap(mPhotoIds.get(i), null);
            }
        }
    }

}
