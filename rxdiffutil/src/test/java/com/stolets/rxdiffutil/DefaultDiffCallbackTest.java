package com.stolets.rxdiffutil;

import com.stolets.rxdiffutil.util.MockitoUtils;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DefaultDiffCallbackTest {
    private final DefaultDiffCallback<String, ExampleModel> callback = MockitoUtils.getMockedDefaultDiffCallback();

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
}
