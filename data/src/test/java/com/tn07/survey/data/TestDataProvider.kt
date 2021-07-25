package com.tn07.survey.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tn07.survey.data.api.PageableApiResponse
import com.tn07.survey.data.db.entity.SurveyEntity
import com.tn07.survey.data.survey.model.SurveyResponse
import com.tn07.survey.data.user.models.UserDataModel
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.openResource
import org.junit.Assert

/**
 * Created by toannguyen
 * Jul 23, 2021 at 14:00
 */
internal object TestDataProvider {

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

    fun generateSurvey(
        id: String? = null,
        title: String? = null,
        description: String? = null,
        coverImageUrl: String? = null
    ): Survey {
        return object : Survey {
            override val id = id ?: "id-${Math.random()}-${System.currentTimeMillis()}"
            override val title = title ?: "title-${System.currentTimeMillis()}"
            override val description = description ?: "description-${System.currentTimeMillis()}"
            override val coverImageUrl =
                coverImageUrl ?: "https://coverImageUrl-${System.currentTimeMillis()}"
        }
    }

    fun generaSurveyEntity(
        id: String? = null,
        title: String? = null,
        description: String? = null,
        coverImageUrl: String? = null
    ): SurveyEntity {
        return SurveyEntity(
            id = id ?: "id-${Math.random()}-${System.currentTimeMillis()}",
            title = title ?: "title-${System.currentTimeMillis()}",
            description = description ?: "description-${System.currentTimeMillis()}",
            coverImageUrl = coverImageUrl ?: "https://coverImageUrl-${System.currentTimeMillis()}",
        )
    }

    fun assertSurvey(expected: Survey, actual: Survey) {
        Assert.assertEquals(expected.id, actual.id)
        Assert.assertEquals(expected.title, actual.title)
        Assert.assertEquals(expected.description, actual.description)
        Assert.assertEquals(expected.coverImageUrl, actual.coverImageUrl)
    }

    fun assertSurveys(expected: List<Survey>, actual: List<Survey>) {
        Assert.assertEquals(expected.size, actual.size)
        expected.forEachIndexed { index, survey ->
            assertSurvey(survey, actual[index])
        }
    }
}