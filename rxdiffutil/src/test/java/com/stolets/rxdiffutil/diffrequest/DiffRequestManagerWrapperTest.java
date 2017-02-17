package com.stolets.rxdiffutil.diffrequest;

import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.BaseTest;
import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.RxDiffResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class DiffRequestManagerWrapperTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";
    private DiffRequestManager mDiffRequestManager;

    @Mock
    DefaultDiffCallback mDefaultDiffCallback;

    @Before
    public void setup() {
        mDiffRequestManager = new DiffRequestManager();
    }

    @Test
    public void calculate_WhenDefaultDiffCallbackSet_ReturnsNull() {
        // Given
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, mDefaultDiffCallback);
        mDiffRequestManager.addPendingRequest(diffRequest);

        final DiffRequestManagerWrapper diffRequestManagerWrapper = new DiffRequestManagerWrapper(mDiffRequestManager, TEST_TAG);

        // When
        final Single<RxDiffResult> single = diffRequestManagerWrapper.calculate();

        // Then
        assertThat(single, nullValue());
    }

    @Test
    public void calculate_WhenDefaultDiffCallbackNotSet_ReturnsSingle() {
        // Given
        final DiffUtil.Callback callback = mock(DiffUtil.Callback.class);
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, callback);
        mDiffRequestManager.addPendingRequest(diffRequest);

        final DiffRequestManagerWrapper diffRequestManagerWrapper = new DiffRequestManagerWrapper(mDiffRequestManager, TEST_TAG);

        // When
        final Single<RxDiffResult> single = diffRequestManagerWrapper.calculate();

        assertThat(single, notNullValue());

        final TestObserver<RxDiffResult> testObserver = new TestObserver<>();
        assert single != null;
        single.subscribe(testObserver);

        testObserver.assertNoErrors();
    }
}
