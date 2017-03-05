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

import com.stolets.rxdiffutil.BaseRoboTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class DiffRequestManagerHolderFragmentRoboTest extends BaseRoboTest {
    @Mock
    DiffRequestManagerHolder mDiffRequestManagerHolder;
    private DiffRequestManagerHolderFragment mFragment;

    @Before
    public void setup() {
        mFragment = DiffRequestManagerHolderFragment.newInstance(mDiffRequestManagerHolder);
    }

    @Test
    public void newInstance_InjectsDiffFragmentManager() {
        // Given mDiffRequestManagerFragment

        // When
        final DiffRequestManagerHolder holder = mFragment.getDiffRequestManagerHolder();

        // Then
        assertThat(holder, notNullValue());
    }


    @Test
    public void it_SetsRetainInstanceOnCreate() {
        // Given
        final DiffRequestManagerHolderFragment spyFragment = spy(DiffRequestManagerHolderFragment.class);

        // When
        spyFragment.onCreate(null);

        // Then
        then(spyFragment).should().setRetainInstance(true);
    }

    /**
     * Note: Robolectric trhows NPE after the onDestroy has been called.
     */
    @Test(expected = NullPointerException.class)
    public void it_CallsRecycleOnDestroy() {
        // Given a fragment

        // When
        mFragment.onDestroy();

        // Then
        then(mDiffRequestManagerHolder).should().recycle();
    }

    @Test
    public void onDestroyView_CallsConfigurationChange() {
        // Given
        final DiffRequestManagerHolderFragment spyFragment = spy(mFragment);

        // When
        spyFragment.onDestroyView();

        // Then
        then(mDiffRequestManagerHolder).should().configurationChanged();
    }
}
