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
import android.support.v7.util.DiffUtil;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Holds all the required data to start the difference calculation.
 */
public final class DiffRequest {
    @NonNull
    private final DiffUtil.Callback mDiffCallback;
    @NonNull
    private final String mTag;
    private final boolean mDetectMoves;

    /**
     * Constructs a new instance of {@link DiffRequest}.
     *
     * @param detectMoves  A boolean flag which determines whether moved items should be detected.
     * @param tag          A unique identifier of the request.
     * @param diffCallback A concrete implementation of {@link DiffUtil.Callback}.
     */
    @SuppressWarnings("WeakerAccess")
    public DiffRequest(final boolean detectMoves, @NonNull final String tag, @NonNull final DiffUtil.Callback diffCallback) {
        checkNotNull(diffCallback, "diffCallback must not be null!");
        checkNotNull(tag, "tag string must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        this.mDetectMoves = detectMoves;
        this.mTag = tag;
        this.mDiffCallback = diffCallback;
    }

    /**
     * @return {@link DiffUtil.Callback} concrete implementation.
     */
    @NonNull
    public DiffUtil.Callback getDiffCallback() {
        return mDiffCallback;
    }

    /**
     * @return A tag identifying the request.
     */
    @NonNull
    public String getTag() {
        return mTag;
    }

    /**
     * @return True if DiffUtil should try to detect moved items, false otherwise.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isDetectingMoves() {
        return mDetectMoves;
    }

    @Override
    public String toString() {
        return "DiffRequest{" +
                "mDiffCallback=" + mDiffCallback +
                ", mDetectMoves=" + mDetectMoves +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiffRequest that = (DiffRequest) o;

        return mDetectMoves == that.mDetectMoves &&
                mTag.equals(that.getTag()) &&
                mDiffCallback.equals(that.mDiffCallback);
    }

    @Override
    public int hashCode() {
        int result = mDiffCallback.hashCode();
        result = 31 * result + mTag.hashCode();
        result = 31 * result + (mDetectMoves ? 1 : 0);
        return result;
    }
}
