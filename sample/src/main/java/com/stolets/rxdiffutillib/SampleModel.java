package com.stolets.rxdiffutillib;

import android.support.annotation.NonNull;

import com.stolets.rxdiffutil.Identifiable;

@SuppressWarnings("WeakerAccess")
public final class SampleModel implements Identifiable<String> {
    @NonNull
    private final String mData;
    @NonNull
    private final String mId;

    SampleModel(@NonNull final String data, @NonNull final String id) {
        this.mData = data;
        this.mId = id;
    }

    @NonNull
    public String getData() {
        return mData;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Override
    public String toString() {
        return "SampleModel{" +
                "mData='" + mData + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleModel that = (SampleModel) o;

        return mData.equals(that.mData) &&
                mId.equals(that.mId);
    }

    @Override
    public int hashCode() {
        int result = mData.hashCode();
        result = 31 * result + mId.hashCode();
        return result;
    }
}
