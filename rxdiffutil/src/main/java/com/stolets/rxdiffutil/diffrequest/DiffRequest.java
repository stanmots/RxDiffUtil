package com.stolets.rxdiffutil.diffrequest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;

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
    @Nullable
    private WeakReference<RecyclerView.Adapter> mAdapterWeakRef;
    private boolean mDetectMoves;

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
     * @return {@link android.support.v7.widget.RecyclerView.Adapter} or null it wasn't set.
     */
    @Nullable
    public RecyclerView.Adapter getAdapter() {
        return mAdapterWeakRef != null ? mAdapterWeakRef.get() : null;
    }

    /**
     * Sets the optinal {@link android.support.v7.widget.RecyclerView.Adapter}.
     * <p>
     * Note: when the adapter is set it will be notified automatically about data changes.
     * </p>
     *
     * @param adapter {@link android.support.v7.widget.RecyclerView.Adapter}.
     */
    @SuppressWarnings("WeakerAccess")
    public void setAdapter(@NonNull final RecyclerView.Adapter adapter) {
        checkNotNull(adapter, "adapter must not be null!");
        mAdapterWeakRef = new WeakReference<>(adapter);
    }

    /**
     * @return True if DiffUtil should try to detect moved items, false otherwise.
     */
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

        final RecyclerView.Adapter adapter = mAdapterWeakRef != null ? mAdapterWeakRef.get() : null;
        final RecyclerView.Adapter thatAdapter = that.mAdapterWeakRef != null ? that.mAdapterWeakRef.get() : null;

        return mDetectMoves == that.mDetectMoves &&
                mTag.equals(that.getTag()) &&
                adapter != null ? adapter.equals(thatAdapter) : thatAdapter == null &&
                mDiffCallback.equals(that.mDiffCallback);
    }

    @Override
    public int hashCode() {
        final RecyclerView.Adapter adapter = mAdapterWeakRef != null ? mAdapterWeakRef.get() : null;

        int result = mDiffCallback.hashCode();
        result = 31 * result + mTag.hashCode();
        result = 31 * result + (adapter != null ? adapter.hashCode() : 0);
        result = 31 * result + (mDetectMoves ? 1 : 0);
        return result;
    }
}
