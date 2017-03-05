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
import android.support.v7.widget.RecyclerView;

import com.stolets.rxdiffutil.Swappable;
import com.stolets.rxdiffutil.internal.Constants;

import java.util.Map;


import static com.stolets.rxdiffutil.internal.Preconditions.checkArgument;
import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;


/**
 * This class is used to hold {@link DiffRequestManager} instances that are bound to different {@link RecyclerView.Adapter}.
 */
@SuppressWarnings("WeakerAccess")
public final class DiffRequestManagerHolder {
    @NonNull
    private final Map<String, DiffRequestManager> mDiffRequestManagers;

    /**
     * Constructs a {@link DiffRequestManagerHolder}.
     *
     * @param managers {@link DiffRequestManager} instances that are bound to to different {@link RecyclerView.Adapter}. Each manager must be identified by the unique tag.
     * @throws NullPointerException If the given map with managers is null.
     */
    public DiffRequestManagerHolder(@NonNull final Map<String, DiffRequestManager> managers) {
        checkNotNull(managers, "managers must not be null!");
        this.mDiffRequestManagers = managers;
    }

    /**
     * Forwards the call to {@link DiffRequestManagerHolder#with(RecyclerView.Adapter, String)} with the default tag.
     */
    @NonNull
    public <D, A extends RecyclerView.Adapter & Swappable<D>> DiffRequestManager<D, A> with(@NonNull final A adapter) {
        return with(adapter, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG);
    }

    /**
     * Creates or retrieves the existing request manager and attaches to it the recycler view adapter reference obtained after the configuration change.
     *
     * @param adapter The recycler view adapter that is updated when the diff calculation is complete.
     * @param tag     The tag string identifying the manager.
     * @param <D>     Diff request data type.
     * @param <A>     The recycler view adapter type.
     * @return The {@link DiffRequestManager}.
     * @throws NullPointerException     If the adapter or the tag is null.
     * @throws IllegalArgumentException If the tag is empty.
     */
    @NonNull
    public <D, A extends RecyclerView.Adapter & Swappable<D>> DiffRequestManager<D, A> with(@NonNull final A adapter, @NonNull final String tag) {
        checkNotNull(adapter, "adapter must not be null!");
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        // Suppress compilation checks since there is no way to perform them
        // see https://stackoverflow.com/questions/22467645/
        @SuppressWarnings("unchecked") DiffRequestManager<D, A> diffRequestManager = mDiffRequestManagers.get(tag);

        // Check whether the adapter with the default tag exists
        if (diffRequestManager != null) {
            diffRequestManager.swapAdapter(adapter);
        } else {
            diffRequestManager = DiffRequestManager.create(adapter, tag);
            addManager(diffRequestManager, tag);
        }

        return diffRequestManager;
    }

    /**
     * Forwards the call to {@link DiffRequestManagerHolder#getManager(String)}.
     * @return {@link DiffRequestManager}.
     */
    @NonNull
    public DiffRequestManager getDefaultManager() {
        return getManager(Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG);
    }

    /**
     * Retrieves the {@link DiffRequestManager} from the manager map in accordance with the given tag.
     *
     * <p>
     *     Note: Do not use the manager returned from this method to update the adapter.
     * </p>
     *
     * @param tag The tag string which identifies the {@link DiffRequestManager}.
     * @return {@link DiffRequestManager}.
     * @throws NullPointerException     If the given tag is null.
     * @throws IllegalArgumentException If the given tag is empty.
     * @throws IllegalStateException    If the tag doesn't identify the existing manager.
     */
    @NonNull
    public DiffRequestManager getManager(@NonNull final String tag) {
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        final DiffRequestManager diffRequestManager = mDiffRequestManagers.get(tag);
        if (diffRequestManager == null) {
            throw new IllegalStateException("Failed to retrieve the manager matching the given tag. Probably you forgot to bind it to an activity.");
        }

        return diffRequestManager;
    }

    /**
     * Puts the given manager into the manager map.
     *
     * @param manager {@link DiffRequestManager}.
     * @param tag     The tag string identifying the given manager in the map so that it can be retrieved later.
     * @throws NullPointerException     if the manager or the tag is null.
     * @throws IllegalArgumentException if the tag is empty.
     */
    void addManager(@NonNull final DiffRequestManager manager, @NonNull final String tag) {
        checkNotNull(manager, "manager must not be null!");
        checkNotNull(tag, "tag must not be null!");
        checkArgument(!tag.isEmpty(), "tag string must not be empty!");

        mDiffRequestManagers.put(tag, manager);
    }

    /**
     * Performs additional clean-up when the retained fragment is about to be destroyed.
     */
    void recycle() {
        for (DiffRequestManager manager : mDiffRequestManagers.values()) {
            manager.releaseResources();
        }

        mDiffRequestManagers.clear();
    }
}
