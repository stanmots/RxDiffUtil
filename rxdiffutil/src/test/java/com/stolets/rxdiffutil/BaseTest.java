package com.stolets.rxdiffutil;

import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseTest {
    @BeforeClass
    public static void setupBaseBeforeClass() {
        RxJavaPlugins.onComputationScheduler(Schedulers.trampoline());

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }
    /**
     * The @Before methods of the base class will be run before those of the subclasses.
     */
    @Before
    public void setupBaseBefore() {
        MockitoAnnotations.initMocks(this);
    }
}
