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
     * Returns a new {@link Single} that emits the {@link RxDiffResult}.
     *
     * @param diffRequest {@link DiffRequest} holding all parameters to start the difference calculation.
     * @return {@link Single}.
     */
    private static Single<RxDiffResult> single(@NonNull final DiffRequest diffRequest) {
        return Single.fromCallable(new Callable<RxDiffResult>() {
            @Override
            public RxDiffResult call() throws Exception {
                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffRequest.getDiffCallback(), diffRequest.isDetectingMoves());
                return new RxDiffResult(diffRequest.getTag(), diffResult);
            }
        });
    }

    /**
     * Adds a new {@link DiffRequest} to the pending list.
     *
     * @param diffRequest A new {@link DiffRequest}.
     *                    Note: if a request with the same tag is already exists it will be replaced.
     */
    void addPendingRequest(@NonNull final DiffRequest diffRequest) {
        checkNotNull(diffRequest, "diffRequest must not be null!");
        mPendingRequests.put(diffRequest.getTag(), diffRequest);
    }

    /**
    /**
     * Disposes the {@link Disposable} according to the given tag if the subscription is still in progress.
     *
     * @param tag A {@link String} identifying the diff request.
     */
    private void dispose(@NonNull final String tag) {
        final Disposable disposableForTag = mCurrentSubscriptions.get(tag);

        // Check if the subscription is still in progress, if so then dispose the disposable
        if (disposableForTag != null) {
            mCompositeDisposable.remove(disposableForTag);
            mCurrentSubscriptions.remove(tag);
        }
    }

    /**
     * @return A map holding all pending requests. Note: the returned map is unmodifiable.
     */
    @NonNull
    Map<String, DiffRequest> getPendingRequests() {
        return Collections.unmodifiableMap(mPendingRequests);
    }
}
