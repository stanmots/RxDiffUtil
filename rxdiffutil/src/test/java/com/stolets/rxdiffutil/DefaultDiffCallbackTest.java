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

        inOrder.verify(mockedAdapter).swapData(mockedData);
        inOrder.verify(mockedDiffResult).dispatchUpdatesTo(mockedAdapter);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getOldData_ReturnsUnmodifiableList() {
        callback.getOldData().add(new TestModel("data", "tag"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getNewData_ReturnsUnmodifiableList() {
        callback.getNewData().add(new TestModel("data", "tag"));
    }
}
