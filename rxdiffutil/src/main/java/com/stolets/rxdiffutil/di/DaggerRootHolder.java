package com.stolets.rxdiffutil.di;

import android.support.annotation.NonNull;

/**
 * The class used to hold Dagger's root component.
 */
public final class DaggerRootHolder {
    @NonNull
    private final RootComponent mRootComponent;

    private DaggerRootHolder() {
        mRootComponent = DaggerRootComponent.create();
    }

    private static class LazyHolder {
        private static final DaggerRootHolder INSTANCE = new DaggerRootHolder();
    }

    /**
     * Constructs the {@link DaggerRootHolder} instance in accordance with the Initialization-on-demand holder idiom.
     * @return {@link DaggerRootHolder}.
     */
    public static DaggerRootHolder getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * @return Dagger's root component.
     */
    @NonNull
    public RootComponent getRootComponent() {
        return mRootComponent;
    }
}
