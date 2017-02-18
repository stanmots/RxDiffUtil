package com.stolets.rxdiffutil;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.diffrequest.DiffRequestBuilder;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Represents an entry point for making diff requests.
 */
@SuppressWarnings("WeakerAccess")
public final class RxDiffUtil {

    /**
     * Creates the diff request builder that is used to configure request parameters.
     *
     * @param diffCallback A concrete implementation of {@link DiffUtil.Callback}.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    public static DiffRequestBuilder with(@NonNull final DiffUtil.Callback diffCallback) {
        checkNotNull(diffCallback, "diffCallback must not be null!");

        return new DiffRequestBuilder(diffCallback);
    }
}
