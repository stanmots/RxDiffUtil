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
import com.stolets.rxdiffutil.util.SupportActivityUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.stolets.rxdiffutil.util.MockitoUtils.TEST_TAG;
import static com.stolets.rxdiffutil.util.MockitoUtils.getStubbedDiffRequestManager;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(RobolectricTestRunner.class)
public class SupportDiffRequestManagerFragmentTest extends BaseRoboTest {
    private SupportDiffRequestManagerFragment mSupportDiffRequestManagerFragment;

    @Before
    public void setup() {
        mSupportDiffRequestManagerFragment = SupportDiffRequestManagerFragment.newInstance(getStubbedDiffRequestManager());
    }

    @Test
    public void newInstance_InjectsDiffFragmentManager() {
        // Given mSupportDiffRequestManagerFragment

        // When
        final DiffRequestManager diffRequestManager = mSupportDiffRequestManagerFragment.getDiffRequestManager();

        // Then
        assertThat(diffRequestManager, notNullValue());
        assertThat(diffRequestManager.getPendingRequests().size(), is(1));
    }

    @Test
    public void it_SurvivesConfigurationChange() {
        // Given
        SupportActivityUtils.addSupportFragmentToActivity(getActivity().getSupportFragmentManager(), mSupportDiffRequestManagerFragment, TEST_TAG);

        // When
        getActivity().recreate();

        // Then
        assertThat(getActivity().getSupportFragmentManager().findFragmentByTag(TEST_TAG), notNullValue());
    }
}
