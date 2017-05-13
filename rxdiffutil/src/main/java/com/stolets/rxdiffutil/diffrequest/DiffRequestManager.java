/*
 * MIT License
 *
 * Copyright (c) 2017 Stan Mots (Storix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.stolets.rxdiffutil.diffrequest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.stolets.rxdiffutil.RxDiffResult;
import com.stolets.rxdiffutil.Swappable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

import static com.stolets.rxdiffutil.internal.Preconditions.assertMainThread;
import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Manages the diff request lifecycle.
 *
 * @param <D> Comparable data type.
 * @param <A> The {@link RecyclerView.Adapter} type.
 */
public final class DiffRequestManager<D, A extends RecyclerView.Adapter & Swappable<D>> implements DiffResultReceiver, Manager<D> {
    @NonNull
    private final String mTag;
    @NonNull
    private final Relay<RxDiffResult> mRelay;
    @NonNull
    private final CompositeDisposable mCompositeDisposable;
    @NonNull
    private WeakReference<A> mAdapterWeakRef;
    @Nullable
    private DiffRequest<D> mCurrentDiffRequest;
    @Nullable
    private DiffUtil.DiffResult mPendingResult;
    @Nullable
    private RxDiffResult mCachedResultForSubscription;
    @Nullable
    private DiffResultSubscriber mDiffResultSubscriber;

    /**
     * Constructs a new instance of {@link DiffRequestManager} with injected parameters.
     *
     * @param adapter             {@link RecyclerView.Adapter} that will be automatically updated and notified about the data changes. Note: the given adapter must implement {@link Swappable} interface.
     * @param tag                 A {@link String} that represents a unique identifier of the manager.
     * @param relay               {@link Relay}.
     * @param compositeDisposable Inject {@link CompositeDisposable}.
     * @throws NullPointerException     If the tag, composite disposable or relay is null.
     * @throws IllegalArgumentException If the tag is empty.
     */
    public DiffRequestManager(@Nullable final A adapter,
                              @NonNull final String tag,
                              @NonNull final PublishRelay<RxDiffResult> relay,
                              @NonNull final CompositeDisposable compositeDisposable) {
        checkNotNull(adapter, "adapter must not be null!");
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");
        checkNotNull(relay, "relay must not be null!");
        checkNotNull(compositeDisposable, "compositeDisposable must not be null!");

        this.mAdapterWeakRef = new WeakReference<>(adapter);
        this.mTag = tag;
        this.mRelay = relay;
        this.mCompositeDisposable = compositeDisposable;
    }

    /**
     * Creates a new instance of {@link DiffRequestManager}.
     *
     * @param adapter {@link RecyclerView.Adapter}.
     * @param tag     A {@link String} identifying this manager.
     * @param <D>     Diff request data type.
     * @param <A>     The {@link RecyclerView.Adapter} type.
     * @return {@link DiffRequestManager}.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    @NonNull
    public static <D, A extends RecyclerView.Adapter & Swappable<D>> DiffRequestManager<D, A> create(@NonNull final A adapter, @NonNull final String tag) {
        final DiffRequestManager<D, A> diffRequestManager = new DiffRequestManager<>(adapter, tag, PublishRelay.<RxDiffResult>create(), new CompositeDisposable());
        return diffRequestManager;
    }

    /**
     * Returns a new {@link Single} that emits the {@link RxDiffResult}.
     *
     * @param diffRequest {@link DiffRequest} holding all parameters to start the difference calculation.
     * @return {@link Single}.
     */
    static Single<RxDiffResult> single(@NonNull final DiffRequest diffRequest) {
        return Single.fromCallable(new Callable<RxDiffResult>() {
            @Override
            public RxDiffResult call() throws Exception {
                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffRequest.getDiffCallback(), diffRequest.isDetectingMoves());
                return new RxDiffResult(diffRequest.getTag(), diffResult);
            }
        })
                .cache()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void receive(@NonNull RxDiffResult rxDiffResult) {
        update(rxDiffResult.getDiffResult());

        if (mRelay.hasObservers()) {
            mRelay.accept(rxDiffResult);
        } else {
            mCachedResultForSubscription = rxDiffResult;
        }
    }

    /**
     * @return The {@link Observable} you can subscribe to to receive the {@link RxDiffResult}.
     */
    @NonNull
    public Observable<RxDiffResult> diffResults() {
        Observable<RxDiffResult> cachedResult;
        if (mCachedResultForSubscription != null) {
            final RxDiffResult rxDiffResult = mCachedResultForSubscription;
            cachedResult = Observable.just(rxDiffResult);
            mCachedResultForSubscription = null;
        } else {
            cachedResult = Observable.empty();
        }
        return mRelay.hide()
                .startWith(cachedResult);
    }

    /**
     * Updates data and visual representation of the adapter if the setNewData and the adapter is not null.
     *
     * @param diffResult The result of the difference calculation.
     * @throws NullPointerException  If the diffResult is null.
     * @throws IllegalStateException If the current thread is not main thread.
     */
    @UiThread
    void update(@NonNull final DiffUtil.DiffResult diffResult) {
        checkNotNull(diffResult, "diffResult must not be null!");
        assertMainThread("The diff result must be obtained on the main thread");

        if (mCurrentDiffRequest == null) {
            return;
        }

        final List<D> newData = mCurrentDiffRequest.getNewData();
        if (newData == null) {
            return;
        }

        final A adapter = mAdapterWeakRef.get();
        if (adapter == null) {
            mPendingResult = diffResult;
            return;
        }

        // Update data
        adapter.swapData(newData);

        // Update visual representation
        diffResult.dispatchUpdatesTo(adapter);

        // Clear all pending data
        mCurrentDiffRequest = null;
        mPendingResult = null;
    }

