package com.stolets.rxdiffutil.diffrequest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * This class is used as a wrapper around {@link DiffRequestManager} to prevent memory leaks.
 */
public final class DiffRequestManagerWrapper {
    @Nullable
    private WeakReference<DiffRequestManager> mDiffRequestManager;
    @NonNull
    private String mTag;

    public DiffRequestManagerWrapper(@NonNull final String tag) {
        checkNotNull(tag, "tag string must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");
        this.mTag = tag;
    }

    public void attachDiffRequestManager(@NonNull final DiffRequestManager diffRequestManager) {
        checkNotNull(diffRequestManager, "diffRequestManager must not be null!");
        this.mDiffRequestManager = new WeakReference<DiffRequestManager>(diffRequestManager);
    }

    @NonNull
    public String getTag() {
        return mTag;
    }
}
