package com.stolets.rxdiffutil;

/**
 * Defines the method that returns a unique identifier used by {@link android.support.v7.util.DiffUtil.Callback#areItemsTheSame(int, int)}.
 *
 * @param <I> Id type.
 */
@SuppressWarnings("WeakerAccess")
public interface Identifiable<I> {
    I getId();
}
