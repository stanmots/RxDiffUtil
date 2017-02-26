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
 * The class used to retrieve the diff request manager from the retained fragment.
 */
final class DiffRequestManagerRetriever {
    private DiffRequestManagerRetriever() {}

    /**
     * Determines the activity type (system or from the support library) and retrieves the {@link DiffRequestManager} from the appropriate retained fragment.
     * @param activity {@link Activity} request lifecycle is bound to.
     * @return {@link DiffRequestManager}.
     */
    @NonNull
    static DiffRequestManager retrieve(@NonNull final Activity activity) {
        checkNotNull(activity, "activity must not be null");
        return findOrCreateManager(activity);
    }

    @NonNull
    static DiffRequestManager findOrCreateManager(@NonNull final Activity activity) {
        if (activity instanceof AppCompatActivity) {
            final AppCompatActivity compatActivity = (AppCompatActivity) activity;
            return ((SupportDiffRequestManagerFragment) SupportActivityUtils.
                    findOrCreateSupportFragment(compatActivity.getSupportFragmentManager(), Constants.RETAINED_FRAGMENT_TAG)).
                    getDiffRequestManager();
        } else {
            return ((DiffRequestManagerFragment) ActivityUtils.
                    findOrCreateFragment(activity.getFragmentManager(), Constants.RETAINED_FRAGMENT_TAG)).
                    getDiffRequestManager();
        }
    }
}
