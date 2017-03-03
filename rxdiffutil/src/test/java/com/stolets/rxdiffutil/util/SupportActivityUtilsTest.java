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

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.stolets.rxdiffutil.BaseTest;
import com.stolets.rxdiffutil.diffrequest.SupportDiffRequestManagerHolderFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.stolets.rxdiffutil.util.MockitoUtils.TEST_TAG;
import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


public class SupportActivityUtilsTest extends BaseTest {
    @Mock
    FragmentManager mFragmentManager;
    @Mock
    Fragment mFragment;
    @Mock
    FragmentTransaction mFragmentTransaction;

    @SuppressLint("CommitTransaction")
    @Before
    public void setup() {
        given(mFragmentManager.beginTransaction()).willReturn(mFragmentTransaction);
        given(mFragmentTransaction.remove(mFragment)).willReturn(mFragmentTransaction);
    }

    @Test
    public void it_IsUtilityClass() throws Exception {
        assertUtilityClassWellDefined(SupportActivityUtils.class);
    }

    @Test
    public void addSupportFragmentToActivity_AddsNewFragment() {
        // Given the support fragment and the fragment manager

        // When
        SupportActivityUtils.addSupportFragmentToActivity(mFragmentManager, mFragment, TEST_TAG);

        // Then
        then(mFragmentTransaction).should().add(mFragment, TEST_TAG);
    }

    @Test
    public void addSupportFragmentToActivity_WhenFragmentWithGivenTagExists_ThenRemovesDuplicate() {
        // Given
        given(mFragmentManager.findFragmentByTag(TEST_TAG)).willReturn(mFragment);

        // When
        SupportActivityUtils.addSupportFragmentToActivity(mFragmentManager, mFragment, TEST_TAG);

        // Then
        then(mFragmentTransaction).should().remove(mFragment);
    }

    @Test
    public void findOrCreateSupportFragment_WhenFragmentFound_ReturnsIt() {
        // Given the support fragment and the fragment manager
        given(mFragmentManager.findFragmentByTag(TEST_TAG)).willReturn(mFragment);

        // When
        final Fragment retrievedFragment = SupportActivityUtils.findOrCreateSupportFragment(mFragmentManager, TEST_TAG);

        // Then
        assertThat(retrievedFragment, notNullValue());
        assertThat(retrievedFragment, is(mFragment));
    }

    @Test
    public void findOrCreateSupportFragment_WhenFragmentNotFound_CreatesNewFragment() {
        // Given the fragment manager

        // When
        final Fragment retrievedFragment = SupportActivityUtils.findOrCreateSupportFragment(mFragmentManager, TEST_TAG);

        // Then
        assertThat(retrievedFragment, notNullValue());
        assertThat(retrievedFragment, instanceOf(SupportDiffRequestManagerHolderFragment.class));
    }
}

