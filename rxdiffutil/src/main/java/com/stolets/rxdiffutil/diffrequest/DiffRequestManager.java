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
import android.support.annotation.UiThread;
import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.RxDiffResult;
import com.stolets.rxdiffutil.di.FragmentScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
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
     * Starts the pending request which matches the given tag.
     * If the request contains {@link DefaultDiffCallback} or its subclass the request will be handled automatically.
     *
     * @param tag {@link String} identifying the pending request.
     * @return {@link Single}.
     */
    @NonNull
    Single<RxDiffResult> execute(@NonNull final String tag) {
        checkNotNull(tag, "tag string must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        final DiffRequest diffRequest = mPendingRequests.get(tag);
        if (diffRequest == null) {
            throw new IllegalArgumentException("There is no pending request for the specified tag!");
        }

        // Remove pending request
        mPendingRequests.remove(tag);

        // Remove the current subscription for the request with the same tag
        dispose(tag);

        final Single<RxDiffResult> diffResultSingle = single(diffRequest).cache();

        // Check whether we should subscribe to the single here
        if (diffRequest.getDiffCallback() instanceof DefaultDiffCallback) {
            final DefaultDiffCallback defaultDiffCallback = (DefaultDiffCallback) diffRequest.getDiffCallback();
            final Disposable disposable = diffResultSingle
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DiffResultSubscriber(defaultDiffCallback));

            registerDisposable(disposable, diffRequest.getTag());
        }

        return diffResultSingle;
    }

    /**
     * Clears all current pending requests and disposes all current subscriptions.
     */
    void releaseResources() {
        mPendingRequests.clear();
        mCurrentSubscriptions.clear();
        mCompositeDisposable.clear();
    }

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
            mCurrentSubscriptions.remove(tag);
            mCompositeDisposable.remove(disposableForTag);
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
