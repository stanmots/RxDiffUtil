package com.stolets.rxdiffutil.util;


import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.ExampleModel;

import java.util.Arrays;
import java.util.List;

public final class MockitoUtils {
    public static DefaultDiffCallback<String, ExampleModel> getMockedDefaultDiffCallback() {
        final List<ExampleModel> mOldList = Arrays.asList(new ExampleModel("s1", "id1"), new ExampleModel("s2", "id2"));
        final List<ExampleModel> mNewList = Arrays.asList(new ExampleModel("s1", "id10"), new ExampleModel("s3", "id2"));

        return new DefaultDiffCallback<>(mOldList, mNewList);
    }
}
