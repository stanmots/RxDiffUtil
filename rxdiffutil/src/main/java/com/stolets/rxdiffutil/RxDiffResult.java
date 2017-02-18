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

package com.stolets.rxdiffutil;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Hods the {@link android.support.v7.util.DiffUtil.DiffResult} identified by the tag.
 */
public final class RxDiffResult {
    @NonNull
    private final String mTag;
    @NonNull
    private final DiffUtil.DiffResult mDiffResult;

    /**
     * Constructs a new instance of {@link RxDiffResult}.
     *
     * @param tag        A {@link String} identifying the result.
     * @param diffResult {@link android.support.v7.util.DiffUtil.DiffResult}.
     */
    public RxDiffResult(@NonNull final String tag, @NonNull final DiffUtil.DiffResult diffResult) {
        checkNotNull(diffResult, "diffResult must not be null!");
        checkNotNull(tag, "tag string must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        this.mTag = tag;
        this.mDiffResult = diffResult;
    }

    /**
     * @return A tag which corresponds to the appropriate diff request.
     */
    @NonNull
    public String getTag() {
        return mTag;
    }

    /**
     * @return {@link android.support.v7.util.DiffUtil.DiffResult}.
     */
    @NonNull
    public DiffUtil.DiffResult getDiffResult() {
        return mDiffResult;
    }

    @Override
    public String toString() {
        return "RxDiffResult{" +
                "mTag='" + mTag + '\'' +
                ", mDiffResult=" + mDiffResult +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RxDiffResult that = (RxDiffResult) o;

        return mTag.equals(that.mTag) &&
                mDiffResult.equals(that.mDiffResult);
    }

    @Override
    public int hashCode() {
        int result = mTag.hashCode();
        result = 31 * result + mDiffResult.hashCode();
        return result;
    }
}
