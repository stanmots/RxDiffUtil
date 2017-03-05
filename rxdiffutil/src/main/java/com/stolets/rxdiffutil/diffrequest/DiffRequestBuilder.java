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
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * The builder used to supply the required input to start difference calculation.
 */
@SuppressWarnings("WeakerAccess")
public final class DiffRequestBuilder<D> {
    @NonNull
    private final Manager<D> mDiffRequestManager;
    @NonNull
    private final String mTag;
    @NonNull
    private final DiffUtil.Callback mCallback;
    @Nullable
    private List<D> mNewData;
    private boolean mDetectMoves;

    /**
     * Constructs a new {@link DiffRequestBuilder} instance.
     *
     * @param diffRequestManager The {@link DiffRequestManager} the built request will be automatically executed by.
     * @param callback           The concrete implementation of {@link DiffUtil.Callback}.
     * @throws NullPointerException If the diffRequestManager or callback is null.
     */
    public DiffRequestBuilder(@NonNull final Manager<D> diffRequestManager,
                              @NonNull final DiffUtil.Callback callback) {
        checkNotNull(diffRequestManager, "diffRequestManager must not be null!");
        checkNotNull(callback, "callback must not be null!");

        this.mDiffRequestManager = diffRequestManager;
        this.mCallback = callback;
        this.mTag = diffRequestManager.getTag();
    }

    /**
     * Forwards the call to {@link DiffRequestBuilder#DiffRequestBuilder(Manager, DiffUtil.Callback)} ensuring type safety.
     */
    @NonNull
    public static <T> DiffRequestBuilder<T> create(@NonNull final Manager<T> diffRequestManager,
                                                   @NonNull final DiffUtil.Callback callback) {
        return new DiffRequestBuilder<>(diffRequestManager, callback);
    }

    /**
     * @param detectMoves Determines whether the moves are detected during difference calculation.
     *                    Default is false.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    public DiffRequestBuilder<D> detectMoves(final boolean detectMoves) {
        mDetectMoves = detectMoves;
        return this;
    }

    /**
     * Supplies the list with the new data to update the recycler view adapter when the diff calculation is complete.
     *
     * @param newData The new data.
     * @return {@link DiffRequestBuilder}.
     * @throws NullPointerException If the given new data list is null.
     */
    @NonNull
    public DiffRequestBuilder<D> updateAdapterWithNewData(@NonNull final List<D> newData) {
        checkNotNull(newData, "newData must not be null!");
        this.mNewData = newData;
        return this;
    }

    /**
     * Constructs the {@link DiffRequest} and initiates the difference calculation.
     */
    public void calculate() {
        // Build the request
        final DiffRequest<D> diffRequest = new DiffRequest<>(mDetectMoves, mTag, mNewData, mCallback);

        // Start diff calculation
        mDiffRequestManager.execute(diffRequest);
    }

    /**
     * @return Boolean flag indicating whether the moves in the lists must be detected.
     */
    public boolean isDetectingMoves() {
        return mDetectMoves;
    }

    /**
     * @return Current list with the calculated new data.
     */
    @Nullable
    List<D> getNewData() {
        return mNewData;
    }
}
