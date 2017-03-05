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

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.BaseTest;
import com.stolets.rxdiffutil.TestAdapter;
import com.stolets.rxdiffutil.TestModel;
import com.stolets.rxdiffutil.util.MockitoUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class DiffRequestBuilderTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";
    private final DiffUtil.Callback mCallback = MockitoUtils.getStubbedDefaultDiffCallback();
    @Mock
    Activity mActivity;
    @Mock
    FragmentManager mFragmentManager;
    @Mock
    DiffRequestManagerHolderFragment mDiffRequestManagerFragment;
    @Mock
    DiffRequestManager<TestModel, TestAdapter<TestModel>> mDiffRequestManager;
    private DiffRequestBuilder<TestModel> mBuilder;

    @Before
    public void setup() {
        given(mDiffRequestManager.getTag()).willReturn(TEST_TAG);
        mBuilder = new DiffRequestBuilder<>(mDiffRequestManager, mCallback);
    }

    @Test
    public void detectMoves_SetsDetectMovesFlag() {
        // Given a builder

        // When
        mBuilder.detectMoves(true);

        // Then
        assertThat(mBuilder.isDetectingMoves(), is(true));
    }

    @Test
    public void create_ReturnsNonNullBuilder() {
        final DiffRequestBuilder<TestModel> builder = DiffRequestBuilder.create(mDiffRequestManager, mCallback);
        assertThat(builder, notNullValue());
    }

    @Test
    public void updateAdapterWithNewData_SetsNewAdapterData() {
        // Given
        final List<TestModel> testList = new ArrayList<>();

        // When
        mBuilder.updateAdapterWithNewData(testList);

        // Then
        assertThat(mBuilder.getNewData(), equalTo(testList));
    }

    @Test
    public void calculate_CreatesDiffRequest() {
        // Given a builder

        // When
        mBuilder.calculate();

        // Then
        then(mDiffRequestManager).should().execute(any(DiffRequest.class));
    }
}
