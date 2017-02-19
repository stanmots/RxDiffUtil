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
 * The view-less retained fragment that is used to hold {@link DiffRequestManager}.
 */
public class SupportDiffRequestManagerFragment extends Fragment {
    private DiffRequestManager mDiffRequestManager;

    public static SupportDiffRequestManagerFragment newInstance(@NonNull final DiffRequestManager diffRequestManager) {
        final SupportDiffRequestManagerFragment supportDiffRequestManagerFragment = new SupportDiffRequestManagerFragment();
        supportDiffRequestManagerFragment.setDiffRequestManager(diffRequestManager);
        return supportDiffRequestManagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        mDiffRequestManager.releaseResources();
        super.onDestroy();
    }

    /**
     * @return {@link DiffRequestManager} instance.
     */
    @NonNull
    public DiffRequestManager getDiffRequestManager() {
        return mDiffRequestManager;
    }

    /**
     * Used to inject {@link DiffRequestManager}.
     *
     * @param diffRequestManager {@link DiffRequestManager}.
     */
    public void setDiffRequestManager(@NonNull final DiffRequestManager diffRequestManager) {
        checkNotNull(diffRequestManager, "diffRequestManager must not be null!");
        this.mDiffRequestManager = diffRequestManager;
    }
}
