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
import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.internal.Constants;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * The builder used to supply the required input to start difference calculation.
 */
public final class DiffRequestBuilder {
    @NonNull
    private final DiffUtil.Callback mDiffCallback;
    @NonNull
    private String mTag = Constants.DIFF_REQUEST_DEFAULT_TAG;
    private boolean mDetectMoves;

    /**
     * Constructs a new {@link DiffRequestBuilder} instance.
     *
     * @param diffCallback The concrete implementation of {@link DiffUtil.Callback}.
     *                     You can pass {@link com.stolets.rxdiffutil.DefaultDiffCallback} to update {@link android.support.v7.widget.RecyclerView.Adapter} automatically.
     */
    public DiffRequestBuilder(@NonNull final DiffUtil.Callback diffCallback) {
        checkNotNull(diffCallback, "diffCallback must not be null!");

        this.mDiffCallback = diffCallback;
    }

    /**
     * @param detectMoves Determines whether the moves are detected during difference calculation.
     *                    Default is false.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public DiffRequestBuilder detectMoves(final boolean detectMoves) {
        mDetectMoves = detectMoves;
        return this;
    }

    /**
     * @param tag A {@link String} that represents a unique identifier of the request.
     *            When you are making a new request the previous request with the same tag will be cancelled (if it is still in progress).
     *            You can skip it if your {@link Activity} manages just one {@link android.support.v7.widget.RecyclerView}.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public DiffRequestBuilder tag(@NonNull final String tag) {
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");
        mTag = tag;
        return this;
    }

    /**
     * Binds the constructed {@link DiffRequest} to the activity lifecycle.
     *
     * @param activity The {@link Activity} that determines the lifecycle of the background calculations.
     *                 When the activity is destroyed and finished (isFinishing = true) all current calculations are cancelled automatically.
     * @return {@link DiffRequestManagerWrapper} you can use to start the difference calculation.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public DiffRequestManagerWrapper bindTo(@NonNull final Activity activity) {
        checkNotNull(activity, "activity must not be null!");

        final DiffRequestManager diffRequestManager = DiffRequestManagerRetriever.retrieve(activity);
        final DiffRequest diffRequest = new DiffRequest(this.mDetectMoves, this.mTag, this.mDiffCallback);

        diffRequestManager.addPendingRequest(diffRequest);

        return new DiffRequestManagerWrapper(diffRequestManager, this.mTag);
    }

    /**
     * @return A concrete implementation of {@link DiffUtil.Callback}.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public DiffUtil.Callback getDiffCallback() {
        return mDiffCallback;
    }

    /**
     * @return A tag string.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public String getTag() {
        return mTag;
    }

    /**
     * @return DetectMoves boolean flag.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isDetectingMoves() {
        return mDetectMoves;
    }
}
