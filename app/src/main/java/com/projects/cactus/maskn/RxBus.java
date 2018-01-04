package com.projects.cactus.maskn;

import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.SingleSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by el on 10/29/2017.
 */

public class RxBus {

    private static final BehaviorSubject<Object> behaviorSubject = BehaviorSubject.create();
    private static final AsyncSubject<Object> asyncSubject = AsyncSubject.create();
    private static final SingleSubject<Object> singleSubject = SingleSubject.create();
    private static final PublishSubject<Object> publishSubject = PublishSubject.create();
    private static final ReplaySubject<Object> replaySubject = ReplaySubject.create();


    public  PublishSubject<Object> getPublishSubject() {
        return publishSubject;
    }

    public  ReplaySubject<Object> getReplaySubject() {
        return replaySubject;
    }


    public  BehaviorSubject<Object> getBehaviorSubject() {
        return behaviorSubject;
    }

    public  AsyncSubject<Object> getAsyncSubject() {
        return asyncSubject;
    }

    public  SingleSubject<Object> getSingleSubject() {
        return singleSubject;
    }



}
