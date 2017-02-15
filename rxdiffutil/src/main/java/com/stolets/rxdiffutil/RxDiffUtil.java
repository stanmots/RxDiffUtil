package com.stolets.rxdiffutil;

import android.app.Activity;
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
     * @param activity     An {@link Activity} the lifecycle of the request is bound to.
     *                     Note: When the activity is finished the request is cancelled automatically.
     * @param diffCallback A concrete implementation of {@link DiffUtil.Callback}.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    public static DiffRequestBuilder builder(@NonNull final Activity activity, @NonNull final DiffUtil.Callback diffCallback) {
        checkNotNull(activity, "activity must not be null!");
        checkNotNull(diffCallback, "diffCallback must not be null!");

        return new DiffRequestBuilder(activity, diffCallback);
    }
}
