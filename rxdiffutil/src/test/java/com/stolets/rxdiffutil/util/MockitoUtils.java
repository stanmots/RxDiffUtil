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

package com.stolets.rxdiffutil.util;


import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.TestAdapter;
import com.stolets.rxdiffutil.TestModel;
import com.stolets.rxdiffutil.diffrequest.DiffRequest;
import com.stolets.rxdiffutil.diffrequest.DiffRequestManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public final class MockitoUtils {
    @SuppressWarnings("WeakerAccess")
    public static final String TEST_TAG = "TEST_TAG";

    public static DefaultDiffCallback<String, TestModel, TestAdapter<TestModel>> getStubbedDefaultDiffCallback() {
        final List<TestModel> mOldList = Arrays.asList(new TestModel("s1", "id1"), new TestModel("s2", "id2"));
        final List<TestModel> mNewList = Arrays.asList(new TestModel("s1", "id10"), new TestModel("s3", "id2"));

        return new DefaultDiffCallback<>(mOldList, mNewList, new TestAdapter<TestModel>());
    }

    public static DiffRequestManager getStubbedDiffRequestManager() {
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, MockitoUtils.getStubbedDefaultDiffCallback());
        final Map<String, DiffRequest> requestMap = new HashMap<>();
        requestMap.put(TEST_TAG, diffRequest);


        return new DiffRequestManager(requestMap,
                new HashMap<String, Disposable>(), new CompositeDisposable());
    }
}
