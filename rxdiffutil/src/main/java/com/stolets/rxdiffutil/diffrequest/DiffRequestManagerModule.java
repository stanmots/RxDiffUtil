package com.stolets.rxdiffutil.diffrequest;

import com.stolets.rxdiffutil.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DiffRequestManagerModule {
    @FragmentScope
    @Provides
    DiffRequestManager provideDiffRequestManager() {
        return new DiffRequestManager();
    }
}
