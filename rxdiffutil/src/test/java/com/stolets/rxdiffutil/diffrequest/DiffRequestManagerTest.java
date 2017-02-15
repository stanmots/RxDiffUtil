package com.stolets.rxdiffutil.diffrequest;

import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.BaseTest;

import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class DiffRequestManagerTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";

    @Mock
    DiffUtil.Callback mCallback;

    @Test
    public void addPendingRequest_AddsRequestToMap() {
        // Given
        final DiffRequestManager manager = new DiffRequestManager();
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, mCallback);

        // When
        manager.addPendingRequest(diffRequest);

        // Then
        assertThat(manager.getPendingRequests().size(), is(1));
        assertThat(manager.getPendingRequests().get(TEST_TAG), notNullValue());
    }
}
