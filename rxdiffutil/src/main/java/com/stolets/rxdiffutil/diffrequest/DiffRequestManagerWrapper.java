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

import android.support.annotation.NonNull;

import com.stolets.rxdiffutil.RxDiffResult;

import io.reactivex.Single;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;


/**
 * This class is used as a wrapper around {@link DiffRequestManager} to handle a request with the appropriate tag.
 */
@SuppressWarnings("WeakerAccess")
public final class DiffRequestManagerWrapper {
    @NonNull
    private final DiffRequestManager mDiffRequestManager;
    @NonNull
    private final String mTag;

    /**
     * Constructs a wrapper around {@link DiffRequestManager}.
     *
     * @param diffRequestManager {@link DiffRequestManager}.
     * @param tag                A tag that is passed to the underlying {@link DiffRequestManager} to handle the appropriate request.
     */
    public DiffRequestManagerWrapper(@NonNull final DiffRequestManager diffRequestManager, @NonNull final String tag) {
        checkNotNull(diffRequestManager, "diffRequestManager must not be null!");
        checkNotNull(tag, "tag string must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        this.mDiffRequestManager = diffRequestManager;
        this.mTag = tag;
    }

    /**
     * Starts the difference calculation in accordance with parameters specified in {@link DiffRequestBuilder}.
     *
     * @return {@link Single} you can subscribe to.
     */
    @NonNull
    public Single<RxDiffResult> calculate() {
        return mDiffRequestManager.execute(mTag);
    }

    /**
     * @return The tag you specified in {@link DiffRequestBuilder}.
     */
    @NonNull
    public String getTag() {
        return mTag;
    }
}
