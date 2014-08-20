package me.tatarka.rxloader;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * A way to manage asynchronous actions in Android using rxjava. It is much easier to get right than
 * an {@link android.os.AsyncTask} or a {@link android.content.Loader}. It properly handles Activity
 * destruction, configuration changes, and posting back to the UI thread.
 *
 * @author Evan Tatarka
 */
public class RxLoaderManager {
    /**
     * The default tag for an async operation. In many cases an Activity or Fragment only needs to
     * run one async operation so it's unnecessary to use tags to differentiate them. If you omit a
     * tag on a method that take one, this tag is used instead.
     */
    public static final String DEFAULT = RxLoaderManager.class.getCanonicalName() + "_default";

    static final String FRAGMENT_TAG = RxLoaderManager.class.getCanonicalName() + "_fragment";

    private final RxLoaderBackend manager;

    RxLoaderManager(RxLoaderBackend manager) {
        this.manager = manager;
    }

    /**
     * Creates a new {@link RxLoader} that manages the given {@link
     * rx.Observable}. This should be called in {@link android.app.Activity#onCreate(android.os.Bundle)}
     * or similar.
     *
     * @param tag        The loader's tag. This must be unique across all loaders for a given
     *                   manager.
     * @param observable the observable to manage
     * @param observer   the observer that receives the observable's callbacks
     * @param <T>        the observable's value type
     * @return a new {@code RxLoader}
     * @see RxLoader
     */
    public <T> RxLoader<T> create(String tag, Observable<T> observable, RxLoaderObserver<T> observer) {
        return new RxLoader<T>(manager, tag, observable, observer);
    }

    /**
     * Creates a new {@link RxLoader} that manages the given {@link
     * rx.Observable}. It uses the {@link RxLoaderManager#DEFAULT} tag. This
     * should be called in {@link android.app.Activity#onCreate(android.os.Bundle)} or similar.
     *
     * @param observable the observable to manage
     * @param observer   the observer that receives the observable's callbacks
     * @param <T>        the observable's value type
     * @return a new {@code RxLoader}
     * @see RxLoader
     */
    public <T> RxLoader<T> create(Observable<T> observable, RxLoaderObserver<T> observer) {
        return new RxLoader<T>(manager, DEFAULT, observable, observer);
    }

    /**
     * Creates a new {@link RxLoader1} that manages the given {@link
     * rx.Observable}. This should be called in {@link android.app.Activity#onCreate(android.os.Bundle)}
     * or similar.
     *
     * @param tag            The loader's tag. This must be unique across all loaders for a given
     *                       manager.
     * @param observableFunc the function that returns the observable to manage
     * @param observer       the observer that receives the observable's callbacks
     * @param <A>            the argument's type.
     * @param <T>            the observable's value type
     * @return a new {@code RxLoader}
     * @see RxLoader1
     */
    public <A, T> RxLoader1<A, T> create(String tag, Func1<A, Observable<T>> observableFunc, RxLoaderObserver<T> observer) {
        return new RxLoader1<A, T>(manager, tag, observableFunc, observer);
    }

    /**
     * Creates a new {@link RxLoader} that manages the given {@link
     * rx.Observable}. It uses the {@link RxLoaderManager#DEFAULT} tag. This
     * should be called in {@link android.app.Activity#onCreate(android.os.Bundle)} or similar.
     *
     * @param observableFunc the function that returns the observable to manage
     * @param observer       the observer that receives the observable's callbacks
     * @param <T>            the observable's value type
     * @param <A>            the argument's type.
     * @return a new {@code RxLoader}
     * @see RxLoader
     */
    public <A, T> RxLoader1<A, T> create(Func1<A, Observable<T>> observableFunc, RxLoaderObserver<T> observer) {
        return new RxLoader1<A, T>(manager, DEFAULT, observableFunc, observer);
    }

    /**
     * Creates a new {@link RxLoader1} that manages the given {@link
     * rx.Observable}. This should be called in {@link android.app.Activity#onCreate(android.os.Bundle)}
     * or similar.
     *
     * @param tag            The loader's tag. This must be unique across all loaders for a given
     *                       manager.
     * @param observableFunc the function that returns the observable to manage
     * @param observer       the observer that receives the observable's callbacks
     * @param <T>            the observable's value type
     * @param <A>            the fist argument's type.
     * @param <B>            the second argument's type.
     * @return a new {@code RxLoader}
     * @see RxLoader1
     */
    public <A, B, T> RxLoader2<A, B, T> create(String tag, Func2<A, B, Observable<T>> observableFunc, RxLoaderObserver<T> observer) {
        return new RxLoader2<A, B, T>(manager, tag, observableFunc, observer);
    }

    /**
     * Creates a new {@link RxLoader} that manages the given {@link
     * rx.Observable}. It uses the {@link RxLoaderManager#DEFAULT} tag. This
     * should be called in {@link android.app.Activity#onCreate(android.os.Bundle)} or similar.
     *
     * @param observableFunc the function that returns the observable to manage
     * @param observer       the observer that receives the observable's callbacks
     * @param <T>            the observable's value type
     * @param <A>            the fist argument's type.
     * @param <B>            the second argument's type.
     * @return a new {@code RxLoader}
     * @see RxLoader
     */
    public <A, B, T> RxLoader2<A, B, T> create(Func2<A, B, Observable<T>> observableFunc, RxLoaderObserver<T> observer) {
        return new RxLoader2<A, B, T>(manager, DEFAULT, observableFunc, observer);
    }

    /**
     * Returns if the {@code RxLoaderManager} contains a {@link RxLoader} with
     * the given tag.
     *
     * @param tag the loader's tag
     * @return true if {@code RxLoaderManager} contains the tag, false otherwise
     */
    public boolean contains(String tag) {
        return manager.get(tag) != null;
    }

    /**
     * Unsubscribes all observers.
     */
    public void unsubscribeAll() {
        manager.unsubscribeAll();
    }

    public void reset() {
        manager.removeAll();
    }
}
