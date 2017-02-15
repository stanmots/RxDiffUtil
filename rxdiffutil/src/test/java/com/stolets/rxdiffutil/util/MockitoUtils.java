package com.stolets.rxdiffutil.util;


import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.TestAdapter;
import com.stolets.rxdiffutil.TestModel;

import java.util.Arrays;
import java.util.List;

public final class MockitoUtils {
    public static DefaultDiffCallback<String, TestModel, TestAdapter<TestModel>> getMockedDefaultDiffCallback() {
        final List<TestModel> mOldList = Arrays.asList(new TestModel("s1", "id1"), new TestModel("s2", "id2"));
        final List<TestModel> mNewList = Arrays.asList(new TestModel("s1", "id10"), new TestModel("s3", "id2"));

        return new DefaultDiffCallback<>(mOldList, mNewList, new TestAdapter<TestModel>());
    }
}
