package com.stolets.rxdiffutil;

import android.support.annotation.NonNull;

import java.util.List;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Holds all the required data to start the difference calculation.
 */
public final class DiffRequest<D> {
    @NonNull
    private final DataComparable<D> mDataComparable;
    @NonNull
    private final List<? extends D> mOldData;
    @NonNull
    private final List<? extends D> mNewData;
    private boolean mDetectMoves;

    /**
     * Constructs a new instance of {@link DiffRequest}.
     *
     * @param dataComparable A custom implementation of {@link DataComparable}.
     * @param oldData        The list with the old data used in difference calculations.
     * @param newData        The list with the new dta used in difference calculations.
     * @param detectMoves    A boolean flag which determines whether moved items should be detected.
     */
    public DiffRequest(@NonNull final DataComparable<D> dataComparable,
                       @NonNull final List<? extends D> oldData,
                       @NonNull final List<? extends D> newData,
                       final boolean detectMoves) {
        checkNotNull(dataComparable, "dataComparable must not be null!");
        checkNotNull(oldData, "oldData must not be null!");
        checkNotNull(newData, "newData must not be null!");

        this.mDataComparable = dataComparable;
        this.mOldData = oldData;
        this.mNewData = newData;
        this.mDetectMoves = detectMoves;
    }

    /**
     * @return {@link DataComparable} concrete implementation.
     */
    @NonNull
    public DataComparable<D> getDataComparable() {
        return mDataComparable;
    }

    /**
     * @return The list with the old data.
     */
    @NonNull
    public List<? extends D> getOldData() {
        return mOldData;
    }

    /**
     * @return The list with the new data.
     */
    @NonNull
    public List<? extends D> getNewData() {
        return mNewData;
    }

    /**
     * @return True if DiffUtil should try to detect moved items, false otherwise.
     */
    public boolean isDetectingMoves() {
        return mDetectMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiffRequest<?> that = (DiffRequest<?>) o;

        return mDetectMoves == that.mDetectMoves &&
                mDataComparable.equals(that.mDataComparable) &&
                mOldData.equals(that.mOldData) &&
                mNewData.equals(that.mNewData);
    }

    @Override
    public int hashCode() {
        int result = mDataComparable.hashCode();
        result = 31 * result + mOldData.hashCode();
        result = 31 * result + mNewData.hashCode();
        result = 31 * result + (mDetectMoves ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiffRequest{" +
                "mDataComparable=" + mDataComparable +
                ", mOldData=" + mOldData +
                ", mNewData=" + mNewData +
                ", mDetectMoves=" + mDetectMoves +
                '}';
    }
}
