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
import android.support.annotation.UiThread;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Default implementation of the methods required by {@link DiffUtil.Callback}.
 * Feel free to create our own subclass overriding only the required method.
 *
 * @param <I> Type of the data id.
 * @param <D> Comparable data type.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DefaultDiffCallback<I, D extends Identifiable<I>, A extends RecyclerView.Adapter & Updatable<D>> extends DiffUtil.Callback {
    private static final String TAG = "DefaultDiffCallback";
    @NonNull
    private final List<D> mOldData;
    @NonNull
    private final List<D> mNewData;
    @NonNull
    private final WeakReference<A> mAdapterWeakRef;

    /**
     * Constructs an instance of {@link DefaultDiffCallback}.
     *
     * @param oldData The current list with the data.
     * @param newData The updated list with the data which is compared with the oldData.
     * @param adapter {@link RecyclerView.Adapter} that will be automatically updated and notified about the data changes. Note: the given adapter must implement {@link Updatable} interface.
     */
    public DefaultDiffCallback(@NonNull final List<D> oldData,
                               @NonNull final List<D> newData,
                               @NonNull final A adapter) {
        checkNotNull(oldData, "oldData must not be null!");
        checkNotNull(newData, "newData must not be null!");
        checkNotNull(adapter, "adapter must not be null!");

        this.mOldData = oldData;
        this.mNewData = newData;
        this.mAdapterWeakRef = new WeakReference<>(adapter);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewData.get(newItemPosition).equals(mOldData.get(oldItemPosition));
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewData.get(newItemPosition).getId().equals(mOldData.get(oldItemPosition).getId());
    }

    @Override
    public int getNewListSize() {
        return mNewData.size();
    }

    @Override
    public int getOldListSize() {
        return mOldData.size();
    }

    /**
     * Updates data and visual representation of the adapter.
     * @param diffResult The result of the difference calculation.
     */
    @UiThread
    public void update(@NonNull final DiffUtil.DiffResult diffResult) {
        checkNotNull(diffResult, "diffResult must not be null!");

        final A adapter = mAdapterWeakRef.get();
        if (adapter != null) {
            // Update data
            adapter.update(mNewData);

            // Update visual representation
            diffResult.dispatchUpdatesTo(adapter);
        } else {
            Log.w(TAG, "The adapter is null, diff result updates won't be dispatched");
        }
    }

    /**
     * @return {@link List} with old data.
     */
    @NonNull
    public List<D> getOldData() {
        return Collections.unmodifiableList(mOldData);
    }

    /**
     * @return {@link List} with new data.
     */
    @NonNull
    public List<D> getNewData() {
        return Collections.unmodifiableList(mNewData);
    }
}
