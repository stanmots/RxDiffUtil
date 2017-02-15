package com.stolets.rxdiffutil.diffrequest;

import com.stolets.rxdiffutil.di.FragmentScope;
import com.stolets.rxdiffutil.di.SubcomponentBuilder;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {DiffRequestManagerModule.class})
public interface DiffRequestSubcomponent {
    void inject(DiffRequestManagerFragment fragment);
    void inject(SupportDiffRequestManagerFragment supportFragment);

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder<DiffRequestSubcomponent> {

    }
}
