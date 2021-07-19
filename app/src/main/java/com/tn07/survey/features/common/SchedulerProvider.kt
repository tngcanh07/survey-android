package com.tn07.survey.features.common

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 19, 2021 at 11:59
 */
interface SchedulerProvider {
    fun io(): Scheduler

    fun computation(): Scheduler

    fun mainThread(): Scheduler
}

class SchedulerProviderImpl @Inject constructor() : SchedulerProvider {
    override fun io(): Scheduler = Schedulers.io()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
}
