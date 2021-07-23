package com.tn07.survey.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.survey.model.SurveyResponse
import com.tn07.survey.data.user.models.UserDataModel
import com.tn07.survey.openResource

/**
 * Created by toannguyen
 * Jul 23, 2021 at 14:00
 */
object TestDataProvider {

    val accessTokenJson: String by lazy {
        openResource("access-token.json").use {
            String(it.readBytes())
        }
    }

    val surveysJson: String by lazy {
        openResource("surveys-response.json").use {
            String(it.readBytes())
        }
    }

    val surveysResponse: PageableApiResponse<SurveyResponse> by lazy {
        Gson().fromJson(
            surveysJson,
            object : TypeToken<PageableApiResponse<SurveyResponse>>() {}.type
        )
    }


    val userJson: String by lazy {
        openResource("user-response.json").use {
            String(it.readBytes())
        }
    }

    val userModel: UserDataModel by lazy {
        Gson().fromJson(surveysJson, UserDataModel::class.java)
    }

}