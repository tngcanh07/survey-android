package com.tn07.survey.features.home.view

import android.util.Pair
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by toannguyen
 * Jul 17, 2021 at 22:34
 */
class SurveyAdapter(
    private val onOpenSurveyDetail: (SurveyUiModel) -> Unit
) : RecyclerView.Adapter<SurveyViewHolder>() {

    private var items = emptyList<SurveyUiModel>()

    fun bindDataSource(dataSource: Flowable<List<SurveyUiModel>>): Disposable {
        return dataSource.filter { it != items }
            .map { newList ->
                val oldList = items
                val result = DiffUtil.calculateDiff(SurveyDiffCallback(oldList, newList))
                return@map Pair.create(newList, result)
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pair ->
                items = pair.first
                pair.second.dispatchUpdatesTo(this)
            }
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        return SurveyViewHolder(parent, onOpenSurveyDetail)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}