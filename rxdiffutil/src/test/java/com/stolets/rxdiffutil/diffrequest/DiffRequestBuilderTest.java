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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.BaseTest;
import com.stolets.rxdiffutil.util.MockitoUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class DiffRequestBuilderTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";
    private final DiffUtil.Callback mCallback = MockitoUtils.getStubbedDefaultDiffCallback();
    private DiffRequestBuilder mBuilder;

    @Mock
    Activity mActivity;

    @Mock
    FragmentManager mFragmentManager;

    @Mock
    DiffRequestManagerFragment mDiffRequestManagerFragment;

    @Mock
    DiffRequestManager mDiffRequestManager;

    @Before
    public void setup() {
        mBuilder = new DiffRequestBuilder(mCallback);
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
    public void tag_SetsRequestTag() {
        // Given a builder

        // When
        mBuilder.tag(TEST_TAG);

        // Then
        assertThat(mBuilder.getTag(), is(TEST_TAG));
    }

    @SuppressLint("CommitTransaction")
    @Test
    public void build_ReturnsRequestManagerWithTag() {
        // Given
        mBuilder.tag(TEST_TAG);

        given(mDiffRequestManagerFragment.getDiffRequestManager()).willReturn(mDiffRequestManager);
        given(mFragmentManager.findFragmentByTag(anyString())).willReturn(mDiffRequestManagerFragment);
        given(mActivity.getFragmentManager()).willReturn(mFragmentManager);

        // When
        final DiffRequestManagerWrapper rxRequestManager = mBuilder.bindTo(mActivity);

        // Then
        assertThat(rxRequestManager.getTag(), is(TEST_TAG));
    }
}
