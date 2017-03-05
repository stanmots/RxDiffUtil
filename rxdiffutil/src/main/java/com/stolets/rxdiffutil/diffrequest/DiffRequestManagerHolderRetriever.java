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

package com.stolets.rxdiffutil.diffrequest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.stolets.rxdiffutil.internal.Constants;
import com.stolets.rxdiffutil.util.ActivityUtils;
import com.stolets.rxdiffutil.util.SupportActivityUtils;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * The class used to retrieveFrom the diff request manager holder from the retained fragment.
 */
public final class DiffRequestManagerHolderRetriever {
    private DiffRequestManagerHolderRetriever() {}

    /**
     * Retrieves the {@link DiffRequestManagerHolder} from the retained fragment.
     *
     * @param activity The {@link Activity} the requests lifecycle will be bound to.
     * @return {@link DiffRequestManagerHolder}.
     *
     * @throws NullPointerException If the given activity is null.
     */
    @NonNull
    public static DiffRequestManagerHolder retrieveFrom(@NonNull final Activity activity) {
        checkNotNull(activity, "activity must not be null");
        return findOrCreateHolder(activity);
    }

    @NonNull
    static DiffRequestManagerHolder findOrCreateHolder(@NonNull final Activity activity) {
        if (activity instanceof AppCompatActivity) {
            final AppCompatActivity compatActivity = (AppCompatActivity) activity;
            return ((SupportDiffRequestManagerHolderFragment) SupportActivityUtils.
                    findOrCreateSupportFragment(compatActivity.getSupportFragmentManager(), Constants.RETAINED_FRAGMENT_TAG)).
                    getDiffRequestManagerHolder();
        } else {
            return ((DiffRequestManagerHolderFragment) ActivityUtils.
                    findOrCreateFragment(activity.getFragmentManager(), Constants.RETAINED_FRAGMENT_TAG)).
                    getDiffRequestManagerHolder();
        }
    }
}
