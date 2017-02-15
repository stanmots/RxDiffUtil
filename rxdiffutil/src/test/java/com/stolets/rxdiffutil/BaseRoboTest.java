package com.stolets.rxdiffutil;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

public abstract class BaseRoboTest {
    private Activity mActivity;
    private ActivityController<Activity> mActivityController;

    @Before
    public void setupBase() {
        setActivityController(Robolectric.buildActivity(Activity.class));
        setActivity(getActivityController().create().get());
    }

    @After
    public void tearDownBase() {
        mActivityController.pause()
                .stop()
                .destroy();
    }

    @NonNull
    @SuppressWarnings("WeakerAccess")
    public Activity getActivity() {
        return mActivity;
    }

    @SuppressWarnings("WeakerAccess")
    public void setActivity(@NonNull final Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @SuppressWarnings("WeakerAccess")
    public ActivityController<Activity> getActivityController() {
        return mActivityController;
    }

    @SuppressWarnings("WeakerAccess")
    public void setActivityController(@NonNull final ActivityController<Activity> activityController) {
        mActivityController = activityController;
    }
}
