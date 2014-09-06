package com.aivanyuk.rx;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.lang.ref.WeakReference;

/**
 * @author Artyom Ivanyuk (ivanyuk.artyom@gmail.com)
 *         on 8/22/14
 */
public final class CursorObservable {
    private CursorObservable() {
    }

    public static Observable<Cursor> create(Context context, Uri uri) {
        return new Builder(context).uri(uri).build();
    }

    public static class Builder {
        private final Context context;
        private Uri uri;
        private String[] projection;
        private String selection;
        private String[] selectionArgs;
        private String sortOrder;
        private ContentObserver contentObsever;
        private WeakReference<Subscriber<? super Cursor>> subscriber = new WeakReference<Subscriber<? super Cursor>>(null);
        private Observable<Cursor> observable;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }


        public Builder uri(final Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder projection(String[] projection) {
            this.projection = projection;
            return this;
        }

        public Builder selection(String selection, String[] selectionArgs) {
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            return this;
        }

        public Builder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Observable<Cursor> build() {
            contentObsever = new ForceLoadContentObserver();
            observable =  Observable.create(
                    new Observable.OnSubscribe<Cursor>() {
                        @Override
                        public void call(final Subscriber<? super Cursor> subscriber) {
                            try {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }
                                Builder.this.subscriber = new WeakReference<Subscriber<? super Cursor>>(subscriber);
                                Cursor cursor = context.getContentResolver().query(uri, projection,
                                                                                   selection,
                                                                                   selectionArgs,
                                                                                   sortOrder);
                                if (cursor != null) {
                                    cursor.getCount();
                                    cursor.registerContentObserver(contentObsever);
                                }
                                subscriber.onNext(cursor);
                                subscriber.onCompleted();
                            } catch (Exception ex) {
                                subscriber.onError(ex);
                            }
                        }
                    }
            ).subscribeOn(Schedulers.io());
            return observable;
        }

        private class ForceLoadContentObserver extends ContentObserver {

            public ForceLoadContentObserver() {
                super(new Handler());
            }

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(final boolean selfChange) {
                onContentChanged();
            }
        }

        private void onContentChanged() {
            Subscriber<? super Cursor> s = subscriber.get();
            if (s != null && !s.isUnsubscribed()) {
                observable.subscribe(s);
            }
        }
    }

}