    /**
     * Starts building the {@link DiffRequest}.
     *
     * @param callback The {@link DiffUtil.Callback} for the diff request.
     * @return The {@link DiffRequestBuilder}.
     */
    @SuppressWarnings("WeakerAccess")
    @NonNull
    public DiffRequestBuilder<D> newDiffRequestWith(@NonNull final DiffUtil.Callback callback) {
        checkNotNull(callback, "callback must not be null!");
        return DiffRequestBuilder.create(this, callback);
    }

    /**
     * Starts the difference calculation in accordance with the parameters defines in the given diff request.
     *
     * @param diffRequest {@link DiffRequest} containing parameters for the diff calculation.
     * @throws NullPointerException If the given diff request is null.
     */
    @Override
    public void execute(@NonNull final DiffRequest<D> diffRequest) {
        checkNotNull(diffRequest, "diffRequest string must not be null!");

        mCurrentDiffRequest = diffRequest;

        // Unsubscribe the previous subscription
        mCompositeDisposable.clear();

        mCompositeDisposable.add(single(diffRequest)
                .subscribe(getDiffResultSubscriber()));
    }

    /**
     * Clears all current subscriptions.
     */
    void releaseResources() {
        mCompositeDisposable.clear();
    }

    /**
     * Replaces the adapter (e.g., after the configuration changes).
     *
     * @param adapter The new recycler view adapter reference.
     */
    @SuppressWarnings("WeakerAccess")
    public void swapAdapter(@Nullable final A adapter) {
        this.mAdapterWeakRef = new WeakReference<>(adapter);
        updateAdapterIfNeeded();
    }

    /**
     * Dispatches the updates to the adapter if there is a pending result.
     */
    void updateAdapterIfNeeded() {
        if (mPendingResult != null) {
            update(mPendingResult);
        }
    }

    /**
     * @return The adapter or null if it was destroyed due to a configuration change.
     */
    @Nullable
    A getAdapter() {
        return this.mAdapterWeakRef.get();
    }

    /**
     * @return A tag string.
     */
    @SuppressWarnings("WeakerAccess")
    @NonNull
    @Override
    public String getTag() {
        return mTag;
    }

    /**
     * Returns the pending {@link DiffUtil.DiffResult} which is saved when the calculation was completed during a configuration change.
     *
     * @return {@link DiffUtil.DiffResult}.
     */
    @Nullable
    DiffUtil.DiffResult getPendingResult() {
        return mPendingResult;
    }

    /**
     * Sets the pending result to update the adapter later.
     *
     * @param pendingResult {@link DiffUtil.DiffResult}.
     */
    void setPendingResult(@Nullable DiffUtil.DiffResult pendingResult) {
        mPendingResult = pendingResult;
    }

    @Nullable
    RxDiffResult getCachedResultForSubscription() {
        return mCachedResultForSubscription;
    }

    void setCachedResultForSubscription(@Nullable RxDiffResult cachedResultForSubscription) {
        mCachedResultForSubscription = cachedResultForSubscription;
    }

    @Nullable
    DiffRequest<D> getCurrentDiffRequest() {
        return mCurrentDiffRequest;
    }

    void setCurrentDiffRequest(@Nullable DiffRequest<D> currentDiffRequest) {
        mCurrentDiffRequest = currentDiffRequest;
    }

    @NonNull
    DiffResultSubscriber getDiffResultSubscriber() {
        if (mDiffResultSubscriber == null) {
            mDiffResultSubscriber = new DiffResultSubscriber(this);
        }

        return mDiffResultSubscriber;
    }

    void setDiffResultSubscriber(@NonNull DiffResultSubscriber diffResultSubscriber) {
        checkNotNull(diffResultSubscriber, "diffResultSubscriber must not be null");

        mDiffResultSubscriber = diffResultSubscriber;
    }

    /**
     * The class used to handle {@link RxDiffResult} obtained from {@link Single}.
     */
    @SuppressWarnings("WeakerAccess")
    static final class DiffResultSubscriber implements BiConsumer<RxDiffResult, Throwable> {
        @NonNull
        private final WeakReference<DiffResultReceiver> mDiffResultReceiverWeakRef;

        DiffResultSubscriber(@NonNull final DiffResultReceiver diffResultReceiver) {
            checkNotNull(diffResultReceiver, "diffResultReceiver must not be null!");
            mDiffResultReceiverWeakRef = new WeakReference<>(diffResultReceiver);
        }

        @Override
        @UiThread
        public void accept(RxDiffResult rxDiffResult, Throwable throwable) throws Exception {
            assertMainThread("The diff result must be obtained on the main thread");

            if (throwable != null) {
                throw new IllegalStateException("Failed to calculate diff", throwable);
            }

            if (rxDiffResult == null) {
                throw new IllegalStateException("rxDiffResult must not be null");
            }

            final DiffResultReceiver diffResultReceiver = mDiffResultReceiverWeakRef.get();
            if (diffResultReceiver != null) {
                diffResultReceiver.receive(rxDiffResult);
            }
        }
    }
}

