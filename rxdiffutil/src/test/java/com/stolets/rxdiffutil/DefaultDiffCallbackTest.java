package com.stolets.rxdiffutil;

import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.util.MockitoUtils;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class DefaultDiffCallbackTest {
    private final DefaultDiffCallback<String, TestModel, TestAdapter<TestModel>> callback = MockitoUtils.getStubbedDefaultDiffCallback();

    @Test
    public void areContentsTheSame_ComparesModelData() {
        // Given the model objects

        // When
        boolean whenSameResult = callback.areContentsTheSame(0, 0);
        boolean whenDifferentResult = callback.areContentsTheSame(1, 1);

        // Then
        assertThat(whenSameResult, is(true));
        assertThat(whenDifferentResult, is(false));
    }

    @Test
    public void areItemsTheSame_ComparesModelIds() {
        // Given the model objects

        // When
        boolean whenDifferentResult = callback.areItemsTheSame(0, 0);
        boolean whenSameResult = callback.areItemsTheSame(1, 1);

        // Then
        assertThat(whenSameResult, is(true));
        assertThat(whenDifferentResult, is(false));
    }

    @Test
    public void testListSizes() {
        assertThat(callback.getOldListSize(), is(2));
        assertThat(callback.getNewListSize(), is(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void update_UpdatesAdapterData() {
        // Given
        final DiffUtil.DiffResult mockedDiffResult = mock(DiffUtil.DiffResult.class);
        final List<TestModel> mockedData = mock(List.class);
        final TestAdapter<TestModel> mockedAdapter = mock(TestAdapter.class);
        final DefaultDiffCallback<String, TestModel, TestAdapter<TestModel>> mockedDefaultDiffCallback = new DefaultDiffCallback<>(mockedData, mockedData, mockedAdapter);

        // When
        mockedDefaultDiffCallback.update(mockedDiffResult);

        // Then
        final InOrder inOrder = inOrder(mockedAdapter, mockedDiffResult);

        inOrder.verify(mockedAdapter).update(mockedData);
        inOrder.verify(mockedDiffResult).dispatchUpdatesTo(mockedAdapter);
    }
}
