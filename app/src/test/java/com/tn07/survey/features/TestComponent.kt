package com.tn07.survey.features

import com.tn07.survey.features.common.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by toannguyen
 * Jul 23, 2021 at 16:22
 */
object TestComponent {
    val schedulerProvider: SchedulerProvider = object : SchedulerProvider {
        override fun io(): Scheduler = Schedulers.trampoline()

        override fun computation(): Scheduler = Schedulers.trampoline()

        override fun mainThread(): Scheduler = Schedulers.trampoline()
    }
}
