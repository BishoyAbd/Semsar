package com.projects.cactus.maskn.utils.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.Scheduler;


/**
 * Implementation of the {@link BaseSchedulerProvider} making all {@link Scheduler}s trampoline.
 */
public class TrampolineSchedulerProvider implements BaseSchedulerProvider {

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return Schedulers.trampoline();
    }
}
