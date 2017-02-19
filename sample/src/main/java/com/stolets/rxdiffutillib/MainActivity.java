package com.stolets.rxdiffutillib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stolets.rxdiffutil.DefaultDiffCallback;
import com.stolets.rxdiffutil.RxDiffResult;
import com.stolets.rxdiffutil.RxDiffUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int UPDATE_TIME_IN_SECONDS = 10;
    private List<SampleModel> mSampleModelList;
    private List<SampleModel> mUpdatedSampleModelList;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private int counter = UPDATE_TIME_IN_SECONDS;

    private TextView mCounterTextView;
    private SampleAdapter mSampleAdapter;
    private Button mRestartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCounterTextView = (TextView) findViewById(R.id.counterTextView);
        mRestartButton = (Button) findViewById(R.id.restart_button);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sample_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSampleModelList = new ArrayList<>();
        mUpdatedSampleModelList = new ArrayList<>();

        mSampleAdapter = new SampleAdapter(mSampleModelList);
        recyclerView.setAdapter(mSampleAdapter);

        restartAdapterUpdating();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompositeDisposable.clear();
    }

    public void restartButtonClicked(View view) {
        restartAdapterUpdating();
    }

    private void restartAdapterUpdating() {
        resetToInitialState();

        // Start generating the model list
        mCompositeDisposable.add(getSampleModelListGenerator()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        mUpdatedSampleModelList.add(new SampleModel(String.format(Locale.getDefault(), "New data %d", aLong), aLong.toString()));
                        mCounterTextView.setText(String.format(Locale.getDefault(), "%d", --counter));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "Failed to generate new sample model list", throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "New sample model list has been generated successfully!");

                        RxDiffUtil.with(new DefaultDiffCallback<>(mSampleModelList, mUpdatedSampleModelList, mSampleAdapter))
                                .detectMoves(true)
                                .bindTo(MainActivity.this)
                                .calculate()
                                .subscribe(new Consumer<RxDiffResult>() {
                                    @Override
                                    public void accept(@NonNull RxDiffResult rxDiffResult) throws Exception {
                                        Log.d(TAG, "Diff calculation is complete");
                                        setUpdateCompleteState();
                                    }
                                });
                    }
                }));
    }

    private void resetToInitialState() {
        // Reset everything to the initial state
        counter = UPDATE_TIME_IN_SECONDS;
        mCompositeDisposable.clear();

        mCounterTextView.setVisibility(View.VISIBLE);
        mRestartButton.setVisibility(View.GONE);

        mSampleModelList.clear();
        mUpdatedSampleModelList.clear();

        mSampleModelList.addAll(createInitialList());

        mSampleAdapter.swapData(mSampleModelList);
        mSampleAdapter.notifyDataSetChanged();
    }

    private void setUpdateCompleteState() {
        mCounterTextView.setVisibility(View.GONE);
        mRestartButton.setVisibility(View.VISIBLE);
    }

    private List<SampleModel> createInitialList() {
        return Arrays.asList(new SampleModel("Initial Data 1", "1"),
                new SampleModel("Initial Data 2", "2"),
                new SampleModel("Initial Data 3", "3"),
                new SampleModel("Initial Data 4", "4"),
                new SampleModel("Initial Data 5", "5"));
    }

    private Observable<Long> getSampleModelListGenerator() {
        return Observable.intervalRange(0, UPDATE_TIME_IN_SECONDS, 0, 1, TimeUnit.SECONDS);
    }
}
