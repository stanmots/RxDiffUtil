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
import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.TestAdapter;
import com.stolets.rxdiffutil.TestModel;
import com.stolets.rxdiffutil.internal.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static com.stolets.rxdiffutil.util.MockitoUtils.TEST_TAG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;


public class DiffRequestManagerHolderTest extends BaseTest {
    @Mock
    DiffRequestManager<String, TestAdapter<String>> mDiffRequestManager;
    @Mock
    TestAdapter<TestModel> mTestAdapter;
    @Mock
    DiffUtil.DiffResult mDiffResult;

    private Map<String, DiffRequestManager> mDiffRequestManagers;
    private DiffRequestManagerHolder mDiffRequestManagerHolder;

    @Mock
    DefaultDiffCallback mDefaultDiffCallback;

    @Before
    public void setup() {
        mDiffRequestManagers = new HashMap<>();
        mDiffRequestManagers.put(TEST_TAG, mDiffRequestManager);
        mDiffRequestManagerHolder = new DiffRequestManagerHolder(mDiffRequestManagers);
    }

    @Test
    public void getDefaultManager_ReturnsManagerWithDefaultTag() {
        // Given
        given(mDiffRequestManager.getTag()).willReturn(Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG);
        mDiffRequestManagerHolder.addManager(mDiffRequestManager, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG);

        // When
        final DiffRequestManager diffRequestManager = mDiffRequestManagerHolder.getDefaultManager();

        // Then
        assertThat(diffRequestManager.getTag(), equalTo(Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG));
    }

    @Test
    public void getManager_ReturnsNonNullManager() {
        // Given a DiffRequestManagerHolder

        // When
        final DiffRequestManager diffRequestManager = mDiffRequestManagerHolder.getManager(TEST_TAG);

        // Then
        assertThat(diffRequestManager, notNullValue());
    }

    @Test
    public void addManager_AddsManagerToMap() {
        // Given a DiffRequestManagerHolder

        // When
        mDiffRequestManagerHolder.addManager(mDiffRequestManager, "TEST_TAG2");

        // Then
        assertThat(mDiffRequestManagers.size(), equalTo(2));
    }

    @Test
    public void with_ForwardsCallWithDefaultTag() {
        // Given
        final DiffRequestManagerHolder spyDiffRequestManagerHolder = spy(mDiffRequestManagerHolder);

        // When
        spyDiffRequestManagerHolder.with(mTestAdapter);

        // Then
        then(spyDiffRequestManagerHolder).should().with(mTestAdapter, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG);
    }

    @Test
    public void with_WhenManagerNull_CreatesNewManager() {
        // Given
        mDiffRequestManagerHolder.recycle();

        // When
        final DiffRequestManager<TestModel, TestAdapter<TestModel>> diffRequestManager = mDiffRequestManagerHolder.with(mTestAdapter, TEST_TAG);

        // Then
        assertThat(diffRequestManager, notNullValue());
    }

    @Test
    public void with_WhenManagerExists_SwapsAdapter() {
        // Given
        final DiffRequestManager<TestModel, TestAdapter<TestModel>> diffRequestManager = DiffRequestManager.create(mTestAdapter, TEST_TAG);
        final DiffRequestManager<TestModel, TestAdapter<TestModel>> spyDiffRequestManage = spy(diffRequestManager);
        mDiffRequestManagerHolder.addManager(spyDiffRequestManage, TEST_TAG);

        // When
        mDiffRequestManagerHolder.with(mTestAdapter, TEST_TAG);

        // Then
        then(spyDiffRequestManage).should().swapAdapter(mTestAdapter);
    }

    @Test
    public void recycle_ReleasesResourcesOfEachManager() {
        // Given a DiffRequestManagerHolder

        // When
        mDiffRequestManagerHolder.recycle();

        // Then
        then(mDiffRequestManager).should().releaseResources();
    }

    @Test
    public void configurationChanged_ClearsCurrentAdapters() {
        // Given the holder

        // When
        mDiffRequestManagerHolder.configurationChanged();

        // Then
        then(mDiffRequestManager).should().swapAdapter(null);
    }
}
