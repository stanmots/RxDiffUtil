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

import android.support.v7.util.DiffUtil;

import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RxDIffResultTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";
    @Mock
    DiffUtil.DiffResult mDiffResult;

    @Test
    public void it_ImplementsEquivalenceRelation() {
        // Given
        final RxDiffResult diffResult1 = new RxDiffResult(TEST_TAG, mDiffResult);
        final RxDiffResult diffResult2 = new RxDiffResult(TEST_TAG, mDiffResult);

        // When diffResult1 is equal to diffResult2

        // Then it is
        // reflexive
        assertThat(diffResult1, equalTo(diffResult1));

        // symmetric
        assertThat(diffResult1, equalTo(diffResult2));
        assertThat(diffResult2, equalTo(diffResult1));

        // transitive
        final RxDiffResult diffResult3 = new RxDiffResult(TEST_TAG, mDiffResult);
        assertThat(diffResult1.equals(diffResult3), is(true));
        assertThat(diffResult2.equals(diffResult3), is(true));
        assertThat(diffResult1.equals(diffResult2), is(true));

        // consistent
        assertThat(diffResult1.equals(diffResult2), is(true));
        assertThat(diffResult1.hashCode(), equalTo(diffResult2.hashCode()));

        // unequal to null
        //noinspection ObjectEqualsNull
        assertThat(diffResult1.equals(null), is(false));
    }
}
