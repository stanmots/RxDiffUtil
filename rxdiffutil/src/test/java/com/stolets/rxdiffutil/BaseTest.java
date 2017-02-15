package com.stolets.rxdiffutil;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class BaseTest {
    /**
     * The @Before methods of the base class will be run before those of the subclasses.
     */
    @Before
    public void setupBaseBefore() {
        MockitoAnnotations.initMocks(this);
    }
}
