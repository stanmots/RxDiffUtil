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

import android.app.Activity;
import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

public abstract class BaseRoboTest extends BaseTest {
    private Activity mActivity;
    private ActivityController<Activity> mActivityController;

    @Before
    public void setupBaseRoboTestBefore() {
        setActivityController(Robolectric.buildActivity(Activity.class));
        setActivity(getActivityController().create().get());
    }

    @After
    public void tearDownBaseRoboTestAfter() {
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
