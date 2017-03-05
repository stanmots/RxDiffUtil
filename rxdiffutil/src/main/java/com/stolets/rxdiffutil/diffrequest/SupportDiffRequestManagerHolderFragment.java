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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;


/**
 * The view-less retained fragment that is used to hold {@link DiffRequestManagerHolder}.
 */
public class SupportDiffRequestManagerHolderFragment extends Fragment {
    @Nullable
    private DiffRequestManagerHolder mDiffRequestManagerHolder;

    public static SupportDiffRequestManagerHolderFragment newInstance(@NonNull final DiffRequestManagerHolder diffRequestManagerHolder) {
        final SupportDiffRequestManagerHolderFragment fragment = new SupportDiffRequestManagerHolderFragment();
        fragment.setDiffRequestManagerHolder(diffRequestManagerHolder);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        getDiffRequestManagerHolder().recycle();
        super.onDestroy();
    }

    /**
     * @return {@link DiffRequestManagerHolder} instance.
     * @throws IllegalStateException If the holder is null.
     */
    @NonNull
    public DiffRequestManagerHolder getDiffRequestManagerHolder() {
        if (mDiffRequestManagerHolder == null) {
            throw new IllegalStateException("The DiffRequestManagerHolder must not be null. Ensure that you are initializing the fragment using newInstance() method");
        }

        return mDiffRequestManagerHolder;
    }

    /**
     * Used to inject {@link DiffRequestManagerHolder}.
     *
     * @param diffRequestManagerHolder {@link DiffRequestManagerHolder}.
     */
    public void setDiffRequestManagerHolder(@NonNull final DiffRequestManagerHolder diffRequestManagerHolder) {
        checkNotNull(diffRequestManagerHolder, "diffRequestManagerHolder must not be null!");
        this.mDiffRequestManagerHolder = diffRequestManagerHolder;
    }
}
