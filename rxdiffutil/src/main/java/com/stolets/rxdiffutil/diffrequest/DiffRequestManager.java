package com.stolets.rxdiffutil.diffrequest;

import android.support.annotation.NonNull;

import com.stolets.rxdiffutil.di.FragmentScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Manages the diff requests lifecycle.
 */
@FragmentScope
public final class DiffRequestManager {
    @NonNull
    private Map<String, DiffRequest> mPendingRequests = new HashMap<>();
    @NonNull
    private Map<String, Disposable> mCurrentSubscriptions = new HashMap<>();
    @NonNull
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * Adds a new {@link DiffRequest} to the pending list.
     * @param diffRequest A new {@link DiffRequest}.
     *                    Note: if a request with the same tag is already exists it will be replaced.
     */
    void addPendingRequest(@NonNull final DiffRequest diffRequest) {
        checkNotNull(diffRequest, "diffRequest must not be null!");
        mPendingRequests.put(diffRequest.getTag(), diffRequest);
    }

    /**
     * @return A map holding all pending requests. Note: the returned map is unmodifiable.
     */
    @NonNull
    Map<String, DiffRequest> getPendingRequests() {
        return Collections.unmodifiableMap(mPendingRequests);
    }
}
