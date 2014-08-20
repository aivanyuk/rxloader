package me.tatarka.rxloader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import static me.tatarka.rxloader.RxLoaderManager.FRAGMENT_TAG;

/**
 * Get an instance of {@link RxLoaderManager} that works with the support
 * library.
 *
 * @author Evan Tatarka
 */
public final class RxLoaderManagerCompat {
    private RxLoaderManagerCompat() {

    }

    /**
     * Get an instance of {@code RxLoaderManager} that is tied to the lifecycle of the given {@link
     * android.support.v4.app.FragmentActivity}.
     *
     * To avoid memory leakage, call {@link RxLoaderManager#unsubscribeAll()} at onDestroy of
     * your fragment. To clear cached loaders results, call {@link RxLoaderManager#reset()}.
     * There might be a good idea to check, whether fragment is trying to save
     * its state before destruction and call {@link RxLoaderManager#reset()} accordingly
     *
     * @param activity the activity
     * @return the {@code RxLoaderManager}
     */
    public static RxLoaderManager get(FragmentActivity activity, Fragment owner) {
        RxLoaderBackendFragmentCompat manager = (RxLoaderBackendFragmentCompat) activity.getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (manager == null) {
            manager = new RxLoaderBackendFragmentCompat();
            activity.getSupportFragmentManager().beginTransaction().add(manager, FRAGMENT_TAG).commit();
        }
        return new RxLoaderManager(manager.get(owner));
    }
}
