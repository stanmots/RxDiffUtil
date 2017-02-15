package com.stolets.rxdiffutil.util;

import com.stolets.rxdiffutil.diffrequest.DiffRequestSubcomponent;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class DaggerUtilsTest {
    @Test
    public void subcomponentBuilderFor_ReturnsNotNullSubcomponentBuilder() {
        // Given
        final Class<?> builderClass = DiffRequestSubcomponent.Builder.class;

        // When
        final DiffRequestSubcomponent.Builder builder = (DiffRequestSubcomponent.Builder) DaggerUtils.subcomponentBuilderFor(builderClass);

        // Then
        assertThat(builder, notNullValue());
    }
}
