package com.stolets.rxdiffutil;

import android.app.Activity;
import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.diffrequest.DiffRequestBuilder;

import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class RxDiffUtilTest extends BaseTest {
    @Mock
    Activity mActivity;

    @Mock
    DiffUtil.Callback mCallback;

    @Test
    public void builder_ReturnsNotNullDiffRequestBuilder() {
        // Given an activity and a callback

        // When
        final DiffRequestBuilder builder = RxDiffUtil.with(mCallback);

        // Then
        assertThat(builder, notNullValue());
    }
}
