package com.stolets.rxdiffutil.di;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SubcomponentBinders.class})
@SuppressWarnings("WeakerAccess")
public interface RootComponent {
    /**
     * @return A map with all the subcomponent builders mapped by their class.
     */
    Map<Class<?>, Provider<SubcomponentBuilder<?>>> subcomponentBuilders();
}
