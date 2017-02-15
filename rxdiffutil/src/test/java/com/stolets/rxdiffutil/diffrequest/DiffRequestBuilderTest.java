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
    private DiffUtil.Callback mCallback = MockitoUtils.getMockedDefaultDiffCallback();
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
        mBuilder = new DiffRequestBuilder(mActivity, mCallback);
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
        final DiffRequestManagerWrapper rxRequestManager = mBuilder.build();

        // Then
        assertThat(rxRequestManager.getTag(), is(TEST_TAG));
    }
}
