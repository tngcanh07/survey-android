package com.tn07.survey.data.api

import com.google.gson.annotations.SerializedName

/**
 * Created by toannguyen
 * Jul 14, 2021 at 23:02
 */
class ApiResponse<T>(
    @SerializedName("data") val data: T
)