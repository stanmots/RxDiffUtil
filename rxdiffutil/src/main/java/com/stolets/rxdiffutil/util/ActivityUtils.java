package com.stolets.rxdiffutil.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;

import com.stolets.rxdiffutil.diffrequest.DiffRequestManagerFragment;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

public final class ActivityUtils {
    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    static void addFragmentToActivity (@NonNull final FragmentManager fragmentManager,
                                                     @NonNull final Fragment fragment,
                                                     @NonNull final String tag) {
        checkNotNull(fragmentManager, "fragmentManager must not be null!");
        checkNotNull(fragment, "fragment must not be null!");
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        removeCurrentFragment(fragmentManager, tag);

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Retrieves the fragment with the given tag. If not exists then a new fragment will be created.
     *
     * @param fragmentManager The fragment manager used to find fragment.
     * @param tag Fragment tag.
     * @return The retrieved retained fragment or a its new instance.
     */
    @NonNull
    public static Fragment findOrCreateFragment(@NonNull final FragmentManager fragmentManager, @NonNull final String tag) {
        checkNotNull(fragmentManager, "fragmentManager must not be null!");
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = DiffRequestManagerFragment.newInstance();
            addFragmentToActivity(fragmentManager, fragment, tag);
        }

        return fragment;
    }

    /**
     * Removes the current fragment with the same tag if it exists to prevent duplicates.
     * @param fragmentManager A fragment manager which manages the fragment removal.
     * @param tag A tag of the fragment that must be removed.
     */
    private static void removeCurrentFragment(@NonNull final FragmentManager fragmentManager,
                                              @NonNull final String tag) {
        final Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }
}

