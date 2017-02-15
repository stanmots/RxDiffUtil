package com.stolets.rxdiffutil;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Hods the {@link android.support.v7.util.DiffUtil.DiffResult} identified by the tag.
 */
public final class RxDiffResult {
    @NonNull
    private final String mTag;
    @NonNull
    private final DiffUtil.DiffResult mDiffResult;

    /**
     * Constructs a new instance of {@link RxDiffResult}.
     *
     * @param tag        A {@link String} identifying the result.
     * @param diffResult {@link android.support.v7.util.DiffUtil.DiffResult}.
     */
    public RxDiffResult(@NonNull final String tag, @NonNull final DiffUtil.DiffResult diffResult) {
        checkNotNull(diffResult, "diffResult must not be null!");
        checkNotNull(tag, "tag string must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        this.mTag = tag;
        this.mDiffResult = diffResult;
    }

    /**
     * @return A tag which corresponds to the appropriate diff request.
     */
    @NonNull
    public String getTag() {
        return mTag;
    }

    /**
     * @return {@link android.support.v7.util.DiffUtil.DiffResult}.
     */
    @NonNull
    public DiffUtil.DiffResult getDiffResult() {
        return mDiffResult;
    }

    @Override
    public String toString() {
        return "RxDiffResult{" +
                "mTag='" + mTag + '\'' +
                ", mDiffResult=" + mDiffResult +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RxDiffResult that = (RxDiffResult) o;

        return mTag.equals(that.mTag) &&
                mDiffResult.equals(that.mDiffResult);
    }

    @Override
    public int hashCode() {
        int result = mTag.hashCode();
        result = 31 * result + mDiffResult.hashCode();
        return result;
    }
}
