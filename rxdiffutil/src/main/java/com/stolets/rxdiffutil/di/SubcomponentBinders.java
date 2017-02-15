package com.stolets.rxdiffutil.di;

import com.stolets.rxdiffutil.diffrequest.DiffRequestSubcomponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

/**
 * Provides subcomponent builders for the @{@link RootComponent} mapping.
 */
@Module(subcomponents = {DiffRequestSubcomponent.class})
interface SubcomponentBinders {
    @Binds
    @IntoMap
    @ClassKey(DiffRequestSubcomponent.Builder.class)
    @SuppressWarnings("unused")
    SubcomponentBuilder<?> provideDiffRequestSubcomponentBuilder(final DiffRequestSubcomponent.Builder builder);
}
