package com.tn07.survey.data.api

import com.google.gson.annotations.SerializedName

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:02
 */
class PageableApiResponse<T>(
    @SerializedName("data") val data: List<T>,
    @SerializedName("meta") val meta: Metadata
)

class Metadata(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("records") val records: Int,
)