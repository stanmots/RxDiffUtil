package com.stolets.rxdiffutil.diffrequest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.stolets.rxdiffutil.RxRequestManager;
import com.stolets.rxdiffutil.internal.Constants;

import java.lang.ref.WeakReference;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * The builder used to supply the required input to start difference calculation.
 */
public final class DiffRequestBuilder {
    @NonNull
    private final WeakReference<Activity> mActivityWeakRef;
    @NonNull
    private DiffUtil.Callback mDiffCallback;
    @NonNull
    private String mTag = Constants.DIFF_REQUEST_DEFAULT_TAG;
    @Nullable
    private WeakReference<RecyclerView.Adapter> mAdapterWeakRef;
    private boolean mDetectMoves;

    /**
     * Constructs a new {@link DiffRequestBuilder} instance.
     *
     * @param activity The {@link Activity} that determines the lifecycle of the async calculations.
     *                 When the activity is destroyed and finished (isFinishing = true) all current calculations are stopped automatically.
     */
    public DiffRequestBuilder(@NonNull final Activity activity, @NonNull final DiffUtil.Callback diffCallback) {
        checkNotNull(activity, "activity must not be null!");
        checkNotNull(diffCallback, "diffCallback must not be null!");

        // Required fields
        this.mActivityWeakRef = new WeakReference<>(activity);
        this.mDiffCallback = diffCallback;
    }

    /**
     * @param detectMoves Determines whether the moves are detected during difference calculation.
     *                    Default is false.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public DiffRequestBuilder detectMoves(final boolean detectMoves) {
        mDetectMoves = detectMoves;
        return this;
    }

    /**
     * @param tag A {@link String} that represents a unique identifier of the request.
     *            When you are making a new request the previous request with the same tag will be cancelled (if it is still in progress).
     *            You can skip it if your {@link Activity} manages just one {@link android.support.v7.widget.RecyclerView}.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public DiffRequestBuilder tag(@NonNull final String tag) {
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");
        mTag = tag;
        return this;
    }

    /**
     * Sets an adapter which will be automatically updated.
     *
     * @param adapter {@link android.support.v7.widget.RecyclerView.Adapter}.
     * @return {@link DiffRequestBuilder}.
     * @see {@link DiffRequest#setAdapter(RecyclerView.Adapter)}.
     */
    @NonNull
    public DiffRequestBuilder adapter(@NonNull final RecyclerView.Adapter adapter) {
        checkNotNull(adapter, "adapter must not be null!");
        this.mAdapterWeakRef = new WeakReference<>(adapter);
        return this;
    }

    /**
     * Builds the {@link DiffRequest} and returns {@link RxRequestManager} with the same tag and attached {@link DiffRequestManager}.
     * <p>
     * Note: If the given activity becomes null you won't be able to use the returned request manager to start diff calculations.
     * </p>
     *
     * @return {@link RxRequestManager}.
     */
    @NonNull
    public RxRequestManager build() {
        final Activity activity = getActivity();
        final RxRequestManager rxRequestManager = new RxRequestManager(this.mTag);

        if (activity != null) {
            final DiffRequestManager diffRequestManager = DiffRequestManagerRetriever.retrieve(activity);
            final DiffRequest diffRequest = new DiffRequest(this.mDetectMoves, this.mTag, this.mDiffCallback);
            final RecyclerView.Adapter adapter = mAdapterWeakRef != null ? mAdapterWeakRef.get() : null;
            if (adapter != null) {
                diffRequest.setAdapter(adapter);
            }

            diffRequestManager.addPendingRequest(diffRequest);
            rxRequestManager.attachDiffRequestManager(diffRequestManager);
        }

        return rxRequestManager;
    }

    /**
     * @return An {@link Activity} or null if it has been destroyed.
     */
    @Nullable
    @SuppressWarnings("WeakerAccess")
    public Activity getActivity() {
        return mActivityWeakRef.get();
    }

    /**
     * @return A concrete implementation of {@link DiffUtil.Callback}.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public DiffUtil.Callback getDiffCallback() {
        return mDiffCallback;
    }

    /**
     * @return A tag string.
     */
    @NonNull
    @SuppressWarnings("WeakerAccess")
    public String getTag() {
        return mTag;
    }

    /**
     * @return DetectMoves boolean flag.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isDetectingMoves() {
        return mDetectMoves;
    }
}
