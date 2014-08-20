package me.tatarka.rxloader;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Persists the task by running it in a fragment with {@code setRetainInstanceState(true)}. This is
 * used internally by {@link RxLoaderManager}.
 *
 * @author Evan Tatarka
 */
public class RxLoaderBackendFragmentCompat extends Fragment {
    private Map<String, RxLoaderBackendFragmentHelper> helpers = new ConcurrentHashMap<String, RxLoaderBackendFragmentHelper>();

    public RxLoaderBackend get(Fragment owner) {
        String ownerId = owner.toString();
        RxLoaderBackendFragmentHelper backend = helpers.get(ownerId);
        if (backend == null) {
            backend = new RxLoaderBackendFragmentHelper();
            helpers.put(ownerId, backend);
        }
        return backend;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        for (Map.Entry<String, RxLoaderBackendFragmentHelper> helper : helpers.entrySet()) {
            helper.getValue().onCreate(savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Map.Entry<String, RxLoaderBackendFragmentHelper> helper : helpers.entrySet()) {
            helper.getValue().onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (Map.Entry<String, RxLoaderBackendFragmentHelper> helper : helpers.entrySet()) {
            helper.getValue().onSaveInstanceState(outState);
        }
    }
}
