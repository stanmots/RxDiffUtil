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

import android.support.v7.util.DiffUtil;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.stolets.rxdiffutil.BaseRoboTest;
import com.stolets.rxdiffutil.RxDiffResult;
import com.stolets.rxdiffutil.TestAdapter;
import com.stolets.rxdiffutil.TestModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

import static com.stolets.rxdiffutil.util.MockitoUtils.TEST_TAG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
public class DiffRequestManagerRoboTest extends BaseRoboTest {
    private final List<TestModel> mNewData = new ArrayList<>();
    @Mock
    DiffUtil.Callback mCallback;
    @Mock
    DiffUtil.DiffResult mDiffResult;
    @Mock
    TestAdapter<TestModel> mTestAdapter;
    @Mock
    CompositeDisposable mCompositeDisposable;
    @Mock
    DiffRequestManager.DiffResultSubscriber mDiffResultSubscriber;
    private RxDiffResult mRxDiffResult;
    private DiffRequest<TestModel> mDiffRequest;
    private DiffRequestManager<TestModel, TestAdapter<TestModel>> mDiffRequestManager;

    @Before
    public void setup() {
        mRxDiffResult = new RxDiffResult(TEST_TAG, mDiffResult);
        mDiffRequest = new DiffRequest<>(true, TEST_TAG, mNewData, mCallback);
        mDiffRequestManager = new DiffRequestManager<>(mTestAdapter, TEST_TAG, PublishRelay.<RxDiffResult>create(), mCompositeDisposable);
        mDiffRequestManager.setDiffResultSubscriber(mDiffResultSubscriber);
    }

    @Test
    public void create_ReturnsNonNullManager() {
        // Given DiffRequestManager class

        // When
        final DiffRequestManager<TestModel, TestAdapter<TestModel>> manager = DiffRequestManager.create(mTestAdapter, TEST_TAG);

        // Then
        assertThat(manager, notNullValue());
    }

    @Test
    public void swapAdapter_ReplacesCurrentAdapter() {
        // Given
        final TestAdapter<TestModel> newAdapter = new TestAdapter<>();

        // When
        mDiffRequestManager.swapAdapter(newAdapter);

        // Then
        assertThat(mDiffRequestManager.getAdapter(), notNullValue());
        assertThat(mDiffRequestManager.getAdapter(), equalTo(newAdapter));
    }

    @Test
    public void single_ReturnsSharedSingle() {
        // Given
        final DiffRequest spyRequest = spy(mDiffRequest);

        // When
        final Single<RxDiffResult> single = DiffRequestManager.single(spyRequest);

        // Then
        for (int i = 0; i < 5; ++i) {
            final TestObserver<RxDiffResult> testObserver = new TestObserver<>();

            single.subscribe(testObserver);

            testObserver.awaitTerminalEvent(500, TimeUnit.MILLISECONDS);
            testObserver.assertValue(new Predicate<RxDiffResult>() {
                @Override
                public boolean test(@NonNull RxDiffResult rxDiffResult) throws Exception {
                    return rxDiffResult.getTag().equals(TEST_TAG);
                }
            });
        }

        then(spyRequest).should(times(1)).isDetectingMoves();
    }

    @Test
    public void receive_TriesToUpdateAdapter() {
        // Given
        final DiffRequestManager spyManager = spy(mDiffRequestManager);

        // When
        spyManager.receive(mRxDiffResult);

        // Then
        then(spyManager).should().update(mDiffResult);
    }

    @Test
    public void receive_WhenNoObservers_CachesResult() {
        // Given
        final Relay mockRelay = mock(PublishRelay.class);
        given(mockRelay.hasObservers()).willReturn(false);

        // When
        mDiffRequestManager.receive(mRxDiffResult);

        // Then
        assertThat(mDiffRequestManager.getCachedResultForSubscription(), notNullValue());
    }

    @Test
    public void diffResults_WhenNoSubscribers_CachesResult() {
        // Given
        mDiffRequestManager.receive(mRxDiffResult);

        // When
        final TestObserver<RxDiffResult> testObserver = new TestObserver<>();
        mDiffRequestManager.diffResults()
                .subscribe(testObserver);

        // Then
        testObserver.assertValue(mRxDiffResult);
    }

    @Test
    public void diffResults_OnSubscription_EmitsCachedResult() throws Exception {
        // Given
        mDiffRequestManager.setCachedResultForSubscription(mRxDiffResult);

        // When
        final TestObserver<RxDiffResult> testObserver = new TestObserver<>();
        mDiffRequestManager.diffResults()
                .subscribe(testObserver);

        // Then
        testObserver.assertValue(mRxDiffResult);
        assertThat(mDiffRequestManager.getCachedResultForSubscription(), nullValue());
    }

