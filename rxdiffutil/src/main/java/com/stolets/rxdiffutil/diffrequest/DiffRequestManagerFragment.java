package com.stolets.rxdiffutil.diffrequest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.stolets.rxdiffutil.util.DaggerUtils;

import javax.inject.Inject;

/**
 * The view-less retained fragment that is used to hold {@link DiffRequestManager}.
 */
public class DiffRequestManagerFragment extends Fragment {
    @Inject
    DiffRequestManager mDiffRequestManager;

    public static DiffRequestManagerFragment newInstance() {
        return new DiffRequestManagerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((DiffRequestSubcomponent.Builder) DaggerUtils
                .subcomponentBuilderFor(DiffRequestSubcomponent.class))
                .build()
                .inject(this);
    }

    @Override
    public void onDestroy() {
        mDiffRequestManager.releaseResources();
        super.onDestroy();
    }

    /**
     * @return {@link DiffRequestManager} instance.
     */
    public DiffRequestManager getDiffRequestManager() {
        return mDiffRequestManager;
    }
}
