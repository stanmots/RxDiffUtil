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

package com.stolets.rxdiffutil.di;

import android.support.annotation.NonNull;

/**
 * The class used to hold Dagger's root component.
 */
public final class DaggerRootHolder {
    @NonNull
    private final RootComponent mRootComponent;

    private DaggerRootHolder() {
        mRootComponent = DaggerRootComponent.create();
    }

    private static class LazyHolder {
        private static final DaggerRootHolder INSTANCE = new DaggerRootHolder();
    }

    /**
     * Constructs the {@link DaggerRootHolder} instance in accordance with the Initialization-on-demand holder idiom.
     * @return {@link DaggerRootHolder}.
     */
    public static DaggerRootHolder getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * @return Dagger's root component.
     */
    @NonNull
    public RootComponent getRootComponent() {
        return mRootComponent;
    }
}
