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
import com.stolets.rxdiffutil.TestModel;
import com.stolets.rxdiffutil.internal.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class DiffRequestTest extends BaseTest {
    @Mock
    private DiffUtil.Callback mDataComparable;
    private DiffRequest<TestModel> mDiffRequest;
    private List<TestModel> mNewData = new ArrayList<>();

    @Before
    public void setup() {
        mDiffRequest = new DiffRequest<>(true, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG, mNewData, mDataComparable);
    }

    @Test
    public void it_ImplementsEquivalenceRelation() {
        // Given
        final DiffRequest<String> diffRequest1 = new DiffRequest<>(true, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG, null, mDataComparable);
        final DiffRequest<String> diffRequest2 = new DiffRequest<>(true, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG, null,mDataComparable);

        // When diffRequest1 is equal to diffRequest2

        // Then it is
        // reflexive
        assertThat(diffRequest1, equalTo(diffRequest1));

        // symmetric
        assertThat(diffRequest1, equalTo(diffRequest2));
        assertThat(diffRequest2, equalTo(diffRequest1));

        // transitive
        final DiffRequest<String> diffRequest3 = new DiffRequest<>(true, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG, null, mDataComparable);
        assertThat(diffRequest1.equals(diffRequest3), is(true));
        assertThat(diffRequest2.equals(diffRequest3), is(true));
        assertThat(diffRequest1.equals(diffRequest2), is(true));

        // consistent
        assertThat(diffRequest1.equals(diffRequest2), is(true));
        assertThat(diffRequest1.hashCode(), equalTo(diffRequest2.hashCode()));

        // unequal to null
        //noinspection ObjectEqualsNull
        assertThat(diffRequest1.equals(null), is(false));
    }

    @Test
    public void toString_IsCorrect() {
        assertThat(new DiffRequest<String>(true, Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG, null, mDataComparable).toString(), startsWith("DiffRequest"));
    }

    @Test
    public void getDiffCallback_ReturnsCorrectCallback() {
        assertThat(mDiffRequest.getDiffCallback(), equalTo(mDataComparable));
    }

    @Test
    public void getTag_ReturnsCorrectTag() {
        assertThat(mDiffRequest.getTag(), equalTo(Constants.DIFF_REQUEST_MANAGER_DEFAULT_TAG));
    }

    @Test
    public void isDetectingMoves_ReturnsCorrectBoolean() {
        assertThat(mDiffRequest.isDetectingMoves(), equalTo(true));
    }

    @Test
    public void getNewData_ReturnsCorrectNewData() {
        assertThat(mDiffRequest.getNewData(), equalTo(mNewData));
    }
}