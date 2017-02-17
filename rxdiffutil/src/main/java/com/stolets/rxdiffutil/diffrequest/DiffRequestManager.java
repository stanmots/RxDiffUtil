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
     * Stores {@link Disposable} reference so that it can be retrieved later using the given tag.
     *
     * @param disposable {@link Disposable}
     * @param tag        A {@link String} representing the tag the given disposable is associated with.
     */
    private void registerDisposable(@NonNull final Disposable disposable, @NonNull final String tag) {
        mCompositeDisposable.add(disposable);
        mCurrentSubscriptions.put(tag, disposable);
    }

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

    /**
     * The class used to handle {@link RxDiffResult} obtained from {@link Single}.
     */
    private static final class DiffResultSubscriber implements BiConsumer<RxDiffResult, Throwable> {
        @NonNull
        private final DefaultDiffCallback mDefaultDiffCallback;

        /**
         * @param defaultDiffCallback {@link DefaultDiffCallback} instance or its subclass the {@link android.support.v7.util.DiffUtil.DiffResult} will be passed to.
         */
        DiffResultSubscriber(@NonNull final DefaultDiffCallback defaultDiffCallback) {
            checkNotNull(defaultDiffCallback, "defaultDiffCallback must not be null!");
            this.mDefaultDiffCallback = defaultDiffCallback;
        }

        @Override
        @UiThread
        public void accept(@io.reactivex.annotations.NonNull RxDiffResult rxDiffResult, @io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
            if (throwable != null && throwable.getMessage() != null) {
                throw new IllegalStateException("Failed to calculate diff", throwable);
            }

            if (rxDiffResult == null) {
                throw new IllegalStateException("rxDiffResult must not be null if the throwable is null too");
            }

            mDefaultDiffCallback.update(rxDiffResult.getDiffResult());
        }
    }
}
