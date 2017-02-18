package com.stolets.rxdiffutil;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TestAdapter<T> extends RecyclerView.Adapter<TestAdapter.TestViewHolder> implements Updatable<T> {
    @Override
    public TestAdapter.TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(TestAdapter.TestViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void update(List<T> newData) {

    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        TestViewHolder(View itemView) {
            super(itemView);
        }
    }
}
