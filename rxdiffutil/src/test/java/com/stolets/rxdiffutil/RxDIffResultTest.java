package com.stolets.rxdiffutil;

import android.support.v7.util.DiffUtil;

import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RxDIffResultTest extends BaseTest {
    private static final String TEST_TAG = "TEST_TAG";
    @Mock
    DiffUtil.DiffResult mDiffResult;

    @Test
    public void it_ImplementsEquivalenceRelation() {
        // Given
        final RxDiffResult diffResult1 = new RxDiffResult(TEST_TAG, mDiffResult);
        final RxDiffResult diffResult2 = new RxDiffResult(TEST_TAG, mDiffResult);

        // When diffResult1 is equal to diffResult2

        // Then it is
        // reflexive
        assertThat(diffResult1, equalTo(diffResult1));

        // symmetric
        assertThat(diffResult1, equalTo(diffResult2));
        assertThat(diffResult2, equalTo(diffResult1));

        // transitive
        final RxDiffResult diffResult3 = new RxDiffResult(TEST_TAG, mDiffResult);
        assertThat(diffResult1.equals(diffResult3), is(true));
        assertThat(diffResult2.equals(diffResult3), is(true));
        assertThat(diffResult1.equals(diffResult2), is(true));

        // consistent
        assertThat(diffResult1.equals(diffResult2), is(true));
        assertThat(diffResult1.hashCode(), equalTo(diffResult2.hashCode()));

        // unequal to null
        //noinspection ObjectEqualsNull
        assertThat(diffResult1.equals(null), is(false));
    }
}
