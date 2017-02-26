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

import android.app.Fragment;
import android.app.FragmentManager;

import com.stolets.rxdiffutil.BaseRoboTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.stolets.rxdiffutil.util.MockitoUtils.TEST_TAG;
import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(RobolectricTestRunner.class)
public class ActivityUtilsRoboTest extends BaseRoboTest {
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Before
    public void setup() {
        mFragment = new Fragment();
        mFragmentManager = getActivity().getFragmentManager();
    }

    @Test
    public void it_IsUtilityClass() throws Exception {
        assertUtilityClassWellDefined(ActivityUtils.class);
    }

    @Test
    public void addFragmentToActivity_AddsNewFragment() {
        // Given a fragment and the fragment manager

        // When
        ActivityUtils.addFragmentToActivity(mFragmentManager, mFragment, TEST_TAG);

        // Then
        assertThat(mFragmentManager.findFragmentByTag(TEST_TAG), notNullValue());
    }

    @Test
    public void findOrCreateFragment_RetrievesRetainedFragment() {
        // Given a fragment and the fragment manager
        ActivityUtils.addFragmentToActivity(mFragmentManager, mFragment, TEST_TAG);

        // When
        Fragment retrievedFragment = ActivityUtils.findOrCreateFragment(mFragmentManager, TEST_TAG);

        // Then
        assertThat(retrievedFragment, notNullValue());
        assertThat(retrievedFragment.getTag(), is(TEST_TAG));
    }

    @Test
    public void findOrCreateFragment_WhenFragmentNotFound_CreatesNewFragment() {
        // Given the fragment manager

        // When
        Fragment retrievedFragment = ActivityUtils.findOrCreateFragment(mFragmentManager, TEST_TAG);

        // Then
        assertThat(retrievedFragment, notNullValue());
        assertThat(retrievedFragment.getTag(), is(TEST_TAG));
    }

    @Test
    public void removeFragment_BeginsRemoveTransaction() {
        // Given a fragment and the fragment manager
        ActivityUtils.addFragmentToActivity(mFragmentManager, mFragment, TEST_TAG);

        // When
        ActivityUtils.removeFragment(mFragmentManager, TEST_TAG);
        mFragmentManager.executePendingTransactions();

        // Then
        assertThat(mFragmentManager.findFragmentByTag(TEST_TAG), nullValue());
    }
}
