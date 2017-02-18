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
import com.stolets.rxdiffutil.internal.Constants;

import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DiffRequestTest extends BaseTest {
    @Mock
    private DiffUtil.Callback mDataComparable;

    @Test
    public void it_ImplementsEquivalenceRelation() {
        // Given
        final DiffRequest diffRequest1 = new DiffRequest(true, Constants.DIFF_REQUEST_DEFAULT_TAG, mDataComparable);
        final DiffRequest diffRequest2 = new DiffRequest(true, Constants.DIFF_REQUEST_DEFAULT_TAG, mDataComparable);

        // When diffRequest1 is equal to diffRequest2

        // Then it is
        // reflexive
        assertThat(diffRequest1, equalTo(diffRequest1));

        // symmetric
        assertThat(diffRequest1, equalTo(diffRequest2));
        assertThat(diffRequest2, equalTo(diffRequest1));

        // transitive
        final DiffRequest diffRequest3 = new DiffRequest(true, Constants.DIFF_REQUEST_DEFAULT_TAG, mDataComparable);
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
}