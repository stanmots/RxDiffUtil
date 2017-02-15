package com.stolets.rxdiffutil.di;


/**
 * The base interface for the subcomponent builders used by Dagger.
 *
 * @param <T> The type of the subcomponent to build.
 */
public interface SubcomponentBuilder<T> {
    /**
     * @return The created subcomponent.
     * @see <a href="https://google.github.io/dagger/api/latest/dagger/Component.Builder.html"</a>
     */
    T build();
}
