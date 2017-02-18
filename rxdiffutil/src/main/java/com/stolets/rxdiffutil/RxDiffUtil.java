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

package com.stolets.rxdiffutil;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.diffrequest.DiffRequestBuilder;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * Represents an entry point for making diff requests.
 */
@SuppressWarnings("WeakerAccess")
public final class RxDiffUtil {

    /**
     * Creates the diff request builder that is used to configure request parameters.
     *
     * @param diffCallback A concrete implementation of {@link DiffUtil.Callback}.
     * @return {@link DiffRequestBuilder}.
     */
    @NonNull
    public static DiffRequestBuilder with(@NonNull final DiffUtil.Callback diffCallback) {
        checkNotNull(diffCallback, "diffCallback must not be null!");

        return new DiffRequestBuilder(diffCallback);
    }
}
