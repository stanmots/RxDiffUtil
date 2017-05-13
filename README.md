RxDiffUtil
=====

[![Build Status](https://travis-ci.org/storix/RxDiffUtil.svg?branch=master)](https://travis-ci.org/storix/RxDiffUtil)
[![codecov](https://codecov.io/gh/storix/RxDiffUtil/branch/master/graph/badge.svg)](https://codecov.io/gh/storix/RxDiffUtil)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/storix/RxDiffUtil/blob/master/LICENSE)
[ ![Download](https://api.bintray.com/packages/storix/maven/rxdiffutil/images/download.svg) ](https://bintray.com/storix/maven/rxdiffutil/_latestVersion)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.storix/rxdiffutil/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.storix/rxdiffutil)

RxDiffUtil is a lightweight Rx wrapper around [DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil.html) from Android support library.

Under the hood it automates a lot of things, such as background processing of multiple [calculateDiff][1] operations, binding to `Activity` lifecycle (`AppCompatActivity` is also supported), automatic `RecyclerView.Adapter` updating etc.

The library is fully compatible with [RxJava2](https://github.com/ReactiveX/RxJava). It's very flexible -  you can configure it very easily to suit your needs. :metal::tada:

****

### Requirements
* minSdkVersion: 14

### Dependency

* Gradle 

```groovy
compile 'io.github.storix:rxdiffutil:0.3.0'
```

* Maven

```xml
<dependency>
  <groupId>io.github.storix</groupId>
  <artifactId>rxdiffutil</artifactId>
  <version>0.3.0</version>
  <type>pom</type>
</dependency>
```

* Ivy
```xml
<dependency org='io.github.storix' name='rxdiffutil' rev='0.3.0'>
  <artifact name='rxdiffutil' ext='pom' ></artifact>
</dependency>
```


### What is this all about?
Using `RecyclerView` (and `RecyclerView.Adapter` as a consequence) we often encounter situations when we need to make a lot of changes to the data source and then notify somehow our adapter in the most efficient way :chart_with_upwards_trend:.

The Android developers can use the following methods to notify the adapter about the underlying model changes:

 Method | Description           
 --------|:----:
 `notifyItemChanged(int pos)` | Notify that item at position has changed.
  `notifyItemInserted(int pos)` | Notify that item reflected at position has been newly inserted.
   `notifyItemRemoved(int pos)` | Notify that items previously located at position has been removed from the data set.
    `notifyDataSetChanged()` | Notify that the dataset has changed. Use only as last resort.

_Note: check out [this](https://guides.codepath.com/android/using-the-recyclerview#overview) amazing tutorial to know more about RecyclerView configuration._

Ok, cool. Imagine we have inserted a new item at the beginning of our model list, we must then notify the adapter as follows:

```java
// Notify the adapter that an item was inserted at position 0
adapter.notifyItemInserted(0)
```
And then we've removed some item in the middle. And then changed 50 scattered items. This becomes not cool very quickly :sweat:. Especially taking into account that our lists can be pretty large.

Of course, we could just call `notifyDataSetChanged()`.  However, it's **NOT** recommended to do that. `notifyDataSetChanged()` eliminates the ability to perform animation sequences to showcase what changed.

Fortunately, `DiffUtil` comes here to our rescue. This tiny tool can be used to compute the difference between the old and new list and notify the adapter with one line of code:

```java
 // calls adapter's proper notify methods after diffResult is computed
diffResult.dispatchUpdatesTo(adapter);
```

It seems that `DiffUtil` completely solves the problem of the efficient adapter updating. But, there is one catch. This helpful utility still involves some boilerplate code:

* Firstly, we must implement a class that implements the `DiffUtil.Callback` required methods. And if we have multiple recycler views this step should be repeated.

* As stated in the documentation:

> If the lists are large, this operation may take significant time so you are advised to run this on a **background thread**, get the DiffUtil.DiffResult then apply it on the RecyclerView on the main thread.

* We must ensure that the adapter is notified right after the data source is updated. Here is the quote from the docs:

> Note that the RecyclerView requires you to dispatch adapter updates **immediately** when you change the data (you cannot defer notify* calls).


* Also take into account that there is no way by default to cancel `calculateDiff` operation. So we must provide some other way to discard `DiffUtil.DiffResult` when we do not need it (e.g., `Activity` has been finished).

And here is the next tool that comes to our rescue: **RxDiffUtil**.

### Features

* Provides Rx interface to `DiffUtil`
* Automatically cancels all operations (unsubscribes all current subscriptions) when `Activity` has been destroyed and finished
* Subscriptions can be bound to both `android.app.Activity` and `android.support.v7.app.AppCompatActivity`
* Automatically configures background threads
* Automatically updates `RecyclerView.Adapter` on the main thread
* Provides the default implementation of `DiffUtil.Callback` which can be easily integrated with the existing code base
* Can manage multiple `RecyclerView`s


### Usage

##### 1. If you want to update the adapter manually
First set the binding in your `Activity`'s `onCreate`:

```java
private DiffRequestManager mDiffRequestManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // The manager can be injected using Dagger
        mDiffRequestManager = RxDiffUtil
                                .bindTo(this)
                                .getDefaultManager();
                                
        mCompositeDisposable.add(mDiffRequestManager
                                .diffResults()
                                .subscribe( rxDiffResult -> {
                                     // Update your adapter here
                                     // Note: If the diff calculation was finished when the activity has been destroyed this method will be called as soon as the new activity is created
                                });                             
}
```


Then, when the new data has been received, call the following:


```java
// At this point you have received the new data (possibly using some async request)
mDiffRequestManager
                   .newDiffRequestWith(diffCallback)
                   .detectMoves(true)
                   .calculate();
```

You can use [DefaultDiffCallback](https://github.com/storix/RxDiffUtil/blob/master/rxdiffutil/src/main/java/com/stolets/rxdiffutil/DefaultDiffCallback.java) as a helper to create the diff callback:

```java
.newDiffRequestWith(new DefaultDiffCallback<>(adapter.getCurrentData(), newData))
```

The lists that are passed to the default callback must hold the data model which implements [Identifiable](https://github.com/storix/RxDiffUtil/blob/master/rxdiffutil/src/main/java/com/stolets/rxdiffutil/Identifiable.java) interface. This interface is required to provide the proper unique identifiers of the compared items. [Here](https://github.com/storix/RxDiffUtil/blob/master/sample/src/main/java/com/stolets/rxdiffutillib/SampleModel.java#L25) is the sample implementation.


If you need to update just one `RecyclerView.Adapter` than that's it. When `Activity` is finished all resources will be disposed; when you call `calculate()` the previous operation is cancelled automatically; the main thread won't be blocked.

##### 2. If you want the adapter to be updated automatically

* The adapter must implement [Swappable](https://github.com/storix/RxDiffUtil/blob/master/rxdiffutil/src/main/java/com/stolets/rxdiffutil/Swappable.java) interface. This is required to update data source and notify adapter at the appropriate time. [Here](https://github.com/storix/RxDiffUtil/blob/master/sample/src/main/java/com/stolets/rxdiffutillib/SampleAdapter.java#L60) is the sample implementation.

* Pass the adapter when creating the binding:

```java
private DiffRequestManager<YourModelType, YourAdapterType> mDiffRequestManager;
  
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
          
    mDiffRequestManager = RxDiffUtil
                                  .bindTo(this)
                                  .with(adapter);                            
}
  ```

* And when the new data arrived:

```java
mDiffRequestManager
                   .newDiffRequestWith(diffCallback)
                   .updateAdapterWithNewData(newData)
                   .detectMoves(true)
                   .calculate();
```

`updateAdapterWithNewData` will notify the adapter asynchronously when the diff calculation has been finished even after the configuration change.

### Activity with multiple recycler views

If you have `Activity` which contains multiple `RecyclerView`s then you must also supply unique tags for each diff calculation operation requested for different `RecyclerView`. This is required to correctly find and cancel a previous request and also allows to distinguish between the diff results in case you decide to merge the observables.

For example, consider there are two `RecyclerView`s and we need to calculate the difference for each one at the different points of our app's lifecycle. All you need to do in such situation is to attach different tags for each request:

```java
// Configure the first adapter binding
mDiffRequestManager1 = RxDiffUtil
                                .bindTo(this)
                                .with(adapter, "ADAPTER_TAG1");  

// Configure the second adapter binding
mDiffRequestManager2 = RxDiffUtil
                                .bindTo(this)
                                .with(adapter, "ADAPTER_TAG2");
```

### Thanks

* [RxJava](https://github.com/ReactiveX/RxJava) team
* [CodePath](https://codepath.com/about) team for the amazing Android tutorials


### LICENSE

MIT License

Copyright (c) 2017 Stan Mots (Storix)

See the [LICENSE](https://github.com/storix/RxDiffUtil/blob/master/LICENSE) file for details.

[1]: https://goo.gl/fd1hUL

