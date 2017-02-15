package com.stolets.rxdiffutil.util;

import android.app.Fragment;
import android.app.FragmentManager;

import com.stolets.rxdiffutil.BaseRoboTest;
import com.stolets.rxdiffutil.internal.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(RobolectricTestRunner.class)
public class ActivityUtilsRoboTest extends BaseRoboTest {
    private static final String TEST_TAG = "TEST_TAG";
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Before
    public void setup() {
        mFragment = new Fragment();
        mFragmentManager = getActivity().getFragmentManager();
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

        ActivityUtils.addFragmentToActivity(mFragmentManager, mFragment, Constants.RETAINED_FRAGMENT_TAG);

        // When
        Fragment retrievedFragment = ActivityUtils.findOrCreateFragment(mFragmentManager, Constants.RETAINED_FRAGMENT_TAG);

        // Then
        assertThat(retrievedFragment, notNullValue());
        assertThat(retrievedFragment.getTag(), is(Constants.RETAINED_FRAGMENT_TAG));
    }
}
