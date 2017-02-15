package com.stolets.rxdiffutil.util;

import android.support.annotation.NonNull;

import com.stolets.rxdiffutil.di.DaggerRootHolder;
import com.stolets.rxdiffutil.di.SubcomponentBuilder;

import static com.stolets.rxdiffutil.internal.Preconditions.checkNotNull;

public final class DaggerUtils {
    /**
     * Retrieves the subcomponent builder according to the given class.
     * @param builderClass {@link Class} of the subcomponent builder.
     * @return {@link SubcomponentBuilder}.
     */
    @NonNull
    public static SubcomponentBuilder<?> subcomponentBuilderFor(@NonNull final Class<?> builderClass) {
        checkNotNull(builderClass, "builderClass must not be null!");

       return DaggerRootHolder.getInstance()
                .getRootComponent()
                .subcomponentBuilders()
                .get(builderClass)
                .get();
    }
}
