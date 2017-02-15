package com.stolets.rxdiffutil.diffrequest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.stolets.rxdiffutil.internal.Constants;
import com.stolets.rxdiffutil.util.ActivityUtils;
import com.stolets.rxdiffutil.util.SupportActivityUtils;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

/**
 * The class used to retrieve the diff request manager from the retained fragment.
 */
final class DiffRequestManagerRetriever {
    /**
     * Determines the activity type (system or from the support library) and retrieves the {@link DiffRequestManager} from the appropriate retained fragment.
     * @param activity {@link Activity} request lifecycle is bound to.
     * @return {@link DiffRequestManager}.
     */
    @NonNull
    static DiffRequestManager retrieve(@NonNull final Activity activity) {
        checkNotNull(activity, "activity must not be null");
        return findOrCreateManager(activity);
    }

    @NonNull
    private static DiffRequestManager findOrCreateManager(@NonNull final Activity activity) {
        if (activity instanceof AppCompatActivity) {
            final AppCompatActivity compatActivity = (AppCompatActivity) activity;
            return ((SupportDiffRequestManagerFragment) SupportActivityUtils.
                    findOrCreateSupportFragment(compatActivity.getSupportFragmentManager(), Constants.RETAINED_FRAGMENT_TAG)).
                    getDiffRequestManager();
        } else {
            return ((DiffRequestManagerFragment) ActivityUtils.
                    findOrCreateFragment(activity.getFragmentManager(), Constants.RETAINED_FRAGMENT_TAG)).
                    getDiffRequestManager();
        }
    }
}
