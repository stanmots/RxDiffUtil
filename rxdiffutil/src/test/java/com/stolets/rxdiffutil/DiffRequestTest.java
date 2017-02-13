package com.stolets.rxdiffutil;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DiffRequestTest extends BaseTest {
    private final List<String> oldData = Arrays.asList("s1", "s2");
    private final List<String> newData = Arrays.asList("s2", "s3");

    @Mock
    private DataComparable<String> mDataComparable;

    @Test
    public void it_ImplementsEquivalenceRelation() {
        // Given
        final DiffRequest<String> diffRequest1 = new DiffRequest<>(mDataComparable, oldData, newData, true);
        final DiffRequest<String> diffRequest2 = new DiffRequest<>(mDataComparable, oldData, newData, true);

        // When diffRequest1 is equal to diffRequest2

        // Then it is
        // reflexive
        assertThat(diffRequest1, equalTo(diffRequest1));

        // symmetric
        assertThat(diffRequest1, equalTo(diffRequest2));
        assertThat(diffRequest2, equalTo(diffRequest1));

        // transitive
        final DiffRequest<String> diffRequest3 = new DiffRequest<>(mDataComparable, oldData, newData, true);
        assertThat(diffRequest1.equals(diffRequest3), is(true));
        assertThat(diffRequest2.equals(diffRequest3), is(true));
        assertThat(diffRequest1.equals(diffRequest2), is(true));

        // consistent
        assertThat(diffRequest1.equals(diffRequest2), is(true));
        assertThat(diffRequest1.hashCode(), equalTo(diffRequest2.hashCode()));

        // unequal to null
        //noinspection ObjectEqualsNull
        assertThat(diffRequest1.equals(null), is(false));
    }
}