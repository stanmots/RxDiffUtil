package com.stolets.rxdiffutil;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;
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
    @NonNull
    private WeakReference<A> mAdapterWeakRef;
    @NonNull
    private final List<? extends D> mOldData;
    @NonNull
    private final List<? extends D> mNewData;

    /**
     * Constructs an instance of {@link DefaultDiffCallback}.
     *
     * @param oldData The current list with the data.
     * @param newData The updated list with the data which is compared with the oldData.
     * @param adapter {@link RecyclerView.Adapter} that will be automatically updated and notified about the data changes. Note: the given adapter must implement {@link Updatable} interface.
     */
    public DefaultDiffCallback(@NonNull final List<? extends D> oldData,
                               @NonNull final List<? extends D> newData,
                               @NonNull final A adapter) {
        checkNotNull(oldData, "oldData must not be null!");
        checkNotNull(newData, "newData must not be null!");
        checkNotNull(newData, "adapter must not be null!");

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
}
