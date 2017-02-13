package com.stolets.rxdiffutil;

import android.support.v7.util.DiffUtil;

/**
 * Interface which defines the methods required by {@link DiffUtil.Callback}.
 *
 * @param <D> Data type to compare.
 */
public interface DataComparable<D> {
    /**
     * Corresponds to {@link DiffUtil.Callback#areContentsTheSame(int, int)}.
     */
    boolean areContentsTheSame(D oldData, D newData);

    /**
     * Corresponds to {@link DiffUtil.Callback#areItemsTheSame(int, int)}.
     */
    boolean areItemsTheSame(D oldData, D newData);

    /**
     * Corresponds to {@link DiffUtil.Callback#getNewListSize()}.
     */
    int getNewListSize();

    /**
     * Corresponds to {@link DiffUtil.Callback#getOldListSize()}.
     */
    int getOldListSize();
}
