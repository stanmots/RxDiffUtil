package com.stolets.rxdiffutil.diffrequest;

import android.support.v7.util.DiffUtil;

import com.stolets.rxdiffutil.BaseTest;
import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.RxDiffResult;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class DiffRequestManagerTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";
    private DiffRequestManager mDiffRequestManager;

    @BeforeClass
    public static void setupClass() {
        RxJavaPlugins.onComputationScheduler(Schedulers.trampoline());

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @Before
    public void setup() {
        mDiffRequestManager = new DiffRequestManager();
    }

    @Mock
    DiffUtil.Callback mCallback;

    @Test
    public void addPendingRequest_AddsRequestToMap() {
        // Given
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, mCallback);

        // When
        mDiffRequestManager.addPendingRequest(diffRequest);

        // Then
        assertThat(mDiffRequestManager.getPendingRequests().size(), is(1));
        assertThat(mDiffRequestManager.getPendingRequests().get(TEST_TAG), notNullValue());
    }

    @Test
    public void execute_GiveDiffUtilCallback_ReturnsNull() {
        // Given
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, mCallback);
        mDiffRequestManager.addPendingRequest(diffRequest);

        // When
        final Single<RxDiffResult> single = mDiffRequestManager.execute(TEST_TAG);

        // Then
        assertThat(single, notNullValue());

        final TestObserver<RxDiffResult> testObserver = new TestObserver<>();
        assert single != null;
        single.subscribe(testObserver);

        testObserver.assertValue(new Predicate<RxDiffResult>() {
            @Override
            public boolean test(@NonNull RxDiffResult rxDiffResult) throws Exception {
                return rxDiffResult.getTag().equals(TEST_TAG);
            }
        });
    }

    @Test
    public void execute_GivenDefaultDiffCallback_ReturnsSingle() {
        // Given
        final DefaultDiffCallback defaultDiffCallback = mock(DefaultDiffCallback.class);
        final DiffRequest diffRequest = new DiffRequest(true, TEST_TAG, defaultDiffCallback);

        mDiffRequestManager.addPendingRequest(diffRequest);

        // When
        final Single<RxDiffResult> single = mDiffRequestManager.execute(TEST_TAG);

        // Then
        assertThat(single, nullValue());
    }
}
