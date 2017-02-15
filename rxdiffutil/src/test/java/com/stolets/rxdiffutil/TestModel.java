package com.stolets.rxdiffutil;

import android.support.annotation.NonNull;

public final class TestModel implements Identifiable<String> {
    @NonNull
    private final String mData;
    @NonNull
    private final String mId;

    public TestModel(@NonNull final String data, @NonNull final String id) {
        this.mData = data;
        this.mId = id;
    }

    @NonNull
    @Override
    public String getId() {
        return mId;
    }

    @NonNull
    public String getData() {
        return mData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestModel that = (TestModel) o;

        return mData.equals(that.mData);
    }

    @Override
    public int hashCode() {
        return mData.hashCode();
    }
}