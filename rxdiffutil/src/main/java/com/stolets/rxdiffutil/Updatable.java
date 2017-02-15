package com.stolets.rxdiffutil;

import java.util.List;

/**
 * Defines the method used to update data managed by {@link android.support.v7.widget.RecyclerView.Adapter}.
 *
 * @param <U> Data type.
 */
@SuppressWarnings("WeakerAccess")
public interface Updatable<U> {
    /**
     * Updates the list with the old data.
     *
     * @param newData {@link List} with the new data which will replace the old data.
     */
    void update(List<? extends U> newData);
}
