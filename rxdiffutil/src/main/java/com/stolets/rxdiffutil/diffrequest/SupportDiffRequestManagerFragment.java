package com.stolets.rxdiffutil.diffrequest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.stolets.rxdiffutil.util.DaggerUtils;

import javax.inject.Inject;

/**
 * The view-less retained fragment that is used to hold {@link DiffRequestManager}.
 */
public class SupportDiffRequestManagerFragment extends Fragment {
    @Inject
    DiffRequestManager mDiffRequestManager;

    public static SupportDiffRequestManagerFragment newInstance() {
        return new SupportDiffRequestManagerFragment();
    }

    public SupportDiffRequestManagerFragment() {
        ((DiffRequestSubcomponent.Builder) DaggerUtils
                .subcomponentBuilderFor(DiffRequestSubcomponent.Builder.class))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
