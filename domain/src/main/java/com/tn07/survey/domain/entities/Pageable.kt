package com.tn07.survey.domain.entities

/**
 * Created by toannguyen
 * Jul 18, 2021 at 16:58
 */
interface Pageable<T> {
    val items: List<T>
    val page: Int
    val pageSize: Int
    val pages: Int
    val total: Int
}