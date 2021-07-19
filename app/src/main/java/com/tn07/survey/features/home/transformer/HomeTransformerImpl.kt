package com.tn07.survey.features.home.transformer

import android.content.Context
import com.tn07.survey.R
import com.tn07.survey.domain.entities.Survey
import com.tn07.survey.domain.entities.User
import com.tn07.survey.domain.exceptions.ConnectionException
import com.tn07.survey.features.home.uimodel.SurveyUiModel
import com.tn07.survey.features.home.uimodel.UserUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by toannguyen
 * Jul 17, 2021 at 17:43
 */
class HomeTransformerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : HomeTransformer {

    private val simpleDateFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
    override val todayDateTime: String
        get() = simpleDateFormat.format(Date())

    override fun transformUser(user: User): UserUiModel {
        return UserUiModel(
            email = user.email,
            avatar = user.avatarUrl
        )
    }

    override fun transformSurvey(survey: Survey): SurveyUiModel {
        return SurveyUiModel(
            id = survey.id,
            title = survey.title,
            description = survey.description,
            backgroundImageUrl = survey.coverImageUrl
        )
    }

    override fun transformErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is ConnectionException -> {
                context.resources.getString(R.string.error_connection)
            }
            else -> "${throwable.javaClass.name}: ${throwable.localizedMessage}"
        }
    }
}