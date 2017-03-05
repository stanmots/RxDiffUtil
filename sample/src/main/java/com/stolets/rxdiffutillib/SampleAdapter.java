package com.stolets.rxdiffutillib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stolets.rxdiffutil.Swappable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public final class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> implements Swappable<SampleModel> {
    @NonNull
    private List<SampleModel> mSampleModelList;

    SampleAdapter() {
        mSampleModelList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View sampleView = layoutInflater.inflate(R.layout.item_sample, parent, false);

        return new ViewHolder(sampleView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SampleModel sample = mSampleModelList.get(position);
        final TextView textView = holder.getTextView();
        textView.setText(sample.getData());
    }

    @Override
    public int getItemCount() {
        return mSampleModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;

        public ViewHolder(View view) {
            super(view);

            mTextView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return mTextView;
        }
    }

    @Override
    public void swapData(@NonNull final List<SampleModel> updatedList) {
        this.mSampleModelList = updatedList;
    }

    @NonNull
    public List<SampleModel> getSampleModelList() {
        return mSampleModelList;
    }
}
