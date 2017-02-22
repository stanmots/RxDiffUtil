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

import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.BaseTest;
import com.stolets.rxdiffutil.RxDiffResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class DiffRequestManagerTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";
    @Mock
    DiffUtil.Callback mCallback;
    private DiffRequestManager mDiffRequestManager;

    @Before
    public void setup() {
        mDiffRequestManager = new DiffRequestManager();
    }

    @Test
    public void addPendingRequest_AddsRequestToMap() {
        // Given
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, mCallback);

        // When
        mDiffRequestManager.addPendingRequest(diffRequest);

        // Then
        assertThat(mDiffRequestManager.getPendingRequests().size(), is(1));
        assertThat(mDiffRequestManager.getPendingRequests().get(TEST_TAG), notNullValue());
    }

    @Test
    public void execute_RemovesPendingRequest() {
        // Given
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, mCallback);
        mDiffRequestManager.addPendingRequest(diffRequest);

        // When
        mDiffRequestManager.execute(TEST_TAG);

        // Then
        assertThat(mDiffRequestManager.getPendingRequests().size(), is(0));
    }

    @Test
    public void execute_ReturnsSharedSingle() {
        // Given
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, mCallback);
        mDiffRequestManager.addPendingRequest(diffRequest);

        // When
        final Single<RxDiffResult> single = mDiffRequestManager.execute(TEST_TAG);

        // Then
        assertThat(single, notNullValue());

        final TestObserver<RxDiffResult> testObserver = new TestObserver<>();
        single.subscribe(testObserver);

        testObserver.awaitTerminalEvent();

        testObserver.assertValue(new Predicate<RxDiffResult>() {
            @Override
            public boolean test(@NonNull RxDiffResult rxDiffResult) throws Exception {
                return rxDiffResult.getTag().equals(TEST_TAG);
            }
        });
    }
}