    @Test
    public void update_SwapsDataAndDispatchesUpdatesInOrder() {
        // Given
        mDiffRequestManager.setCurrentDiffRequest(mDiffRequest);

        // When
        mDiffRequestManager.update(mDiffResult);

        // Then
        final InOrder inOrder = inOrder(mTestAdapter, mDiffResult);
        inOrder.verify(mTestAdapter).swapData(mNewData);
        inOrder.verify(mDiffResult).dispatchUpdatesTo(mTestAdapter);

        assertThat(mDiffRequestManager.getCurrentDiffRequest(), nullValue());
        assertThat(mDiffRequestManager.getPendingResult(), nullValue());
    }

    @Test
    public void update_WhenNewDataNull_DoesntSwapAdapterData() {
        // Given the diff request with the null new data
        final DiffRequest<TestModel> diffRequest = new DiffRequest<>(true, TEST_TAG, null, mCallback);
        mDiffRequestManager.setCurrentDiffRequest(diffRequest);

        // When
        mDiffRequestManager.update(mDiffResult);

        // Then
        then(mTestAdapter).shouldHaveZeroInteractions();
    }

    @Test
    public void update_WhenRequestNull_DoesntSwapAdapterData() {
        // Given
        mDiffRequestManager.setCurrentDiffRequest(null);

        // When
        mDiffRequestManager.update(mDiffResult);

        // Then
        then(mTestAdapter).shouldHaveZeroInteractions();
    }

    @Test
    public void update_WhenNewDataNonNullAndAdapterNull_SavesPendingResult() {
        // Given a non null current request
        mDiffRequestManager.setCurrentDiffRequest(mDiffRequest);
        mDiffRequestManager.swapAdapter(null);

        // When
        mDiffRequestManager.update(mDiffResult);

        // Then
        assertThat(mDiffRequestManager.getPendingResult(), notNullValue());
        assertThat(mDiffRequestManager.getPendingResult(), equalTo(mDiffResult));
    }

    @Test
    public void updateAdapterIfNeeded_WhenPendingResultNonNull_UpdatesAdapter() {
        // Given
        DiffRequestManager spyManager = spy(mDiffRequestManager);
        spyManager.setPendingResult(mDiffResult);

        // When
        spyManager.updateAdapterIfNeeded();

        // Then
        then(spyManager).should().update(mDiffResult);
    }

    @Test
    public void newDiffRequestWith_ReturnsNonNullBuilder() {
        // Given a manager

        // When
        final DiffRequestBuilder builder = mDiffRequestManager.newDiffRequestWith(mCallback);

        // Then
        assertThat(builder, notNullValue());
    }

    @Test
    public void execute_ClearsAndAddsSubscriptionInOrder() {
        // Given a manager

        // When
        mDiffRequestManager.execute(mDiffRequest);

        // Then
        final InOrder inOrder = inOrder(mCompositeDisposable);
        inOrder.verify(mCompositeDisposable).clear();
        inOrder.verify(mCompositeDisposable).add(any(Disposable.class));
    }

    @Test
    public void releaseResources_ClearsSubscriptions() {
        // Given a manager

        // When
        mDiffRequestManager.releaseResources();

        // Then
        then(mCompositeDisposable).should().clear();
    }

    @Test
    public void getTag_ReturnsCorrectTag() {
        assertThat(mDiffRequestManager.getTag(), equalTo(TEST_TAG));
    }

    @Test
    public void setCurrentDiffRequest_SetsCorrectRequest() {
        // Given a diff request

        // When
        mDiffRequestManager.setCurrentDiffRequest(mDiffRequest);

        // Then
        assertThat(mDiffRequestManager.getCurrentDiffRequest(), equalTo(mDiffRequest));
    }

    @Test
    public void defaultSubscriber_ReturnsResultToReceiver() throws Exception {
        // Given
        final DiffRequestManager spyManager = spy(mDiffRequestManager);
        final DiffRequestManager.DiffResultSubscriber diffResultSubscriber = new DiffRequestManager.DiffResultSubscriber(spyManager);

        // When
        diffResultSubscriber.accept(mRxDiffResult, new Throwable());

        // Then
        then(spyManager).should().receive(mRxDiffResult);
    }

    @Test(expected = IllegalStateException.class)
    public void defaultSubscriber_WhenThrowableHasMessage_ThenThrows() throws Exception {
        // Given
        final DiffRequestManager.DiffResultSubscriber diffResultSubscriber = new DiffRequestManager.DiffResultSubscriber(mDiffRequestManager);

        // When
        diffResultSubscriber.accept(mRxDiffResult, new Throwable(""));

        // Then throws IllegalStateException
    }

    @Test(expected = IllegalStateException.class)
    public void defaultSubscriber_WhenResultAndThrowableNull_ThenThrows() throws Exception {
        // Given
        final DiffRequestManager.DiffResultSubscriber diffResultSubscriber = new DiffRequestManager.DiffResultSubscriber(mDiffRequestManager);

        // When
        diffResultSubscriber.accept(null, null);

        // Then throws IllegalStateException
    }
}
