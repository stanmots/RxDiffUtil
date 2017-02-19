/*
 * MIT License
 *
 * Copyright (c) 2017 Stan Mots (Storix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.stolets.rxdiffutil.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;

import com.stolets.rxdiffutil.diffrequest.DiffRequestManager;
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
            fragment = DiffRequestManagerFragment.newInstance(new DiffRequestManager());
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

